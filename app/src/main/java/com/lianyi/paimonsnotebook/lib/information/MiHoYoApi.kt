package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.config.Settings

class MiHoYoApi {
    companion object{
        //JSON数据获取
        const val JSON_DATA = "https://qoolianyi.github.io/PaimonsNotebook.github.io/"
        //首页banner
        const val HOME_BANNER = "https://bbs-api.mihoyo.com/post/wapi/getOfficialRecommendedPosts?gids=2"
        //首页活动
        const val BLACK_BOARD = "https://api-static.mihoyo.com/common/blackboard/ys_obc/v1/get_activity_calendar?app_sn=ys_obc"

        //获取详细信息,通过content_id
        const val LOAD_DETAIL_INFO = "https://bbs.mihoyo.com/ys/obc/content//detail?bbs_presentation_style=no_header"

        //大地图
        const val MAP = "https://webstatic.mihoyo.com/app/ys-map-cn/index.html"

        //米游社登录
        const val LOGIN = "https://m.bbs.mihoyo.com/ys/#/login"

        //通过login_ticket 获取 itoken和stoken
        const val GET_MULTI_TOKEN = "https://api-takumi.mihoyo.com/auth/api/getMultiTokenByLoginTicket?"

        //通过Cookie获得玩家信息
        const val GET_GAME_ROLES_BY_COOKIE = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=${Constants.GENSHIN_GAME_ID}"


        //当前月份签到奖励
        const val GET_CURRENT_MONTH_SIGN_REWARD_INFO = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=e202009291139501"
        //每日签到
        const val DAILY_SIGN = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign"

        const val GET_GACHA_LOG = "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog?"

        //获得便笺url
        fun getDailyNoteUrl(gameUID:String,server:String):String{
            return "https://api-takumi-record.mihoyo.com/game_record/app/genshin/api/dailyNote?role_id=$gameUID&server=$server"
        }

        //获得旅行者札记url
        fun getMonthInfoUrl(month:String, gameUID: String, server: String):String{
            return "https://hk4e-api.mihoyo.com/event/ys_ledger/monthInfo?month=$month&bind_uid=$gameUID&bind_region=$server&bbs_presentation_style=fullscreen&bbs_auth+required=true&mys_source=GameRecord"
        }

        //获得本月签到信息url
        fun getCurrentMonthSignInfoUrl(gameUID: String, server: String):String{
            return "https://api-takumi.mihoyo.com/event/bbs_sign_reward/info?act_id=e202009291139501&uid=${gameUID}&region=${server}"
        }

        //获得祈愿记录url
        fun getGachaLogUrl(logUrl:String,gachaType: Int,page:Int,size:Int,end_id:String):String{
            val params = logUrl.split("?").last().dropLast(5)
            return "${GET_GACHA_LOG}${params}&gacha_type=${gachaType}&page=${page}&size=${size}&end_id=$end_id"
        }

    }
}