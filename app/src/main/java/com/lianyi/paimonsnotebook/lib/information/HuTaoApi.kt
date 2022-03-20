package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.util.toMyRequestBody

class HuTaoApi {
    companion object{
        //登录
        const val LOGIN = "http://101.35.51.175:5001/Auth/Login"
        //总览数据
        const val OVER_VIEW = "http://101.35.51.175:5001/Statistics/Overview"
        //角色出场数据
        const val AVATAR_PARTICIPATION = "http://101.35.51.175:5001/Statistics/AvatarParticipation"
        //圣遗物数据
        const val AVATAR_RELIQUARY_USAGE = "http://101.35.51.175:5001/Statistics/AvatarReliquaryUsage"
        //组队数据
        const val TEAM_COLLOCATION = "http://101.35.51.175:5001/Statistics/TeamCollocation"
        //武器使用数据
        const val WEAPON_USAGE = "http://101.35.51.175:5001/Statistics/WeaponUsage"
        //角色命座数据
        const val CONSTELLATION = "http://101.35.51.175:5001/Statistics/Constellation"
        //队伍使用数据
        const val TEAM_COMBINATION = "http://101.35.51.175:5001/Statistics/TeamCombination"

        //获得圣遗物数据
        const val GET_RELIQUARIES = "http://101.35.51.175:5001/GenshinItems/Reliquaries"
        //获得武器数据
        const val GET_WEAPONS  = "http://101.35.51.175:5001/GenshinItems/Weapons"
        //获得角色数据
        const val GET_AVATARS = "http://101.35.51.175:5001/GenshinItems/Avatars"

        const val APP_ID = "08d9e7ea-7b27-45c1-8110-bd996f053d90"
        const val SECRET = "xlcuZ5xS+ER5SHo3LLEfOstNrKxTJfMPWZwIfdXvNPk="

        //上传数据前上传一次genshin_item
        const val POST_GENSHIN_ITEM = "http://101.35.51.175:5001/GenshinItems/Upload"

        //请求的TOKEN sp
        const val SP_TOKEN = "hutao_token"

        const val RECORD_UPLOAD = "http://101.35.51.175:5001/Record/Upload"

        val LOGIN_REQUEST_BODY by lazy {
             """
                {
                  "appId": "$APP_ID",
                  "secret": "$SECRET"
                }
            """.trimIndent().toMyRequestBody()
        }
    }
}