package com.lianyi.paimonsnotebook.config

import java.text.SimpleDateFormat

class Settings {
    companion object{
        //数据缓存
        const val SP_NAME = "cache_info"
        //用户信息缓存
        const val USP_NAME = "user_info"
        //游戏角色信息缓存
        const val CSP_NAME = "character_info"
        //游戏武器信息缓存
        const val WSP_NAME ="weapon_info"


        //替换占位符
        const val REPLACE_PLACEHOLDER = "#(*PLACEHOLDER!!)"
    }
}
class URL{
    companion object{
        //首页banner
        const val HOME_BANNER = "https://bbs-api.mihoyo.com/post/wapi/getOfficialRecommendedPosts?gids=2"
        //首页活动
        const val BLACK_BOARD = "https://api-static.mihoyo.com/common/blackboard/ys_obc/v1/get_activity_calendar?app_sn=ys_obc"

        //获取详细信息,通过content_id
        const val LOAD_DETAIL_INFO = "https://bbs.mihoyo.com/ys/obc/content//detail?bbs_presentation_style=no_header"
    }
}

fun getItemDetailUrl(contentID:String):String{
    return "https://bbs.mihoyo.com/ys/obc/content/${contentID.trim()}/detail?bbs_presentation_style=no_header"
}

class ResponseCode{
    companion object{
        const val OK = "0"
    }
}

class JsonCacheName{
    companion object{
        const val BANNER_CACHE = "banner_cache"
        const val BLACK_BOARD = "black_board"

        const val CHARACTER_LIST = "character_list"
        const val WEAPON_LIST = "weapon_list"
    }
}

class Format{
    companion object{
        val TIME_DAY = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val TIME_DAY_WEEK = SimpleDateFormat("E")
    }
}

class PagerIndex{
    companion object{
        const val HOME_PAGE = 0
        const val DAILY_MATERIALS_PAGE = 1
        const val WEEK_MATERIALS_PAGE = 2
        const val CHARACTER_PAGE = 3
        const val WEAPON_PAGE = 4
        const val MAP_PAGE = 5
        const val WISH_PAGE = 6
        const val SEARCH_PAGE = 7
        const val TIME_LINE_PAGE = 8
    }
}

//实体类型 角色=100 武器=200
class EntityType{
    companion object{
        const val CHARACTER = 100
        const val WEAPON = 200
    }
}

