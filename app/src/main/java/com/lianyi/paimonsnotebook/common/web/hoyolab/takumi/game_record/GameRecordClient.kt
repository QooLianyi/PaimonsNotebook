package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record

import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.extension.request.setXrpcChallenge
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity

class GameRecordClient {
    suspend fun getDailyNote(
        user: UserAndUid,
        challenge: String = "",
    ): ResultData<DailyNoteData> =
        etgDailyNote(user.userEntity, user.playerUid, challenge)

    suspend fun getDailyNote(
        user: UserEntity,
        role: UserGameRoleData.Role,
        challenge: String = "",
    ): ResultData<DailyNoteData> =
        etgDailyNote(user, PlayerUid(role.game_uid, role.region), challenge)

    suspend fun getDailyNoteForWidget(user: UserEntity) = buildRequest {
        url(ApiEndpoints.CardWidgetDataV2)

        setUser(user, CookieHelper.Type.Ltoken or CookieHelper.Type.Stoken)
        setDynamicSecret(DynamicSecret.SaltType.K2)
    }.getAsJson<DailyNoteWidgetData>()

    private suspend fun etgDailyNote(
        user: UserEntity,
        playerUid: PlayerUid,
        challenge: String = "",
    ): ResultData<DailyNoteData> {
        val result = buildRequest {
            url(ApiEndpoints.GameRecordDailyNote(playerUid))

            setUser(user = user, cookieType = CookieHelper.Type.Ltoken)

            setDynamicSecret(
                saltType = DynamicSecret.SaltType.X6,
                version = DynamicSecret.Version.Gen2
            )

            if (challenge.isNotBlank() && challenge != "error") {
                setXrpcChallenge(challenge)
            }

        }.getAsJson<DailyNoteData>()

        return result
    }

}