package com.lianyi.paimonsnotebook.common.data.hoyolab.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user.UserFullInfoData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity

data class User(
    val userEntity: UserEntity,
    val userInfo: UserFullInfoData.UserInfo,
    val userGameRoles: SnapshotStateList<UserGameRoleData.Role>,
    val isAvailable: Boolean
) {
    var isSelected by mutableStateOf(userEntity.isSelected)

    fun getSelectedGameRole(): UserGameRoleData.Role? = userGameRoles.takeFirstIf { it.is_chosen }

    fun getUserAndUid(): UserAndUid? {
        val role = getSelectedGameRole() ?: return null
        return UserAndUid(userEntity = userEntity, playerUid = role.getPlayerUid())
    }

}