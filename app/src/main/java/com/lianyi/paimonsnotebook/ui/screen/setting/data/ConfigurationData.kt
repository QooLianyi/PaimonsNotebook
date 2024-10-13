package com.lianyi.paimonsnotebook.ui.screen.setting.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.ui.screen.setting.util.enums.HomeScreenDisplayState

/*
* 配置信息
* */
class ConfigurationData {
    //首页显示状态
    var homeScreenDisplayState by mutableStateOf(homeScreenDisplayStateDefault)

    //启用悬浮窗
    var enableOverlay by mutableStateOf(ENABLE_OVERLAY_DEFAULT)

    //总是使用默认用户
    var alwaysUseDefaultUser by mutableStateOf(ALWAYS_USE_DEFAULT_USER_DEFAULT)

    //启用自动删除过期图片
    var enableAutoCleanExpiredImages by mutableStateOf(ENABLE_AUTO_CLEAN_EXPIRED_IMAGES_DEFAULT)

    //启用检查新版本
    var enableCheckNewVersion by mutableStateOf(ENABLE_CHECK_NEW_VERSION_DEFAULT)

    //启用元数据
    var enableMetadata by mutableStateOf(ENABLE_METADATA_DEFAULT)

    companion object {
        val homeScreenDisplayStateDefault = HomeScreenDisplayState.Community
        const val ENABLE_OVERLAY_DEFAULT = false
        const val ALWAYS_USE_DEFAULT_USER_DEFAULT = true
        const val ENABLE_AUTO_CLEAN_EXPIRED_IMAGES_DEFAULT = true
        const val ENABLE_CHECK_NEW_VERSION_DEFAULT = true
        const val ENABLE_METADATA_DEFAULT = true

        //重置选项
        suspend fun resetConfig() {
            PaimonsNotebookApplication.context.datastorePf.edit {
                it[PreferenceKeys.HomeScreenDisplayState] = homeScreenDisplayStateDefault.name
                it[PreferenceKeys.EnableOverlay] = ENABLE_OVERLAY_DEFAULT
                it[PreferenceKeys.AlwaysUseDefaultUser] = ALWAYS_USE_DEFAULT_USER_DEFAULT
                it[PreferenceKeys.EnableAutoCleanExpiredImages] =
                    ENABLE_AUTO_CLEAN_EXPIRED_IMAGES_DEFAULT
                it[PreferenceKeys.EnableCheckNewVersion] = ENABLE_CHECK_NEW_VERSION_DEFAULT
                it[PreferenceKeys.EnableMetadata] = ENABLE_METADATA_DEFAULT
            }
        }
    }
}
