package com.lianyi.paimonsnotebook.common.util.request

import android.os.Build
import com.google.gson.Gson
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private val gson by lazy {
    Gson()
}

val defaultOkHttpClient by lazy {
    OkHttpClient.Builder().apply {
        retryOnConnectionFailure(true)

        addInterceptor {
            val request = it.request().newBuilder()
            request.addHeader("x-rpc-client_type", CoreEnvironment.ClientType)
            request.addHeader("x-rpc-app_version", CoreEnvironment.XrpcVersion)
            request.addHeader("x-rpc-sys_version", Build.VERSION.RELEASE)
            request.addHeader("x-rpc-device_fp", CoreEnvironment.DeviceFp)
            request.addHeader("x-rpc-device_name", "${Build.BRAND} ${Build.MODEL}")
            request.addHeader("x-rpc-device_id", CoreEnvironment.DeviceId)
            request.addHeader("x-rpc-device_model", Build.MODEL)
            request.addHeader("x-rpc-channel", "miyousheluodi")
            request.addHeader("x-rpc-app_id", "bll8iq97cem8")
            request.addHeader("User-Agent", CoreEnvironment.HoyolabMobileUA)

            it.proceed(request.build())
        }
    }.build()
}

val emptyOkHttpClient by lazy {
    OkHttpClient.Builder().build()
}

val applicationOkHttpClient by lazy {
    OkHttpClient.Builder().apply {
        retryOnConnectionFailure(true)

        addInterceptor {
            val request = it.request().newBuilder()
            request.addHeader("User-Agent", CoreEnvironment.PaimonsNotebookUA)

            it.proceed(request.build())
        }
    }.build()
}


fun buildRequest(block: Request.Builder.() -> Unit) = Request.Builder().apply(block).build()

fun <K, V> Map<K, V>.toJson(): String = gson.toJson(this)

fun <K, V> Map<K, V>.post(builder: Request.Builder) =
    builder.post(toJson().toRequestBody("application/json".toMediaType()))

//获取字节流
suspend fun Request.getAsByte(client: OkHttpClient = emptyOkHttpClient) =
    withContext(Dispatchers.IO) {
        try {
            client.newCall(this@getAsByte).await().body?.byteStream()
        } catch (e: Exception) {
            val (code, msg) = if (e is SocketTimeoutException || e is UnknownHostException) {
                ResultData.NETWORK_ERROR to "网络异常"
            } else {
                ResultData.UNKNOWN_EXCEPTION to "未知错误"
            }
            null
        }
    }

suspend fun Request.getAsText(client: OkHttpClient = emptyOkHttpClient) =
    withContext(Dispatchers.IO) {
        try {
            client.newCall(this@getAsText).await().body?.string() ?: ""
        } catch (e: Exception) {
            val (code, msg) = if (e is SocketTimeoutException || e is UnknownHostException) {
                ResultData.NETWORK_ERROR to "网络异常"
            } else {
                ResultData.UNKNOWN_EXCEPTION to "未知错误"
            }
            "{\"retcode\":${code},\"message\":\"${msg}\",\"data\":null}"
        }
    }

//获取文字响应体,并返回请求结果状态
suspend fun Request.getAsTextResult(client: OkHttpClient = emptyOkHttpClient) =
    withContext(Dispatchers.IO) {
        var resultText = ""
        try {
            resultText = getAsText(client)
            true
        } catch (e: Exception) {
            false
        } to resultText
    }

suspend fun Request.getAsByteResult(client: OkHttpClient = emptyOkHttpClient) =
    withContext(Dispatchers.IO) {
        var resultText: InputStream? = null
        try {
            resultText = getAsByte(client)
            true
        } catch (e: Exception) {
            false
        } to resultText
    }


//获取原生json
suspend fun <T> Request.getAsJsonNative(
    type: ParameterizedType,
    client: OkHttpClient = defaultOkHttpClient
): T? =
    try {
        Gson().fromJson(getAsText(client), type)
    } catch (e: Exception) {
        println(e.message)
        null
    }

suspend inline fun <reified T> Request.getAsJson(client: OkHttpClient = defaultOkHttpClient): ResultData<T> =
    getAsJson(getParameterizedType(ResultData::class.java, T::class.java), client)

suspend inline fun <reified T> Request.getAsJson(
    type: ParameterizedType,
    client: OkHttpClient = defaultOkHttpClient,
): ResultData<T> {
    return try {
        Gson().fromJson(this.getAsText(client), type)
    } catch (e: Exception) {
        Gson().fromJson(
            "{\"retcode\":${ResultData.RESPONSE_CONVERT_EXCEPTION},\"message\":\"请求结果转换异常\",\"data\":null}",
            getParameterizedType(ResultData::class.java, T::class.java)
        )
    }
}

private suspend fun Call.await(): Response =
    suspendCancellableCoroutine { cancellableContinuation ->
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                cancellableContinuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                cancellableContinuation.resume(response)
            }
        })
    }

