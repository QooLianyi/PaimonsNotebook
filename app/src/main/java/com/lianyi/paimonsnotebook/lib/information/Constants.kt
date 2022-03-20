package com.lianyi.paimonsnotebook.lib.information

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.util.PaiMonsNoteBook

//常量
class Constants {
    companion object{
        //每日签到用的SALT
        const val SIGN_SALT = "4a8knnbk5pbjqsrudp3dq484m9axoc5g"
        const val SIGN_APP_VERSION = "2.10.1"
        //一般请求用的DS
        const val API_SALT = "xV8v4Qu54lUKrEYFZkJhB8cuOh9Asafs"
        const val APP_VERSION = "2.14.1"

        //数据缓存
        const val SP_NAME = "cache_info"
        //用户信息缓存
        const val USP_NAME = "user_info"
        //游戏角色信息缓存
        const val CSP_NAME = "character_info"
        //游戏武器信息缓存
        const val WSP_NAME ="weapon_info"

        //账号COOKIE名称
        const val STOKEN_NAME = ""
        const val ITOKEN_NAME = ""
        const val LOGIN_TICKET_NAME = "login_ticket"
        const val LOGIN_UID_NAME = "login_uid"
        const val ACCOUNT_ID_NAME = "account_id"
        const val LTUID_NAME = "ltuid"
        const val LTOKEN_NAME = "ltoken"
        const val COOKIE_TOKEN_NAME = "cookie_token"

        //每日委托状态
        const val DAILY_TASK_STATE_NOT_FINISHED = "「每日委托」奖励还未领取"
        const val DAILY_TASK_STATE_FINISHED = "「每日委托」奖励已领取"

        const val SP_NEED_UPDATE = "needUpdate"

        //替换占位符
        const val REPLACE_PLACEHOLDER = "#(*PLACEHOLDER!!)"

        const val UIGF_EXPORT_APP_NAME = "PaimonsNotebook"

        const val PAIMONSNOTEBOOK_GITHUB_URL = "https://github.com/QooLianyi/PaimonsNotebook"

        //UIGF 的页面
        const val UIGF_URL = "https://github.com/DGP-Studio/Snap.Genshin/wiki/StandardFormat"

        //角色和武器的更新URL
        const val PAIMONS_NOTEBOOK_WEB = "https://qoolianyi.github.io/PaimonsNotebook.github.io/"

        const val PAIMONS_NOTE_BOOK_LATEST = "https://api.github.com/repos/QooLianyi/PaimonsNotebook/releases/latest"

        const val ENA = "p.isEnable"
        const val HTML_SELECTOR_PAIMONS_NOTE_BOOK_NOTICE = "p.notice"
        const val HTML_SELECTOR_PAIMONS_NOTE_BOOK_APP_LASTEST_VERSION_CODE = "p.app_lastest_version_code"

        //旅行者札记饼图颜色
        val monthLegendColors = listOf(
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.abyss_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.daily_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.event_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.mail_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.adventure_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.task_fill),
            ContextCompat.getColor(PaiMonsNoteBook.context, R.color.other_fill)
        )

        val GACHA_HISTORY_5_STAR_CHARACTER_COLOR =  Color.parseColor("#F3A75B")
        val GACHA_HISTORY_5_STAR_WEAPON_COLOR =  Color.parseColor("#D56C6C")
        val GACHA_HISTORY_4_STAR_CHARACTER_COLOR =  Color.parseColor("#A962FA")
        val GACHA_HISTORY_4_STAR_WEAPON_COLOR =  Color.parseColor("#665CF1")

        val gachaHistoryColors = listOf(
            Color.parseColor("#F3A75B"),
            Color.parseColor("#A962FA"),
            Color.parseColor("#665CF1"),
            Color.parseColor("#496CEE")
        )

        val gachaColorList = listOf("#F3A75B",
            "#A962FA",
            "#665CF1",
            "#496CEE",
            "#FD79A8",
            "#D63031",
            "#FDCB6E",
            "#E17055",
            "#00B894",
            "#00CEC9",
            "#0984E3",
            "#6C5CE7",
            "#A29BFE",
            "#55EFC4",
            "#4CD137",
            "#F368E0",
            "#FF9FF3",
            "#FD7272",
            "#7D5FFF",
            "#FFF200",
            "#FFAF40",
            "#EE5A24",
            "#A3CB38",
            "#FDA7DF",
            "#ED4C67",
            "#B53471",
            "#30336B",
            "#006266",
            "#9AECDB",
            "#CC8E35",
            "#CCAE62",
            "#B33939",
            "#CD6133",
            "#82589F"
        )

        fun getMonthLegendColors(list: List<Int>):List<Int>{
            val colors = mutableListOf<Int>()
            list.forEach {
                colors += when(it){
                    1->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.adventure_fill)
                    }
                    2->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.task_fill)
                    }
                    3->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.daily_fill)
                    }
                    4->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.abyss_fill)
                    }
                    5->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.mail_fill)
                    }
                    6->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.event_fill)
                    }
                    else->{
                        ContextCompat.getColor(PaiMonsNoteBook.context, R.color.other_fill)
                    }
                }
            }
            return colors
        }

        fun getNoticeStatus(noticeId:String):String = "have_seen_${noticeId}"
    }
}