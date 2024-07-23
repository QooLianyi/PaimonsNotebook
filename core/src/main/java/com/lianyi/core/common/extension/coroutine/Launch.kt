package com.lianyi.core.common.extension.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) =
    this.launch(Dispatchers.IO) {
        block.invoke(this)
    }

fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) =
    this.launch(Dispatchers.Main) {
        block.invoke(this)
    }

fun CoroutineScope.launchUnconfined(block: suspend CoroutineScope.() -> Unit) =
    this.launch(Dispatchers.Unconfined) {
        block.invoke(this)
    }

suspend fun withContextMain(block: suspend CoroutineScope.() -> Unit) =
    withContext(Dispatchers.Main) {
        block.invoke(this)
    }
