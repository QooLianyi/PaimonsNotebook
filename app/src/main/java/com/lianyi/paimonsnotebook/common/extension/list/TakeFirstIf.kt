package com.lianyi.paimonsnotebook.common.extension.list

fun <T> List<T>.takeFirstIf(predicate: (T) -> Boolean): T? {
    //多线程并发时可能会出现A线程修改集合,B线程读取集合
    return try {
        val index = this.indexOfFirst(predicate)
        if (index == -1) null else this[index]
    } catch (e: Exception) {
        null
    }
}