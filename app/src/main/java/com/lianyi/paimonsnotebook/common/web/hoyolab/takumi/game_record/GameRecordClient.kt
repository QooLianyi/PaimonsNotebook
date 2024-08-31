package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.extension.request.setXRpcChallenge
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss.SpiralAbyssData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity

class GameRecordClient {
    suspend fun getDailyNote(
        user: UserAndUid,
        challenge: String = "",
    ) = getDailyNote(user.userEntity, user.playerUid, challenge)

    suspend fun getDailyNote(
        user: UserEntity,
        role: UserGameRoleData.Role,
        challenge: String = "",
    ) = getDailyNote(user, PlayerUid(role.game_uid, role.region), challenge)

    suspend fun getDailyNoteForWidget(
        user: UserEntity
    ) = buildRequest {
        url(ApiEndpoints.CardWidgetDataV2)

        setUser(user, CookieHelper.Type.Ltoken or CookieHelper.Type.Stoken)
        setDynamicSecret(DynamicSecret.SaltType.K2)
    }.getAsJson<DailyNoteWidgetData>()

    private suspend fun getDailyNote(
        user: UserEntity,
        playerUid: PlayerUid,
        challenge: String = "",
    ) = buildRequest {
        url(ApiEndpoints.GameRecordDailyNote(playerUid))

        setUser(user = user, cookieType = CookieHelper.Type.Ltoken)

        setDynamicSecret(
            saltType = DynamicSecret.SaltType.X6,
            version = DynamicSecret.Version.Gen2
        )

        if (challenge.isNotBlank() && challenge != "error") {
            setXRpcChallenge(challenge)
        }

    }.getAsJson<DailyNoteData>()

    suspend fun getSpiralAbyssData(
        user: UserAndUid,
        scheduleType: String,
    ) = buildRequest {
        url(ApiEndpoints.gameRecordSpiralAbyss(scheduleType = scheduleType, uid = user.playerUid))

        setUser(user.userEntity, CookieHelper.Type.Cookie)
        //client_type = 5时使用 X4
        setDynamicSecret(DynamicSecret.SaltType.X6, DynamicSecret.Version.Gen2)
    }.getAsJson<SpiralAbyssData>()

    suspend fun getCharacterList(
        user: UserAndUid,
        sortType: Int = 1
    ) = buildRequest {
        url(ApiEndpoints.gameRecordCharacterList)

        setUser(user.userEntity, CookieHelper.Type.Cookie)

        buildMap {
            put("role_id", user.playerUid.value)
            put("server", user.playerUid.region)
            put("sort_type", sortType)
        }.post(this)

    }.getAsJson<CharacterListData>()

    suspend fun getCharacterDetail(
        user: UserAndUid,
        characterIds: List<Int>
    ) = buildRequest {
        url(ApiEndpoints.gameRecordCharacterDetail)

        setUser(user.userEntity, CookieHelper.Type.Cookie)

        buildMap {
            put("role_id", user.playerUid.value)
            put("server", user.playerUid.region)
            put("character_ids", characterIds)
        }.post(this)

    }.getAsJson<CharacterDetailData>()

}