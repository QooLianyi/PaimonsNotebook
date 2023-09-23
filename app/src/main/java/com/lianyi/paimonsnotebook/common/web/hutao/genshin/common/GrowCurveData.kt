package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common

/*
* 成长曲线
* */
data class GrowCurveData(
    val Curves: List<Curve>,
    val Level: Int
){
    data class Curve(
        val Type: Int,
        val Value: Float
    )
}