package com.lianyi.paimonsnotebook.common.extension.list

/*
* 用于将集合分割为指定长度的集合
* */
fun <T> List<T>.split(count: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var list = mutableListOf<T>()
    this.forEachIndexed { _, t ->
        list += t
        if (list.size == count) {
            result += list
            list = mutableListOf()
        }
    }
    if(list.isNotEmpty()){
        result += list
    }

    return result
}