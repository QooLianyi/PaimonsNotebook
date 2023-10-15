package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth

import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper

class AuthClient {

    suspend fun getAuthMultiToken(login_ticket: String, stuid: String) =
        buildRequest {
            url(ApiEndpoints.AuthMultiToken(login_ticket, stuid))
        }.getAsJson<MultiTokenByLoginTicketData>()

    suspend fun getActionTicketBySToken(user: User, action: String = "game_role") =
        buildRequest {
            url(ApiEndpoints.AuthActionTicket(action, "", user.aid))

            setUser(user, CookieHelper.Type.Stoken)
            setDynamicSecret(DynamicSecret.SaltType.K2, DynamicSecret.Version.Gen1, true)
        }.getAsJson<ActionTicketData>()

    suspend fun getGameToken(user: User) =
        buildRequest {
            url(ApiEndpoints.getGameToken(user.aid))

            setUser(user,CookieHelper.Type.Stoken)
        }.getAsJson<GameTokenData>()

}