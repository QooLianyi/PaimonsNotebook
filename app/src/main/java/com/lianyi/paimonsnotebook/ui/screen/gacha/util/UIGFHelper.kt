package com.lianyi.paimonsnotebook.ui.screen.gacha.util

object UIGFHelper {

    const val UIGF_VERSION = "v2.3"

    private const val Novice_WISH = "100"
    private const val PERMANENT_WISH = "200"
    private const val AVATAR_WISH_1 = "301"
    private const val AVATAR_WISH_2 = "400"
    private const val WEAPON_WISH = "302"

    val gachaList = arrayOf(
        Novice_WISH,
        PERMANENT_WISH,
        AVATAR_WISH_1,
        AVATAR_WISH_2,
        WEAPON_WISH
    )

    val uigfGachaTypeCount: Int
        get() = gachaList.size

    fun getUIGFType(type: String) = when (type) {
        AVATAR_WISH_1, AVATAR_WISH_2 -> AVATAR_WISH_1
        else -> type
    }

    fun getUIGFName(type: String) = when (type) {
        Novice_WISH -> "新手祈愿"
        AVATAR_WISH_1, AVATAR_WISH_2 -> "角色活动"
        WEAPON_WISH -> "神铸赋形"
        PERMANENT_WISH -> "奔行世间"
        else -> ""
    }

    object ItemType{
        const val Weapon = "武器"
        const val Avatar = "角色"
    }

    //UIGF 字段
    object Field {
        object Info{
            const val Uid = "uid"
            const val Lang = "lang"
            const val ExportTimestamp = "export_timestamp"
            const val ExportApp = "export_app"
            const val ExportAppVersion = "export_app_version"
            const val UIGFVersion = "uigf_version"

            val fields = arrayOf(
                Uid,
                Lang,
                ExportTimestamp,
                ExportApp,
                ExportAppVersion,
                UIGFVersion
            )

        }
        object Item{
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