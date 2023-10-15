package com.lianyi.paimonsnotebook.common.extension.scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) {
    this.launch(Dispatchers.IO) {
        block.invoke(this)
    }
}

fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) {
    this.launch(Dispatchers.Main) {
        block.invoke(this)
    }
}

fun CoroutineScope.launchUnconfined(block: suspend CoroutineScope.() -> Unit) {
    this.launch(Dispatchers.Unconfined) {
        block.invoke(this)
    }
}