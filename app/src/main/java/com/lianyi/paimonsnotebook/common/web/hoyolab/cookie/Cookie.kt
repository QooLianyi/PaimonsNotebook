package com.lianyi.paimonsnotebook.common.web.hoyolab.cookie

/*
* cookie封装类
* */
class Cookie {
    private val map = mutableMapOf<String, String>()

    override fun toString(): String = map.map { "${it.key}=${it.value}" }.joinToString(separator = ";", postfix = ";")

    operator fun set(key: String, value: String) {
        map[key] = value
    }

    operator fun get(key: String): String? = map[key]

    fun parse(string: String) {
        map.putAll(CookieHelper.stringToCookieMap(string))
    }

    fun foreach(block: (String, String) -> Unit) {
        map.forEach { (key, value) ->
            block(key, value)
        }
    }

}