package com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf

object UIGFHelper {

    const val UIGF_HOME_PAGE = "https://uigf.org/"

    const val UIGF_VERSION = "v3.0"

    private const val NOVICE_WISH = "100"
    private const val PERMANENT_WISH = "200"
    private const val AVATAR_WISH_1 = "301"
    private const val AVATAR_WISH_2 = "400"
    private const val WEAPON_WISH = "302"
    private const val CHRONICLED_WISH = "500"


    //当更新祈愿卡池类型时,需要在此处同步添加
    val gachaList = arrayOf(
        NOVICE_WISH,
        PERMANENT_WISH,
        AVATAR_WISH_1,
        AVATAR_WISH_2,
        WEAPON_WISH,
        CHRONICLED_WISH
    )

    val uigfGachaTypeCount: Int
        get() = gachaList.size

    fun getUIGFType(type: String) = when (type) {
        AVATAR_WISH_1, AVATAR_WISH_2 -> AVATAR_WISH_1
        else -> type
    }

    //返回UIGF名称,未知时直接返回类型
    fun getUIGFName(type: String) = when (type) {
        NOVICE_WISH -> "新手祈愿"
        AVATAR_WISH_1, AVATAR_WISH_2 -> "角色活动"
        WEAPON_WISH -> "神铸赋形"
        PERMANENT_WISH -> "奔行世间"
        CHRONICLED_WISH -> "集录祈愿"
        else -> type
    }

    //根据uid获取时区
    fun getRegionTimeZoneByUid(uid: String) =
        when (uid.first()) {
            '6' -> -5L //os_usa
            '7' -> 1L //os_euro
            else -> 8L //os_cht, os_asia, cn_gf01, cn_qd01
        }

    object ItemType {
        const val Weapon = "武器"
        const val Avatar = "角色"
    }

    //UIGF 字段
    object Field {
        object Info {
            const val Uid = "uid"
            const val Lang = "lang"
            const val ExportTimestamp = "export_timestamp"
            const val ExportApp = "export_app"
            const val ExportAppVersion = "export_app_version"
            const val UIGFVersion = "uigf_version"
            const val RegionTimeZone = "region_time_zone"

            val fields = arrayOf(
                Uid,
                Lang,
                ExportTimestamp,
                ExportApp,
                ExportAppVersion,
                UIGFVersion,
//                RegionTimeZone //时区不是必须有的
            )
        }

        object Item {
            const val UigfGachaType = "uigf_gacha_type"
            const val GachaType = "gacha_type"
            const val ItemId = "item_id"
            const val Count = "count"
            const val Time = "time"
            const val Name = "name"
            const val ItemType = "item_type"
            const val RankType = "rank_type"
            const val Id = "id"

            val fields = arrayOf(
                UigfGachaType,
                GachaType,
                ItemId,
                Count,
                Time,
                Name,
                ItemType,
                RankType,
                Id
            )
        }
    }

}