package com.lianyi.paimonsnotebook.lib.information

class Constants {
    companion object{
        //每日签到用的SALT
        const val SIGN_SALT = "4a8knnbk5pbjqsrudp3dq484m9axoc5g"
        const val SIGN_APP_VERSION = "2.10.1"
        //一般请求用的DS
        const val API_SALT = "xV8v4Qu54lUKrEYFZkJhB8cuOh9Asafs"
        const val APP_VERSION = "2.14.1"


        //原神游戏ID
        const val GENSHIN_GAME_ID = "hk4e_cn"

        //数据缓存 格式为:缓存名称+UID
        const val SP_NAME = "cache_info"
        //缓存便笺
        const val SP_DAILY_NOTE_NAME = "daily_note"
        //缓存本月原石数据
        const val SP_MONTH_LEDGER_NAME = "month_ledger"

        //用户信息缓存
        const val USP_NAME = "user_info"
        //游戏角色信息缓存
        const val CSP_NAME = "character_info"
        //游戏武器信息缓存
        const val WSP_NAME ="weapon_info"

        const val USP_MAIN_USER_NAME = "main_user"
        const val USP_USER_LIST_NAME = "user_list"

        const val STOKEN_NAME = ""
        const val ITOKEN_NAME = ""
        const val LOGIN_TICKET_NAME = "login_ticket"
        const val LOGIN_UID_NAME = "login_uid"
        const val ACCOUNT_ID_NAME = "account_id"
        const val LTUID_NAME = "ltuid"
        const val LTOKEN_NAME = "ltoken"
        const val COOKIE_TOKEN_NAME = "cookie_token"

        const val DAILY_TASK_STATE_NOT_FINISHED = "「每日委托」奖励还未领取"
        const val DAILY_TASK_STATE_FINISHED = "「每日委托」奖励已领取"


        //替换占位符
        const val REPLACE_PLACEHOLDER = "#(*PLACEHOLDER!!)"
    }
}