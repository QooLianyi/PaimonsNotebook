package com.lianyi.paimonsnotebook.common.data.hoyolab.user

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.extension.map.cookieString
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user.UserFullInfoData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData

/*
* 账号
*
* */
data class AccountData(
    val userInfo: UserFullInfoData.UserInfo,
    val userGameRoles: List<UserGameRoleData.Role>,
    val isDefault: Boolean = false,
    val CookieToken: Map<String, String>,
    val Stoken: Map<String, String>,
    val Ltoken: Map<String, String>,
    val playerUid: PlayerUid = PlayerUid("", ""),
) {
    val available: Boolean
        get() = userInfo != null && userGameRoles != null && CookieToken != null && Stoken != null && Ltoken != null && playerUid != null

    val cookie: String
        get() = "${Stoken.cookieString}${Ltoken.cookieString}${CookieToken.cookieString}"
}
