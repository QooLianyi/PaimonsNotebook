package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding

data class UserGameRoleData(
    val list: List<Role>
) {
    data class Role(
        val game_biz: String,
        val game_uid: String,
        val is_chosen: Boolean,
        val is_official: Boolean,
        val level: Int,
        val nickname: String,
        val region: String,
        val region_name: String,
    )
}