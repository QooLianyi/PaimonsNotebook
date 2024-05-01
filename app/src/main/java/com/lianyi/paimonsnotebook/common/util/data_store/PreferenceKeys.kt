package com.lianyi.paimonsnotebook.common.util.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    //device_fp
    val DeviceFp by lazy {
        stringPreferencesKey("device_fp")
    }

    //device_id
    val DeviceId by lazy {
        stringPreferencesKey("device_id")
    }

    //device_id
    val DeviceId40 by lazy {
        stringPreferencesKey("device_id40")
    }

    //bbs_device_id
    val BBSDeviceId by lazy {
        stringPreferencesKey("bbs_device_id")
    }

    //外部存储权限申请标识
    val PermissionRequestFlag by lazy {
        booleanPreferencesKey("permissionRequestFlag")
    }

    //启用选择主页modal自动收回
    val EnableHomeModalSelectClose by lazy {
        booleanPreferencesKey("enableHomeModalSelectClose")
    }

    val EnableOverlay by lazy {
        booleanPreferencesKey("enableOverlay")
    }

    //主页显示状态
    val HomeScreenDisplayState by lazy {
        stringPreferencesKey("homeScreenDisplayState")
    }

    //桌面组件是否以当前选择的用户为绑定目标
    val AppwidgetAlwaysUseSelectedUser by lazy {
        booleanPreferencesKey("appwidgetAlwaysUseSelectedUser")
    }

    //当选择用户的时候,默认使用选中用户
    val AlwaysUseDefaultUser by lazy {
        booleanPreferencesKey("alwaysUseDefaultUser")
    }

    //当前显示的祈愿记录uid
    val GachaRecordCurrentGameUid by lazy {
        stringPreferencesKey("gachaRecordCurrentGameUid")
    }

    //祈愿记录时区map,存储时区
    val GachaRecordGameUidRegionMap by lazy {
        stringPreferencesKey("gachaRecordGameUidRegionMap")
    }


    //当前祈愿记录最后的uid,用于生成无ID的记录
    val GenerateGachaRecordId by lazy {
        longPreferencesKey("generateGachaRecordId")
    }

    //桌面组件快捷方式组件当前页面
    val AppWidgetShortcutCurrentPage by lazy {
        intPreferencesKey("appWidgetShortcutCurrentPage")
    }

    //最后一次查看的角色的id
    val LastViewAvatarId by lazy {
        intPreferencesKey("lastViewAvatarId")
    }

    val LastViewWeaponId by lazy {
        intPreferencesKey("lastViewWeaponId")
    }

    //第一次进入配置界面
    val FirstEntryAppWidgetConfigurationScreen by lazy {
        booleanPreferencesKey("firstEntryAppWidgetConfigurationScreen")
    }

    //是否同意用户协议
    val AgreeUserAgreement by lazy {
        booleanPreferencesKey("agreeUserAgreement")
    }

    //是否启用自动清除过期图像
    val EnableAutoCleanExpiredImages by lazy {
        booleanPreferencesKey("enableAutoCleanExpiredImages")
    }

    val EnableCheckNewVersion by lazy {
        booleanPreferencesKey("enableCheckNewVersion")
    }

    //全屏模式(隐藏状态栏)
    val FullScreenMode by lazy {
        booleanPreferencesKey("fullScreenMode")
    }

    //桌面组件快捷方式当前页面
    val AppWidgetDailyMaterialCurrentPage by lazy {
        intPreferencesKey("appWidgetDailyMaterialCurrentPage")
    }

    //元数据更新时间
    val MetadataUpdateTime by lazy {
        longPreferencesKey("metadataUpdateTime")
    }

    //staticResources渠道
    val StaticResourcesChannel by lazy {
        stringPreferencesKey("staticResourcesChannel")
    }

    //启用成就更新时刷新列表
    val EnableAchievementChangeUpdateList by lazy {
        booleanPreferencesKey("enableAchievementChangeUpdateList")
    }

    //禁用成就记录交互
    val DisableAchievementInteraction by lazy {
        booleanPreferencesKey("disableAchievementInteraction")
    }

    //配置的快捷方式列表
    val ShortcutsList by lazy {
        stringPreferencesKey("shortcutsList")
    }

    //启用快捷方式列表
    val EnableShortcutsList by lazy {
        booleanPreferencesKey("enableShortcutsList")
    }

    //本地存储的app_key
    val AuthorizeAppSign by lazy {
        stringPreferencesKey("authorizeAppSign")
    }

    val widgetsCount by lazy {
        intPreferencesKey("widgetsCount")
    }
}