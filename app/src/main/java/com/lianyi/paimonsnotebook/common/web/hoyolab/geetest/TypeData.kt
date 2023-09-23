package com.lianyi.paimonsnotebook.common.web.hoyolab.geetest

data class TypeData(
    val aspect_radio: AspectRadio,
    val beeline: String,
    val click: String,
    val fullpage: String,
    val geetest: String,
    val slide: String,
    val static_servers: List<String>,
    val type: String,
    val voice: String
) {
    data class AspectRadio(
        val beeline: Int,
        val click: Int,
        val slide: Int,
        val voice: Int
    )
}