package com.lianyi.paimonsnotebook.common.extension.string

import com.lianyi.paimonsnotebook.common.util.json.JSON

/*
* JSON 格式校验
* */

//判断是否是空对象(map)字符串
fun String.isEmptyObj() = this == JSON.EMPTY_OBJ

//判断是否是空列表字符串
fun String.isEmptyList() = this == JSON.EMPTY_LIST

private val isObjectRegex by lazy {
    Regex("\\{.*\\}")
}

private val isListRegex by lazy {
    Regex("\\[.*\\]")
}

//判断是否是json的object格式
fun String.isObjectJson() = isObjectRegex.containsMatchIn(this)

//判断是否是json的list格式
fun String.isListJson() = isObjectRegex.containsMatchIn(this)

//判断是否是json格式
fun String.isJson() = isObjectRegex.containsMatchIn(this) || isObjectRegex.containsMatchIn(this)
