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
    var enableOverlay by mutableStateOf(EnableOverlayDefault)

    //总是使用默认用户
    var alwaysUseDefaultUser by mutableStateOf(AlwaysUseDefaultUserDefault)

    //启用自动删除过期图片
    var enableAutoCleanExpiredImages by mutableStateOf(EnableAutoCleanExpiredImagesDefault)

    //启用检查新版本
    var enableCheckNewVersion by mutableStateOf(EnableCheckNewVersionDefault)

    //启用元数据
    var enableMetadata by mutableStateOf(EnableMetadataDefault)

    companion object {
        val homeScreenDisplayStateDefault = HomeScreenDisplayState.Community
        const val EnableOverlayDefault = false
        const val AlwaysUseDefaultUserDefault = true
        const val EnableAutoCleanExpiredImagesDefault = true
        const val EnableCheckNewVersionDefault = true
        const val EnableMetadataDefault = true

        //重置选项
        suspend fun resetConfig() {
            PaimonsNotebookApplication.context.datastorePf.edit {
                it[PreferenceKeys.HomeScreenDisplayState] = homeScreenDisplayStateDefault.name
                it[PreferenceKeys.EnableOverlay] = EnableOverlayDefault
                it[PreferenceKeys.AlwaysUseDefaultUser] = AlwaysUseDefaultUserDefault
                it[PreferenceKeys.EnableAutoCleanExpiredImages] =
                    EnableAutoCleanExpiredImagesDefault
                it[PreferenceKeys.EnableCheckNewVersion] = EnableCheckNewVersionDefault
                it[PreferenceKeys.EnableMetadata] = EnableMetadataDefault
            }
        }
    }
}
