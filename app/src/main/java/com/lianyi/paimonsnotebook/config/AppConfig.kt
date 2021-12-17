package com.lianyi.paimonsnotebook.config

import com.lianyi.paimonsnotebook.util.dp

class AppConfig {
    companion object{
        //屏幕适配
        const val AUTO_SIZE = 400f

        //缓存页面上限
        const val OFF_SCREEN_PAGE_LIMIT = 8

        //APP初始化所需步骤次数 便笺 banner数据更新 blackBoard数据更新 json数据完整性检查
        const val APP_INIT_COUNT = 4

        //点击间隔多少毫秒内退出app
        const val PRESS_BACK_INTERVAL = 1000L


        //menu展开和收起的尺寸
        const val MENU_START_WIDTH = 115
        const val MENU_END_WIDTH = 40

    }

}