package com.lianyi.paimonsnotebook.config


/*
* 此类存放各类APP配置信息
*
* */

class AppConfig {
    companion object{
        //屏幕适配
        const val AUTO_SIZE = 400f

        //缓存页面上限
        const val OFF_SCREEN_PAGE_LIMIT = 9

        //APP初始化所需步骤次数 便笺 banner数据更新 blackBoard数据更新 json数据完整性检查
        const val APP_INIT_COUNT = 1

        //点击间隔多少毫秒内退出app
        const val PRESS_BACK_INTERVAL = 1000L

        //menu展开和收起的尺寸
        const val MENU_START_WIDTH = 125
        const val MENU_END_WIDTH = 40

        //账号最大数量
        const val MAX_ACCOUNT_NUM = 20

        //抽卡记录每页导出间隔
        const val LOAD_GACHA_HISTORY_INTERVAL = 800L


        //侧边栏设置
        const val SP_SIDE_BAR_BUTTON_SETTINGS = "side_bar_button_settings"
        //内容边距设置
        const val SP_CONTENT_MARGIN_SETTINGS = "content_margin_settings"
        //祈愿排序设置
        const val SP_WISH_SORT_BY_DESCENDING = "wish_sort_by_descending"

        //首页banner点击跳转
        const val SP_HOME_BANNER_JUMP_TO_ARTICLE = "home_banner_jump_to_article"
        //首页近期活动点击跳转
        const val SP_HOME_NEAR_ACTIVITY_JUMP_TO_ARTICLE = "home_near_activity_jump_to_article"
        //首页公告点击跳转
        const val SP_HOME_NOTICE_JUMP_TO_ARTICLE = "home_notice_jump_to_article"
        //点击侧边栏内容跳转后收起侧边栏
        const val SP_HOME_SIDE_BAR_AUTO_PICKUP = "home_side_bar_auto_pickup"
        //点击游戏公告按钮跳转公告列表
        const val SP_HOME_ANNOUNCEMENT_JUMP_TO_LIST = "home_announcement_jump_to_list"
    }
}