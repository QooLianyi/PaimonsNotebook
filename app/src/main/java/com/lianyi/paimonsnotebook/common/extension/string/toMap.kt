package com.lianyi.paimonsnotebook.common.extension.string

fun String.toMap(split:String,kvSplit:String):Map<String,String>{
    val cookies = mutableMapOf<String, String>()
    this.split(split).toList()
        .forEach {
            val kv = it.split(kvSplit)
            val key = kv.first().trim()
            val value = kv.last().trim()
            if (key.isNotBlank()) {
                cookies[key] = value
            }
        }
    return cookies
}