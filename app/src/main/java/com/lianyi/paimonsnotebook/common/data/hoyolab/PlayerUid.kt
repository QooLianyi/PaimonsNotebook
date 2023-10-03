package com.lianyi.paimonsnotebook.common.data.hoyolab

import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData

data class PlayerUid(val value: String, val region: String) {
    companion object {
        fun fromGameRole(role: UserGameRoleData.Role) =
            PlayerUid(role.game_uid, role.region)
    }
}