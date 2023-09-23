package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common

/*
* 突破加成
* */
data class PromoteData(
    val AddProperties: List<AddProperty>,
    val Id: Int,
    val Level: Int
){
    data class AddProperty(
        val Type: Int,
        val Value: Float
    )
}