package com.lianyi.paimonsnotebook.common.extension.string

/*
* 从Url中获取指定的值
*
* 目标值:表达式
* */
fun String.takeValueFromUrl(target: String): String = takeValueFromUrl()[target] ?: ""

fun String.takeValueFromUrl(): Map<String, String> =
    try {
        split("?").last().split("&").associate {
            val split = it.split("=")

            split.first().trim() to split.last().trim()
        }
    } catch (_: Exception) {
        mapOf()
    }