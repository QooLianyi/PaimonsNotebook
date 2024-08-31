package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character

data class CharacterListData(
    val list: List<CharacterData>
){
    data class CharacterData(
        val actived_constellation_num: Int,
        val element: String,
        val fetter: Int,
        val icon: String,
        val id: Int,
        val image: String,
        val is_chosen: Boolean,
        val level: Int,
        val name: String,
        val rarity: Int,
        val side_icon: String,
        val weapon: Weapon,
        val weapon_type: Int
    )

    data class Weapon(
        val affix_level: Int,
        val icon: String,
        val id: Int,
        val level: Int,
        val rarity: Int,
        val type: Int
    )
}