package com.lianyi.paimonsnotebook.common.web

/*
*
* */
object HutaoEndpoints {

    private const val HomaSnapGenshinApi = "https://homa.snapgenshin.com"
    private const val HutaoMetadataSnapGenshinApi = "https://hutao-metadata.snapgenshin.com"
    private const val HutaoMetadata2SnapGenshinApi = "https://hutao-metadata2.snapgenshin.com"
    private const val RawGithubUserContentSnapMetadataApi =
        "https://raw.githubusercontent.com/DGP-Studio/Snap.Metadata/main"
    private const val StaticSnapGenshinApi = "https://static.snapgenshin.com"
    private const val StaticZipSnapGenshinApi = "https://static-zip.snapgenshin.com"

    /// <summary>
    /// 胡桃资源主机名
    /// </summary>
    const val StaticHutao = "static.hut.ao"

    /// <summary>
    /// 获取末尾Id
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>获取末尾Id Url</returns>
    fun gachaLogEndIds(uid: String) = "${HomaSnapGenshinApi}/GachaLog/EndIds?Uid=${uid}"

    /// <summary>
    /// 获取祈愿记录
    /// </summary>
    const val GachaLogRetrieve = "${HomaSnapGenshinApi}/GachaLog/Retrieve"

    /// <summary>
    /// 上传祈愿记录
    /// </summary>
    const val GachaLogUpload = "{HomaSnapGenshinApi}/GachaLog/Upload"

    /// <summary>
    /// 获取Uid列表
    /// </summary>
    const val GachaLogUids = "${HomaSnapGenshinApi}/GachaLog/Uids"

    /// <summary>
    /// 删除祈愿记录
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>删除祈愿记录 Url</returns>
    fun gachaLogDelete(uid: String) = "${HomaSnapGenshinApi}/GachaLog/Delete?Uid=${uid}"

    /// <summary>
    /// 获取祈愿统计信息
    /// </summary>
    const val GachaLogStatisticsCurrentEvents =
        "${HomaSnapGenshinApi}/GachaLog/Statistics/CurrentEventStatistics"

    /// <summary>
    /// 获取祈愿统计信息
    /// </summary>
    /// <param name="distributionType">分布类型</param>
    /// <returns>祈愿统计信息Url</returns>
//    fun gachaLogStatisticsDistribution(GachaDistributionType distributionType)
//    {
//        return "${HomaSnapGenshinApi}/GachaLog/Statistics/Distribution/{distributionType}"
//    }

    /// <summary>
    /// 获取注册验证码
    /// </summary>
    const val PassportVerify = "${HomaSnapGenshinApi}/Passport/Verify"

    /// <summary>
    /// 注册账号
    /// </summary>
    const val PassportRegister = "${HomaSnapGenshinApi}/Passport/Register"

    /// <summary>
    /// 重设密码
    /// </summary>
    const val PassportResetPassword = "${HomaSnapGenshinApi}/Passport/ResetPassword"

    /// <summary>
    /// 登录
    /// </summary>
    const val PassportLogin = "${HomaSnapGenshinApi}/Passport/Login"

    /// <summary>
    /// 用户信息
    /// </summary>
    const val PassportUserInfo = "${HomaSnapGenshinApi}/Passport/UserInfo"

    /// <summary>
    /// 上传日志
    /// </summary>
    const val HutaoLogUpload = "${HomaSnapGenshinApi}/HutaoLog/Upload"

    /// <summary>
    /// 检查 uid 是否上传记录
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>路径</returns>
    fun recordCheck(uid: String) = "${HomaSnapGenshinApi}/Record/Check?uid=${uid}"

    /// <summary>
    /// uid 排行
    /// </summary>
    /// <param name="uid">uid</param>
    /// <returns>路径</returns>
    fun recordRank(uid: String) = "${HomaSnapGenshinApi}/Record/Rank?uid=\${uid}"

    /// <summary>
    /// 上传记录
    /// </summary>
    const val RecordUpload = "${HomaSnapGenshinApi}/Record/Upload"

    /// <summary>
    /// 统计信息
    /// </summary>
    const val StatisticsOverview = "${HomaSnapGenshinApi}/Statistics/Overview"

    /// <summary>
    /// 出场率
    /// </summary>
    const val StatisticsAvatarAttendanceRate =
        "${HomaSnapGenshinApi}/Statistics/Avatar/AttendanceRate"

    /// <summary>
    /// 使用率
    /// </summary>
    const val StatisticsAvatarUtilizationRate =
        "${HomaSnapGenshinApi}/Statistics/Avatar/UtilizationRate"

    /// <summary>
    /// 角色搭配
    /// </summary>
    const val StatisticsAvatarAvatarCollocation =
        "${HomaSnapGenshinApi}/Statistics/Avatar/AvatarCollocation"

    /// <summary>
    /// 角色持有率
    /// </summary>
    const val StatisticsAvatarHoldingRate = "${HomaSnapGenshinApi}/Statistics/Avatar/HoldingRate"

    /// <summary>
    /// 武器搭配
    /// </summary>
    const val StatisticsWeaponWeaponCollocation =
        "${HomaSnapGenshinApi}/Statistics/Weapon/WeaponCollocation"

    /// <summary>
    /// 持有率
    /// </summary>
    const val StatisticsTeamCombination = "${HomaSnapGenshinApi}/Statistics/Team/Combination"

    /// <summary>
    /// 胡桃元数据文件
    /// </summary>
    /// <param name="locale">语言</param>
    /// <param name="fileName">文件名称</param>
    /// <returns>路径</returns>
    fun hutaoMetadataFile(locale: String, fileName: String) =
        "${HutaoMetadataSnapGenshinApi}/${locale}/${fileName}"

    /// <summary>
    /// 胡桃元数据2文件
    /// </summary>
    /// <param name="locale">语言</param>
    /// <param name="fileName">文件名称</param>
    /// <returns>路径</returns>
    fun hutaoMetadata2File(locale: String, fileName: String) =
        "${HutaoMetadata2SnapGenshinApi}/Genshin/${locale}/${fileName}"

    /// <summary>
/// Github 元数据文件
/// </summary>
/// <param name="locale">语言</param>
/// <param name="fileName">文件名称</param>
/// <returns>路径</returns>
    fun rawGithubUserContentMetadataFile(locale: String, fileName: String) =
        "${RawGithubUserContentSnapMetadataApi}/Genshin/${locale}/${fileName}"

    /// <summary>
/// UI_Icon_None
/// </summary>
    val UIIconNone by lazy {
        staticFile("Bg", "UI_Icon_None.png")
    }

    /// <summary>
/// UI_ItemIcon_None
/// </summary>
    val UIItemIconNone by lazy {
        staticFile("Bg", "UI_ItemIcon_None.png")
    }

    /// <summary>
/// UI_AvatarIcon_Side_None
/// </summary>
    val UIAvatarIconSideNone by lazy {
        staticFile("AvatarIcon", "UI_AvatarIcon_Side_None.png")
    }

    /// <summary>
/// 图片资源
/// </summary>
/// <param name="category">分类</param>
/// <param name="fileName">文件名称 包括后缀</param>
/// <returns>路径</returns>
    fun staticFile(category: String, fileName: String) =
        "${StaticSnapGenshinApi}/${category}/${fileName}"

    /// <summary>
/// 压缩包资源
/// </summary>
/// <param name="fileName">文件名称 不包括后缀</param>
/// <returns>路径</returns>
    fun staticZip(fileName: String) = "${StaticZipSnapGenshinApi}/${fileName}.zip"
}