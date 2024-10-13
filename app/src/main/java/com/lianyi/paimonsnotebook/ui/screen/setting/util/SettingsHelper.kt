package com.lianyi.paimonsnotebook.ui.screen.setting.util

import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.data.ConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.setting.util.enums.HomeScreenDisplayState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object SettingsHelper {
    private val _ConfigurationData = MutableStateFlow(ConfigurationData())
    val configurationFlow = _ConfigurationData.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreValues { preferences ->
                val configurationData = ConfigurationData().apply {
                    homeScreenDisplayState =
                        HomeScreenDisplayState.valueOf(
                            preferences[PreferenceKeys.HomeScreenDisplayState]
                                ?: ConfigurationData.homeScreenDisplayStateDefault.name
                        )
                    enableOverlay = preferences[PreferenceKeys.EnableOverlay]
                        ?: ConfigurationData.ENABLE_OVERLAY_DEFAULT
                    alwaysUseDefaultUser =
                        preferences[PreferenceKeys.AlwaysUseDefaultUser]
                            ?: ConfigurationData.ALWAYS_USE_DEFAULT_USER_DEFAULT
                    enableAutoCleanExpiredImages =
                        preferences[PreferenceKeys.EnableAutoCleanExpiredImages]
                            ?: ConfigurationData.ENABLE_AUTO_CLEAN_EXPIRED_IMAGES_DEFAULT
                    enableCheckNewVersion = preferences[PreferenceKeys.EnableCheckNewVersion]
                        ?: ConfigurationData.ENABLE_CHECK_NEW_VERSION_DEFAULT
                    enableMetadata = preferences[PreferenceKeys.EnableMetadata]
                        ?: ConfigurationData.ENABLE_METADATA_DEFAULT
                }

                _ConfigurationData.emit(configurationData)

                val customDrawerListJson =
                    preferences[PreferenceKeys.CustomHomeDrawerList] ?: JSON.EMPTY_LIST

                val enableCustomDrawer = preferences[PreferenceKeys.EnableCustomHomeDrawer] ?: false

                HomeHelper.updateShowModalItemData(
                    configurationData.enableMetadata,
                    customDrawerListJson,
                    enableCustomDrawer
                )
            }
        }
    }
}