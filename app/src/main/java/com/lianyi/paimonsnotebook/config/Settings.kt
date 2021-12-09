package com.lianyi.paimonsnotebook.config

import com.lianyi.paimonsnotebook.R
import java.text.SimpleDateFormat

//一些常量
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

    }
}

class ResponseCode{
    companion object{
        const val OK = "0"
    }
}
//本地JSON数据名称
class JsonCacheName{
    companion object{
        //banner缓存
        const val BANNER_CACHE = "banner_cache"
        //blackBoard缓存
        const val BLACK_BOARD = "black_board"

        //角色entity缓存
        const val CHARACTER_ENTITY_LIST = "character_entity_list"
        //武器entity缓存
        const val WEAPON_ENTITY_LIST = "weapon_entity_list"

        //角色列表缓存
        const val CHARACTER_LIST = "character_list"
        //武器列表缓存
        const val WEAPON_LIST = "weapon_list"
    }
}
//时间格式
class Format{
    companion object{
        val TIME = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val TIME_WEEK = SimpleDateFormat("E")
        val TIME_DAY = SimpleDateFormat("d")

        fun getWeekByName(name:String):Int{
            return when(name){
                "周一"->0
                "周二"->1
                "周三"->2
                "周四"->3
                "周五"->4
                "周六"->5
                else-> 6
            }
        }
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

class WeaponType{
    companion object{
        const val ONE_HAND_SWORD = 2001
        const val BOTH_HAND_SWORD = 2002
        const val BOW_AND_ARROW = 2003
        const val MAGIC_ARTS = 2004
        const val SPEAR = 2005

        fun getNameByType(type:Int):String{
            return when(type){
                ONE_HAND_SWORD->"单手剑"
                BOTH_HAND_SWORD->"双手剑"
                BOW_AND_ARROW->"弓"
                MAGIC_ARTS->"法器"
                SPEAR->"长柄武器"
                else->"*ERROR*"
            }
        }

        fun getTypeByName(name:String):Int{
            return when(name){
                "单手剑"->ONE_HAND_SWORD
                "双手剑"-> BOTH_HAND_SWORD
                "弓"->BOW_AND_ARROW
                "法器"->MAGIC_ARTS
                "长柄武器"->SPEAR
                else->-100
            }
        }

        fun getResourceByType(type:Int):Int{
            return when(type){
                ONE_HAND_SWORD-> R.drawable.icon_one_hand_sword
                BOTH_HAND_SWORD->R.drawable.icon_both_hand_sword
                BOW_AND_ARROW->R.drawable.icon_bow_and_arrow
                MAGIC_ARTS->R.drawable.icon_magic_arts
                SPEAR->R.drawable.icon_spear
                else ->0
            }
        }

    }
}

class CharacterProperty{
    companion object{
        const val FIRE = 1001
        const val WATER = 1002
        const val ELECT = 1003
        const val GRASS = 1004
        const val ICE = 1005
        const val ROCK = 1006
        const val WIND = 1007

        fun getTypeByName(name: String):Int{
            return when(name){
                "火"-> FIRE
                "水"-> WATER
                "雷"-> ELECT
                "草"-> GRASS
                "冰"-> ICE
                "岩"->ROCK
                "风"-> WIND
                else-> -100
            }
        }

        fun getNameByType(type: Int):String{
            return when(type){
                FIRE-> "火"
                WATER-> "水"
                ELECT-> "雷"
                GRASS-> "草"
                ICE-> "冰"
                ROCK-> "岩"
                WIND-> "风"
                else-> "*ERROR*"
            }
        }

        fun getImageResourceByType(type: Int):Int{
            return when(type){
                FIRE-> R.drawable.icon_element_fire
                WATER->R.drawable.icon_element_water
                ELECT->R.drawable.icon_element_elect
                GRASS->R.drawable.icon_element_grass
                ICE->  R.drawable.icon_element_ice
                ROCK-> R.drawable.icon_element_rock
                WIND-> R.drawable.icon_element_wind
                else-> 0
            }
        }

    }
}
class Area{
    companion object{
        const val MOND = ""
        const val LIYUE = ""
        const val IMUZUMA = ""
    }
}


