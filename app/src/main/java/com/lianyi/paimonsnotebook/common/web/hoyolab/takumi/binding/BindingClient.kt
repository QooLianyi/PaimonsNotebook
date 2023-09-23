package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding

import com.lianyi.paimonsnotebook.common.core.enviroment.EnvironmentClientType
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.extension.request.setXrpcClientType
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper

class BindingClient {

    suspend fun getUserGameRolesByStoken(user: User) =
        buildRequest {
            url(ApiEndpoints.UserGameRolesByStoken)

            setUser(user, CookieHelper.Type.Stoken)
            //x-rpc-client_type为5时使用LK2
            setDynamicSecret(DynamicSecret.SaltType.K2, DynamicSecret.Version.Gen1, true)

        }.getAsJson<UserGameRoleData>()

    suspend fun getUserGameRolesByActionTicket(actionTicket: String, stokenV2: String) =
        buildRequest {
            url(ApiEndpoints.UserGameRolesByActionTicket(actionTicket))

            addHeader("Cookie", stokenV2)
        }.getAsJson<UserGameRoleData>()

    suspend fun generateAuthenticationKey(user: UserAndUid) =
        buildRequest {
            url(ApiEndpoints.BindingGenAuthKey)

            setUser(user.userEntity, CookieHelper.Type.Stoken)
            setDynamicSecret(DynamicSecret.SaltType.LK2, DynamicSecret.Version.Gen1, true)
            setXrpcClientType(EnvironmentClientType.WEB)

            buildMap {
                put("auth_appid", "webview_gacha")
                put("game_biz", "hk4e_cn")
                put("game_uid", user.playerUid.value)
                put("region", user.playerUid.region)
            }.post(this)
        }.getAsJson<GameAuthKeyData>()

    suspend fun changeGameRoleByDefault(
        actionTicket: String,
        gameRole: UserGameRoleData.Role,
    ) = buildRequest {
        url(ApiEndpoints.ChangeGameRoleByDefault)

        buildMap {
            put("action_ticket", actionTicket)
            put("game_biz", gameRole.game_biz)
            put("game_uid", gameRole.game_uid)
            put("region", gameRole.region)
            put("t", System.currentTimeMillis())
        }.post(this)

    }.getAsJson<Unit>()

}