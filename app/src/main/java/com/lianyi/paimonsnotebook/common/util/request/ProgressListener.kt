package com.lianyi.paimonsnotebook.common.util.request


interface ProgressListener {
    fun update(url: String, bytesRead: Long, contentLength: Long, done: Boolean)
}