package com.lianyi.paimonsnotebook.lib.information

import android.content.Intent
import com.lianyi.paimonsnotebook.bean.PlayerCharacterInformationBean
import com.lianyi.paimonsnotebook.bean.PlayerInformationBean
import com.lianyi.paimonsnotebook.bean.SpiralAbyssBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.ui.activity.SearchResultActivity
import com.lianyi.paimonsnotebook.util.*
import okio.utf8Size
import java.lang.StringBuilder

class MiHoYoApi {
    companion object{
        //JSON数据获取
        const val JSON_DATA = "https://qoolianyi.github.io/PaimonsNotebook.github.io/"

        //首页公告
        const val OFFICIAL_RECOMMEND_POST = "https://bbs-api.mihoyo.com/post/wapi/getOfficialRecommendedPosts?gids=2"
        //黑板 筛选首页活动
        const val BLACK_BOARD = "https://api-static.mihoyo.com/common/blackboard/ys_obc/v1/get_activity_calendar?app_sn=ys_obc"

        //获取详细信息,通过content_id
        const val LOAD_DETAIL_INFO = "https://bbs.mihoyo.com/ys/obc/content//detail?bbs_presentation_style=no_header"

        //首页信息
        const val HOME_PAGER_INFORMATION = "https://bbs-api-static.mihoyo.com/apihub/wapi/webHome?gids=2&page=1&page_size=20"

        //米游社表情
        const val EMOTICON = "https://bbs-api-static.mihoyo.com/misc/api/emoticon_set"

        //大地图
        const val MAP = "https://webstatic.mihoyo.com/app/ys-map-cn/index.html"
        const val MAP_V2 = "https://webstatic.mihoyo.com/ys/app/interactive-map/index.html?bbs_presentation_style=no_header&lang=zh-cn&_markerFps=24"

        //米游社登录
        const val LOGIN = "https://m.bbs.mihoyo.com/ys/#/login"

        //通过login_ticket 获取 itoken和stoken
        const val GET_MULTI_TOKEN = "https://api-takumi.mihoyo.com/auth/api/getMultiTokenByLoginTicket?"

        //通过Cookie获得玩家信息
        const val GET_GAME_ROLES_BY_COOKIE = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn"

        //当前月份签到奖励
        const val GET_CURRENT_MONTH_SIGN_REWARD_INFO = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=e202009291139501"
        //每日签到
        const val DAILY_SIGN = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign"

        const val GET_GACHA_LOG = "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog?"

        //获得角色列表详情 (post) requestBody ={"character_ids":[10000016,10000030...(拥有角色id)],"role_id":"","server":""}
        const val GET_CHARACTER_LIST_DETAIL = "https://api-takumi.mihoyo.com/game_record/app/genshin/api/character"

        //游戏内公告 content
        val ANNOUNCEMENT_URL by lazy {
            "https://hk4e-api-static.mihoyo.com/common/hk4e_cn/announcement/api/getAnnContent?game=hk4e&game_biz=hk4e_cn&lang=zh-cn&bundle_id=hk4e_cn&platform=pc&region=${mainUser!!.region}&t=${System.currentTimeMillis()/1000}&level=${mainUser!!.gameLevel}&channel_id=1"
        }

        //游戏内公告list
        val ANNOUNCEMENT_LIST_URL by lazy {
            "https://hk4e-api.mihoyo.com/common/hk4e_cn/announcement/api/getAnnList?game=hk4e&game_biz=hk4e_cn&lang=zh-cn&bundle_id=hk4e_cn&platform=pc&region=${mainUser!!.region}&level=${mainUser!!.gameLevel}&uid=${mainUser!!.gameUid}"
        }



        //获得便笺url
        fun getDailyNoteUrl(gameUID:String,server:String):String{
            return "https://api-takumi-record.mihoyo.com/game_record/app/genshin/api/dailyNote?role_id=$gameUID&server=$server"
        }

        //获得旅行者札记url
        fun getMonthLedgerUrl(month:Int, gameUID: String, server: String):String{
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

        fun getAccountInformation(loginUid:String):String{
            return "https://bbs-api.mihoyo.com/user/api/getUserFullInfo?uid=$loginUid"
        }

        //获得帖子连接
        fun getArticleUrl(postId:String?):String{
            return if(postId?.contains("article") == true){
                "https://bbs.mihoyo.com/ys/article$postId"
            }else{
                postId?:""
            }
        }

        //获得帖子JSON url
        fun getArticleDataUrl(postId:String?):String{
            return "https://bbs-api.mihoyo.com/post/wapi/getPostFull?gids=2&post_id=${postId}&read=1"
        }

        //通过帖子url 获得帖子id
        fun getArticlePostIdByUrl(url:String):String{
            val tag = "/article/"
            return if(url.contains(tag)){
                val articlePosition = url.indexOf(tag)
                val urlLength = url.length
                val takeCount = urlLength - articlePosition - tag.length
                url.takeLast(takeCount)
            }else{
                url
            }
        }

        //获得Cookie
        fun getCookie(user: UserBean):String{
            return "ltuid=${user.loginUid};ltoken=${user.lToken};account_id=${user.loginUid};cookie_token=${user.cookieToken}"
        }

        //获得查询玩家信息的url
        fun getPlayerInfoUrl(gameUID:String, server: String):String{
            return "https://api-takumi.mihoyo.com/game_record/app/genshin/api/index?role_id=${gameUID}&server=$server"
        }

        //获得查询玩家深渊的url
        fun getAbyssUrl(gameUID: String,server: String):String{
            return "https://api-takumi.mihoyo.com/game_record/app/genshin/api/spiralAbyss?role_id=${gameUID}&schedule_type=1&server=$server"
        }

        //获得玩家深渊数据 回调
        fun getAbyssData(roleId:String, server:String,block:(SpiralAbyssBean)->Unit){
            val query = getAbyssUrl(roleId, server).split("?").last()
            Ok.get(getAbyssUrl(roleId,server),query){
                if(it.ok){
                    block(GSON.fromJson(it.optString("data"), SpiralAbyssBean::class.java))
                }
            }
        }

        //获得玩家角色数据 回调
        fun getCharacterData(playerInformationBean: PlayerInformationBean,roleId: String,server: String,block: (PlayerCharacterInformationBean) -> Unit){
            val characterIds = StringBuilder()

            playerInformationBean.avatars.forEachIndexed{ i: Int, avatarsBean: PlayerInformationBean.AvatarsBean ->
                when(i){
                    0->{
                        characterIds.append("[${avatarsBean.id},")
                    }
                    playerInformationBean.avatars.size-1->{
                        characterIds.append("${avatarsBean.id}]")
                    }
                    else->{
                        characterIds.append("${avatarsBean.id},")
                    }
                }
            }

            val body = """
                {"character_ids":${characterIds},
                "role_id":"$roleId","server":"$server"}
                 """.trimIndent()

            Ok.getCharacter(getCookie(mainUser!!),body,body.toMyRequestBody()){
                if(it.ok){
                    block(GSON.fromJson(it.optString("data"),
                        PlayerCharacterInformationBean::class.java))
                }
            }
        }

        //获得玩家信息
        private var switch = true
        fun getPlayerData(uid:String, server:String = "cn_gf01", block: (Boolean, PlayerInformationBean?, Intent?) -> Unit){
            val query = "role_id=${uid}&server=${server}"

            Ok.get(getPlayerInfoUrl(uid,server),query){
                if(it.ok){
                    val playerInfo = GSON.fromJson(it.optString("data"),
                        PlayerInformationBean::class.java)
                    val intent = Intent(PaiMonsNoteBook.context, SearchResultActivity::class.java)
                    intent.putExtra("roleId",uid)
                    intent.putExtra("server",server)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    switch = true
                    block(true,playerInfo,intent)
                }else{
                    switch = if(switch){
                        getPlayerData(uid, "cn_qd01"){ b: Boolean, playerInformationBean: PlayerInformationBean?, intent:Intent?->
                            block(b,playerInformationBean,intent)
                        }
                        false
                    }else{
                        block(false,null,null)
                        true
                    }
                }
            }
        }
    }
}