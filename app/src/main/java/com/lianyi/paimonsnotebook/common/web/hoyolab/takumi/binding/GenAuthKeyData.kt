package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid

data class GenAuthKeyData(
    val authAppId: String,
    val gameBiz: String,
    val playerUid: PlayerUid,
) {
    val gameUid: String
        get() = playerUid.value
    val region: String
        get() = playerUid.region

    val asQueryParameter
        get() = "auth_appid=${authAppId}&game_biz=${gameBiz}&game_uid=${gameUid}&region=${region}"

    companion object {
        fun createForWebViewGacha(playerUid: PlayerUid) =
            GenAuthKeyData("webview_gacha", "hk4e_cn", playerUid)
    }
}