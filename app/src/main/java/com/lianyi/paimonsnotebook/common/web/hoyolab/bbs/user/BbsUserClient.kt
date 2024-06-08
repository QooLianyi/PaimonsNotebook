package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user

import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.extension.request.setReferer
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper

class BbsUserClient {
    suspend fun getFullInfo(user: User) =
        buildRequest {
            url(ApiEndpoints.UserFullInfo)

            setUser(user, CookieHelper.Type.Ltoken)

            setReferer("https://bbs-api.mihoyo.com")
        }.getAsJson<UserFullInfoData>()
}