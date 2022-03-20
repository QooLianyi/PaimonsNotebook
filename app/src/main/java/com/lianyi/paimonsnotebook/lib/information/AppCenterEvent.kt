package com.lianyi.paimonsnotebook.lib.information

class AppCenterEvent {
    companion object{
        const val EVENT_MENU_ITEM_SELECT = "menu_item_select"
        const val EVENT_OPEN_ACTIVITY = "open_activity"
        const val EVENT_HOME_PAGE_ACTION = "home_page_action"

        //侧边栏点击事件
        const val MENU_ITEM_SELECT_EVENT_CULTIVATE_CALCULATE = "cultivate_calculate"
        const val MENU_ITEM_SELECT_EVENT_WISH = "wish"
        const val MENU_ITEM_SELECT_EVENT_MAP = "map"
        const val MENU_ITEM_SELECT_EVENT_DAILY_MATERIALS = "daily_materials"
        const val MENU_ITEM_SELECT_EVENT_WEEKLY_MATERIALS = "weekly_materials"
        const val MENU_ITEM_SELECT_EVENT_CHARACTER = "character"
        const val MENU_ITEM_SELECT_EVENT_WEAPON = "weapon"
        const val MENU_ITEM_SELECT_EVENT_SEARCH = "search"
        const val MENU_ITEM_SELECT_EVENT_HUTAO_DATABASE = "hutao_database"
        const val MENU_ITEM_SELECT_EVENT_OPTIONS = "options"
        const val MENU_ITEM_SELECT_EVENT_SUMMER_LAND = "land"

        //主页点击事件
        const val HOME_PAGE_ACTION_DAILY_SIGN = "daily_sign"
        const val HOME_PAGE_ACTION_ACCOUNT_MANAGER = "account_manager"
        const val HOME_PAGE_ACTION_ME_INFO = "me_info"
        const val HOME_PAGE_ACTION_MONTH_MONTH_LEDGER = "month_ledger"

        //主页banner文章游戏公告点击事件
        const val HOME_PAGE_ACTION_OPEN_BANNER = "banner"
        const val HOME_PAGE_ACTION_OPEN_NEAR_ACTIVITY = "near_activity"
        const val HOME_PAGE_ACTION_NOTICE = "notice"
        const val HOME_PAGE_ACTION_ANNOUNCEMENT = "announcement"

        const val EMPTY = "empty"
    }
}