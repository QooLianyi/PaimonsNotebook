package com.lianyi.paimonsnotebook.common.data.hoyolab.user

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.database.user.entity.User

data class UserAndUid(
    val userEntity: User,
    val playerUid: PlayerUid,
)
