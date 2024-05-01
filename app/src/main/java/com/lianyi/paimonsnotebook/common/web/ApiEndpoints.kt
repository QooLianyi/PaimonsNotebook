package com.lianyi.paimonsnotebook.common.web

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData

/*
* API端点
* 从SnapHutao项目中拷贝并进行了修改
* SnapHutao(https://github.com/DGP-Studio/Snap.Hutao)
* */
object ApiEndpoints {

    // https://sdk-static.mihoyo.com/hk4e_cn/mdk/launcher/api/content?filter_adv=true&key=eYd89JmJ&language=zh-cn&launcher_id=18
    // https://sdk-static.mihoyo.com/hk4e_cn/mdk/launcher/api/content?key=eYd89JmJ&language=zh-cn&launcher_id=18

    private const val ApiGeetest = "https://api.geetest.com"
    private const val ApiV6Geetest = "https://apiv6.geetest.com"

    private const val ApiSDK = "https://api-sdk.mihoyo.com"

    private const val ApiTakumi = "https://api-takumi.mihoyo.com"
    private const val ApiTakumiCommon = "${ApiTakumi}/common"
    private const val ApiTakumiAuthApi = "${ApiTakumi}/auth/api"
    private const val ApiTaKumiBindingApi = "${ApiTakumi}/binding/api"

    private const val ApiTakumiMiyoushe = "https://api-takumi.miyoushe.com"
    private const val ApiTakumiMiyousheBindingApi = "${ApiTakumiMiyoushe}/binding/api"

    private const val ApiTakumiMiyousheAuthApi = "${ApiTakumiMiyoushe}/auth/api"

    private const val ApiTakumiRecord = "https://api-takumi-record.mihoyo.com"
    private const val ApiTakumiRecordApi = "${ApiTakumiRecord}/game_record/app/genshin/api"

    private const val ApiTakumiCardApi = "${ApiTakumiRecord}/game_record/app/card/api"
    private const val ApiTakumiCardWApi = "${ApiTakumiRecord}/game_record/app/card/wapi"

    private const val ApiTakumiEvent = "${ApiTakumi}/event"
    private const val ApiTakumiEventCalculate = "${ApiTakumiEvent}/e20200928calculate"

    private const val App = "https://app.mihoyo.com"
    private const val AppAuthApi = "${App}/account/auth/api"

    private const val BbsApi = "https://bbs-api.mihoyo.com"
    private const val BbsApiUserApi = "${BbsApi}/user/wapi"

    private const val BbsApiMiYouShe = "https://bbs-api.miyoushe.com"
    private const val BbsApiMiYouSheApiHub = "https://bbs-api.miyoushe.com/apihub"
    private const val BbsApiMiYouSheTopicApi = "https://bbs-api.miyoushe.com/topic/api"
    private const val BbsApiMiYouShePainterApiTopic =
        "https://bbs-api.miyoushe.com/painter/api/topic"


    private const val Hk4eApi = "https://hk4e-api.mihoyo.com"
    private const val Hk4eApiAnnouncementApi = "${Hk4eApi}/common/hk4e_cn/announcement/api"
    private const val Hk4eApiGachaInfoApi = "${Hk4eApi}/event/gacha_info/api"

    private const val Hk4eSdk = "https://hk4e-sdk.mihoyo.com"

    private const val PassportApi = "https://passport-api.mihoyo.com"
    private const val PassportApiAccountAuthApi = "${PassportApi}/account/auth/api"
    private const val PassportApiV4 = "https://passport-api-v4.mihoyo.com"

    private const val PassportApiAccountMaCnSession = "${PassportApi}/account/ma-cn-session"


    private const val PassportApiStatic = "https://passport-api-static.mihoyo.com"

    private const val PassportApiStaticAccountMaCnPassport =
        "${PassportApiStatic}/account/ma-cn-passport/passport"

    private const val PublicOperationHk4e = "https://public-operation-hk4e.mihoyo.com";
    private const val PublicOperationHk4eGachaInfoApi = "${PublicOperationHk4e}/gacha_info/api";

    private const val SdkStatic = "https://sdk-static.mihoyo.com"
    private const val SdkStaticLauncherApi = "${SdkStatic}/hk4e_cn/mdk/launcher/api"

    private const val AnnouncementQuery =
        "game=hk4e&game_biz=hk4e_cn&lang=zh-cn&bundle_id=hk4e_cn&platform=pc&region=cn_gf01&level=55&uid=100000000"

    private const val ApiStatic = "https://api-static.mihoyo.com"
    private const val ApiStaticCommon = "${ApiStatic}/common"
    private const val BbsApiStatic = "https://bbs-api-static.mihoyo.com"

    private const val PublicDataApi = "https://public-data-api.mihoyo.com"

    private const val WebStaticMiHoYo = "https://webstatic.mihoyo.com"
    private const val WebStaticMiHoYoBBSEvent = "https://webstatic.mihoyo.com/bbs/event"

    const val getFp = "${PublicDataApi}/device-fp/api/getFp"

    /*
    * - WEB：4 MWEB：5 米游社：2
    * */
    fun getExtList(platform: Int) = "${PublicDataApi}/device-fp/api/getExtList?platform=${platform}"

    fun geetestGet(gt: String, challenge: String, w: String = "") =
        "${ApiV6Geetest}/get.php?gt=${gt}&challenge=${challenge}&client_type=android&client_type=android&lang=zh-cn&pt=20&w=${w}"

    /// <summary>
    /// 获取GT码
    /// </summary>
    /// <param name="gt">gt</param>
    /// <returns>GT码Url</returns>
    fun geetestGetType(gt: String) =
        "${ApiGeetest}/gettype.php?gt=${gt}"

    fun GeetestGetTypeV6(gt: String, t: Long) =
        "${ApiV6Geetest}/gettype.php?gt=${gt}&t=${t}"

    /// <summary>
    /// 验证接口
    /// </summary>
    /// <param name="gt">gt</param>
    /// <param name="challenge">challenge流水号</param>
    /// <returns>验证接口Url</returns>
    fun GeetestAjax(gt: String, challenge: String) =
        "${ApiV6Geetest}/ajax.php?gt=${gt}&challenge=${challenge}&lang=zh-cn&client_type=android"

    /// <summary>
    /// 获取 stoken 与 ltoken
    /// </summary>
    /// <param name="actionType">操作类型 game_role</param>
    /// <param name="stoken">Stoken</param>
    /// <param name="uid">uid</param>
    /// <returns>Url</returns>
    fun AuthActionTicket(actionType: String, stoken: String, uid: String) =
        "${ApiTakumiAuthApi}/getActionTicketBySToken?action_type=${actionType}&stoken=${stoken}&uid=${uid}"

    /// <summary>
    /// 获取 stoken 与 ltoken
    /// </summary>
    /// <param name="loginTicket">登录票证</param>
    /// <param name="loginUid">uid</param>
    /// <returns>Url</returns>
    fun AuthMultiToken(loginTicket: String, loginUid: String) =
        "${ApiTakumiAuthApi}/getMultiTokenByLoginTicket?login_ticket=${loginTicket}&uid=${loginUid}&token_types=3"

    /// <summary>
    /// 用户游戏角色
    /// </summary>
    /// <param name="actionTicket">操作凭证</param>
    /// <returns>用户游戏角色字符串</returns>
    fun UserGameRolesByActionTicket(actionTicket: String) =
        "${ApiTaKumiBindingApi}/getUserGameRoles?action_ticket=${actionTicket}&game_biz=hk4e_cn"

    /// <summary>
    /// 用户游戏角色
    /// </summary>
    const val UserGameRolesByCookie =
        "${ApiTaKumiBindingApi}/getUserGameRolesByCookie?game_biz=hk4e_cn"

    /// <summary>
    /// 用户游戏角色
    /// </summary>
    const val UserGameRolesByStoken = "${ApiTakumiMiyousheBindingApi}/getUserGameRolesByStoken"

    /// <summary>
    /// AuthKey
    /// </summary>
    const val BindingGenAuthKey = "${ApiTaKumiBindingApi}/genAuthKey"

    //修改账户默认角色
    const val ChangeGameRoleByDefault = "${ApiTaKumiBindingApi}/changeGameRoleByDefault"

    /// <summary>
    /// 小组件数据
    /// </summary>
    const val CardWidgetData = "${ApiTakumiCardApi}/getWidgetData?game_id=2"

    const val CardWidgetDataV2 = "${ApiTakumiRecord}/game_record/genshin/aapi/widget/v2?game_id=2"

    /// <summary>
    /// 发起验证码
    /// </summary>
    const val CardCreateVerification = "${ApiTakumiCardWApi}/createVerification?is_high=false"

    /// <summary>
    /// 验证验证码
    /// </summary>
    const val CardVerifyVerification = "${ApiTakumiCardWApi}/verifyVerification"

    //米游社验证码
    const val MiYouSheVerifyVerification = "${BbsApiMiYouShe}/misc/api/verifyVerification"

    //发起米游社验证码
    const val CreateMiYouSheVerifyVerification =
        "${BbsApiMiYouShe}/misc/api/createVerification?is_high=false"


    //二维码扫码
    fun getQRCodeScan(gameBiz: String) =
        "${ApiSDK}/${gameBiz}/combo/panda/qrcode/scan"

    //二维码确认
    fun getQRCodeConfirm(gameBiz: String) =
        "${ApiSDK}/${gameBiz}/combo/panda/qrcode/confirm"

    fun getQRCodeFetch() =
        "${ApiSDK}/hk4e_cn/combo/panda/qrcode/fetch"

    fun getQRCodeQuery() =
        "${ApiSDK}/hk4e_cn/combo/panda/qrcode/query"


    /// <summary>
    /// 角色基本信息
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>角色基本信息字符串</returns>
    fun GameRecordRoleBasicInfo(uid: PlayerUid) =
        "${ApiTakumiRecordApi}/roleBasicInfo?role_id=${uid.value}&server=${uid.region}"

    /// <summary>
    /// 角色信息
    /// </summary>
    const val GameRecordCharacter = "${ApiTakumiRecordApi}/character"

    /// <summary>
    /// 游戏记录实时便笺
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>游戏记录实时便笺字符串</returns>
    fun GameRecordDailyNote(uid: PlayerUid) =
        "${ApiTakumiRecordApi}/dailyNote?server=${uid.region}&role_id=${uid.value}"

    /// <summary>
    /// 游戏记录主页
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>游戏记录主页字符串</returns>
    fun GameRecordIndex(uid: PlayerUid) =
        "${ApiTakumiRecordApi}/index?server=${uid.region}&role_id=${uid.value}"

    /// <summary>
    /// 深渊信息
    /// </summary>
    /// <param name="scheduleType">深渊类型</param> 1当期 2上期
    /// <param name="uid">Uid</param>
    /// <returns>深渊信息字符串</returns>
    fun gameRecordSpiralAbyss(scheduleType: String, uid: PlayerUid) =
        "${ApiTakumiRecordApi}/spiralAbyss?role_id=${uid.value}&schedule_type=${scheduleType}&server=${uid.region}"

    /// <summary>
    /// 计算器角色列表 size 20
    /// </summary>
    const val CalculateAvatarList = "${ApiTakumiEventCalculate}/v1/avatar/list"

    /// <summary>
    /// 计算器角色技能列表
    /// </summary>
    /// <param name="avatarId">角色Id</param>
    /// <param name="avatar">元素类型</param>
    /// <returns>技能列表</returns>
    //TODO 角色技能计算
//    fun CalculateAvatarSkillList(Hoyolab.Takumi.Event.Calculate.Avatar avatar)
//    {
//        return "${ApiTakumiEventCalculate}/v1/avatarSkill/list?avatar_id={avatar.Id}&element_attr_id={(int)avatar.ElementAttrId}"
//    }

    /// <summary>
    /// 计算器结果
    /// </summary>
    const val CalculateCompute = "${ApiTakumiEventCalculate}/v2/compute"

    /// <summary>
    /// 计算器洞天摹本
    /// </summary>
    /// <param name="shareCode">分享码</param>
    /// <returns>洞天摹本</returns>
    // &region=cn_gf01
    // ignored
    fun CalculateFurnitureBlueprint(shareCode: String) =
        "${ApiTakumiEventCalculate}/v1/furniture/blueprint?share_code=${shareCode}"

    /// <summary>
    /// 计算器家具计算
    /// </summary>
    const val CalculateFurnitureCompute = "${ApiTakumiEventCalculate}/v1/furniture/compute"

    /// <summary>
    /// 计算器家具列表 size 32
    /// </summary>
    const val CalculateFurnitureList = "${ApiTakumiEventCalculate}/v1/furniture/list"

    /// <summary>
    /// 计算器同步角色详情 size 20
    /// </summary>
    /// <param name="avatarId">角色Id</param>
    /// <param name="uid">uid</param>
    /// <returns>角色详情</returns>
//    fun CalculateSyncAvatarDetail(avatarId:AvatarId,uid:PlayerUid) {
//        return "${ApiTakumiEventCalculate}/v1/sync/avatar/detail?avatar_id={avatarId.Value}&uid={uid.Value}&region={uid.region}"
//    }

    /// <summary>
    /// 计算器同步角色列表 size 20
    /// </summary>
    const val CalculateSyncAvatarList = "${ApiTakumiEventCalculate}/v1/sync/avatar/list"

    /// <summary>
    /// 计算器武器列表 size 20
    /// </summary>
    const val CalculateWeaponList = "${ApiTakumiEventCalculate}/v1/weapon/list"

    /// <summary>
    /// 另一个AuthKey
    /// </summary>
    const val AppAuthGenAuthKey = "${AppAuthApi}/genAuthKey"

    /// <summary>
    /// BBS 指向引用
    /// </summary>
    const val BbsReferer = "https://bbs.mihoyo.com"

    const val AppMihoyoReferer = "https://app.mihoyo.com"

    const val WebStaticMihoyoReferer = "https://webstatic.mihoyo.com"

    //HOST
    const val ApiSdkHost = "api-sdk.mihoyo.com"


    /// <summary>
    /// 用户详细信息
    /// </summary>
    const val UserFullInfo = "${BbsApiUserApi}/getUserFullInfo?gids=2"

    /// <summary>
    /// 查询其他用户详细信息
    /// </summary>
    /// <param name="bbsUid">bbs Uid</param>
    /// <returns>查询其他用户详细信息字符串</returns>
    fun UserFullInfoQuery(bbsUid: String) =
        "${BbsApiUserApi}/getUserFullInfo?uid=${bbsUid}&gids=2"

    /// <summary>
    /// 公告列表
    /// </summary>
    const val AnnList = "${Hk4eApiAnnouncementApi}/getAnnList?${AnnouncementQuery}"

    /// <summary>
    /// 公告内容
    /// </summary>
    const val AnnContent = "${Hk4eApiAnnouncementApi}/getAnnContent?${AnnouncementQuery}"

    /// <summary>
    /// 获取祈愿记录
    /// </summary>
    /// <param name="query">query string</param>
    /// <returns>祈愿记录信息Url</returns>
    fun GachaInfoGetGachaLog(query: String) =
        "${PublicOperationHk4eGachaInfoApi}/getGachaLog?${query}"

    /// <summary>
    /// 获取 CookieToken
    /// </summary>
    const val AccountGetCookieTokenBySToken =
        "${PassportApiAccountAuthApi}/getCookieAccountInfoBySToken"

    /// <summary>
    /// 获取LToken
    /// </summary>
    const val AccountGetLtokenByStoken = "${PassportApiAccountAuthApi}/getLTokenBySToken"

    //获取GameToken
    fun getGameToken(aid: String) =
        "${ApiTakumiMiyousheAuthApi}/getGameToken?uid=${aid}"


    /// <summary>
    /// 登录
    /// </summary>
    const val AccountLoginByPassword = "${PassportApi}/account/ma-cn-passport/app/loginByPassword"

    /// <summary>
    /// 验证 Ltoken 有效性
    /// </summary>
    const val AccountVerifyLtoken = "${PassportApiV4}/account/ma-cn-session/web/verifyLtoken"

    /// <summary>
    /// 创建 ActionTicket
    /// </summary>
    const val AccountCreateActionTicket =
        "${PassportApi}/account/ma-cn-verifier/app/createActionTicketByToken"


    /// <summary>
    /// 启动器资源
    /// </summary>
    /// <param name="launcherId">启动器Id</param>
    /// <param name="channel">通道</param>
    /// <param name="subChannel">子通道</param>
    /// <returns>启动器资源字符串</returns>
    fun SdkStaticLauncherResource(launcherId: String, channel: String, subChannel: String) =
        "${SdkStaticLauncherApi}/resource?key=eYd89JmJ&launcher_id=${launcherId}&channel_id=${channel}&sub_channel_id=${subChannel}"

    //推荐公告
    const val OfficialRecommendedPosts = "${BbsApi}/post/wapi/getOfficialRecommendedPosts?gids=2"

    //获得帖子详情
    fun getPostFull(post_id: Long) =
        "${BbsApiMiYouShe}/post/wapi/getPostFull?gids=2&post_id=${post_id}"

    //活动日历
    const val ActivityCalendar =
        "${ApiStaticCommon}/blackboard/ys_obc/v1/get_activity_calendar?app_sn=ys_obc"

    //近期活动与攻略
    const val NearActivity = "${ApiStaticCommon}/blackboard/ys_obc/v1/home/position?app_sn=ys_obc"

    //祈愿记录卡池
    const val GachaPool = "${ApiTakumiCommon}/blackboard/ys_obc/v1/gacha_pool?app_sn=ys_obc"

    //webHome,包含轮播图等
    const val BbsWebHome = "${BbsApiStatic}/apihub/wapi/webHome?gids=2&page=1&page_size=20"

    fun getGameRecordDailyNote(uid: String, region: String) =
        "https://webstatic.mihoyo.com/app/community-game-records/index.html?bbs_presentation_style=fullscreen#/ys/daily/?role_id=${uid}&server=${region}"

    //内部网页Url

    //获取原神游戏记录url
    fun getGenshinGameRecordUrl(role: UserGameRoleData.Role) =
        "${WebStaticMiHoYo}/app/community-game-records/index.html?bbs_presentation_style=fullscreen#/ys/daily/?role_id=${role.game_uid}&server=${role.region}"

    //原神签到
    const val GenshinSign =
        "${WebStaticMiHoYoBBSEvent}/signin-ys/index.html?bbs_auth_required=true&act_id=e202009291139501&utm_source=bbs&utm_medium=mys&utm_campaign=icon"

    //星穹铁道签到
    const val SRSign =
        "${WebStaticMiHoYoBBSEvent}/signin/hkrpg/e202304121516551.html?bbs_auth_required=true&act_id=e202304121516551&bbs_auth_required=true&bbs_presentation_style=fullscreen&utm_source=bbs&utm_medium=mys&utm_campaign=icon"


    //获取投票内容
    fun getVotes(ownerUid: String, voteIds: String, gids: String = "2") =
        "${BbsApiMiYouSheApiHub}/api/getVotes?gids=${gids}&owner_uid=${ownerUid}&vote_ids=${voteIds}"

    //获取投票结果
    fun getVotesResult(ownerUid: String, voteIds: String, gids: String = "2") =
        "${BbsApiMiYouSheApiHub}/api/getVotesResult?gids=${gids}&owner_uid=${ownerUid}&vote_ids=${voteIds}"

    /*
    * 根据topic_id获取分类信息
    * */
    fun getTopicInfo(id: Long) = "${BbsApiMiYouSheTopicApi}/getTopicFullInfo?id=${id}"

    fun getPainterTopicList(
        topicId: Long,
        listType: String = "UNKNOWN",
        offset: String = "",
        size: Int = 20,
        gameId: Int = 0
    ) =
        "${BbsApiMiYouShePainterApiTopic}/list?topic_id=${topicId}&list_type=${listType}&offset=${offset}&size=${size}&game_id=${gameId}"

    //通过ticket获取stoken?
    fun loginByAuthTicket() =
        "${PassportApiStaticAccountMaCnPassport}/loginByAuthTicket"

    //通过gameToken获取SToken
    val getTokenByGameToken = "${PassportApiAccountMaCnSession}/app/getTokenByGameToken"

}