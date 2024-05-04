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
    var enableOverlay by mutableStateOf(enableOverlayDefault)

    //总是使用默认用户
    var alwaysUseDefaultUser by mutableStateOf(alwaysUseDefaultUserDefault)

    //启用自动删除过期图片
    var enableAutoCleanExpiredImages by mutableStateOf(enableAutoCleanExpiredImagesDefault)

    //启用检查新版本
    var enableCheckNewVersion by mutableStateOf(enableCheckNewVersionDefault)

    //启用metadata
    var enableMetadata by mutableStateOf(enableMetadataDefault)

    companion object {
        val homeScreenDisplayStateDefault = HomeScreenDisplayState.Community
        const val enableOverlayDefault = false
        const val alwaysUseDefaultUserDefault = true
        const val enableAutoCleanExpiredImagesDefault = true
        const val enableCheckNewVersionDefault = true
        const val enableMetadataDefault = true

        //重置选项
        suspend fun resetConfig() {
            PaimonsNotebookApplication.context.datastorePf.edit {
                it[PreferenceKeys.HomeScreenDisplayState] = homeScreenDisplayStateDefault.name
                it[PreferenceKeys.EnableOverlay] = enableOverlayDefault
                it[PreferenceKeys.AlwaysUseDefaultUser] = alwaysUseDefaultUserDefault
                it[PreferenceKeys.EnableAutoCleanExpiredImages] =
                    enableAutoCleanExpiredImagesDefault
                it[PreferenceKeys.EnableCheckNewVersion] = enableCheckNewVersionDefault
                it[PreferenceKeys.EnableMetadata] = enableMetadataDefault
            }
        }
    }

}
