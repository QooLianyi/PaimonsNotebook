package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

/*
* 养成材料计算结果
* */
data class Consumption(
    val avatar_consume: List<Item>,
    val avatar_skill_consume: List<Item>,
    val reliquary_consume: List<Item>,
    val weapon_consume: List<Item>
){
    data class Item(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )
}