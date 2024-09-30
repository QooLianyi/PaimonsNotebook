package com.lianyi.paimonsnotebook.ui.screen.setting.util

import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
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
                val configurationData =
                    ConfigurationData().apply {
                        homeScreenDisplayState =
                            HomeScreenDisplayState.valueOf(
                                preferences[PreferenceKeys.HomeScreenDisplayState]
                                    ?: ConfigurationData.homeScreenDisplayStateDefault.name
                            )
                        enableOverlay = preferences[PreferenceKeys.EnableOverlay]
                            ?: ConfigurationData.EnableOverlayDefault
                        alwaysUseDefaultUser =
                            preferences[PreferenceKeys.AlwaysUseDefaultUser]
                                ?: ConfigurationData.AlwaysUseDefaultUserDefault
                        enableAutoCleanExpiredImages =
                            preferences[PreferenceKeys.EnableAutoCleanExpiredImages]
                                ?: ConfigurationData.EnableAutoCleanExpiredImagesDefault
                        enableCheckNewVersion = preferences[PreferenceKeys.EnableCheckNewVersion]
                            ?: ConfigurationData.EnableCheckNewVersionDefault
                        enableMetadata = preferences[PreferenceKeys.EnableMetadata]
                            ?: ConfigurationData.EnableMetadataDefault
                    }

                _ConfigurationData.emit(configurationData)

                println("updateShowModalItemData = ${configurationData.enableMetadata}")
                HomeHelper.updateShowModalItemData(configurationData.enableMetadata)
            }
        }
    }
}