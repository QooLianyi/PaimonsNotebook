package com.lianyi.paimonsnotebook.common.util.coil

import coil.intercept.Interceptor
import coil.request.ImageResult
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
/*
* 合并相同图片url(memory key)请求的拦截器
*
* from:https://github.com/Ehviewer-Overhauled/Ehviewer/blob/1.8.x.x/app/src/main/java/com/hippo/ehviewer/coil/MergeInterceptor.kt
* */
@OptIn(ExperimentalCoroutinesApi::class)
object MergeInterceptor : Interceptor {
    private val pendingContinuationMap: HashMap<String, MutableList<CancellableContinuation<Unit>>> = hashMapOf()
    private val pendingContinuationMapLock = Any()
    private val notifyScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val EMPTY_LIST = mutableListOf<CancellableContinuation<Unit>>()

    private fun triggerSuccessor(key: String) {
        notifyScope.launch {
            synchronized(pendingContinuationMapLock) {
                // Wake up a pending continuation to continue executing task
                val successor = pendingContinuationMap[key]?.removeFirstOrNull()?.apply { resume(Unit) { triggerSuccessor(key) } }
                // If no successor, delete this entry from hashmap
                successor ?: pendingContinuationMap.remove(key)
            }
        }
    }

    private fun triggerFailure(key: String, e: Throwable) {
        notifyScope.launch {
            synchronized(pendingContinuationMapLock) {
                pendingContinuationMap.remove(key)?.forEach { it.resumeWithException(e) }
            }
        }
    }

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val req = chain.request

//        val key = req.memoryCacheKey?.key?.takeIf { it.startsWith("m/")} ?: return withContext(req.interceptorDispatcher) { chain.proceed(req) }
        val key = req.memoryCacheKey?.key ?: return withContext(req.interceptorDispatcher) { chain.proceed(req) }

        suspendCancellableCoroutine { continuation ->
            synchronized(pendingContinuationMapLock) {
                val existPendingContinuations = pendingContinuationMap[key]
                if (existPendingContinuations == null) {
                    pendingContinuationMap[key] = EMPTY_LIST
                    continuation.resume(Unit) {
                        triggerSuccessor(key)
                    }
                } else {
                    if (existPendingContinuations === EMPTY_LIST) pendingContinuationMap[key] = mutableListOf()
                    pendingContinuationMap[key]!!.apply {
                        add(continuation)
                        continuation.invokeOnCancellation { remove(continuation) }
                    }
                }
            }
        }

        try {
            return withContext(req.interceptorDispatcher) { chain.proceed(req) }.apply {
                // Wake all pending continuations shared with the same memory key since we have written it to memory cache
                notifyScope.launch {
                    synchronized(pendingContinuationMapLock) {
                        pendingContinuationMap.remove(key)?.forEach { it.resume(Unit) }
                    }
                }
            }
        } catch (e: CancellationException) {
            triggerSuccessor(key)
            throw e
        } catch (e: Throwable) {
            // Wake all pending continuations since this request is to be failed
            triggerFailure(key, e)
            throw e
        }
    }
}