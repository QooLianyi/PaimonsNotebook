package com.lianyi.paimonsnotebook.ui.screen.setting.util

import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.ui.screen.setting.data.ConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.setting.util.configuration_enum.HomeScreenDisplayState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object SettingsHelper {
    private val ConfigurationData = MutableStateFlow(ConfigurationData())
    val configurationFlow = ConfigurationData.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreValues { preferences ->
                ConfigurationData.value =
                    ConfigurationData().apply {
                        homeScreenDisplayState =
                            HomeScreenDisplayState.valueOf(
                                preferences[PreferenceKeys.HomeScreenDisplayState]
                                    ?: HomeScreenDisplayState.Community.name
                            )
                        enableHomeModalSelectClose =
                            preferences[PreferenceKeys.EnableHomeModalSelectClose] ?: false
                        enableOverlay = preferences[PreferenceKeys.EnableOverlay] ?: false
                        appwidgetAlwaysUseSelectedUser =
                            preferences[PreferenceKeys.AppwidgetAlwaysUseSelectedUser] ?: false
                        fullScreenMode =
                            preferences[PreferenceKeys.FullScreenMode] ?: false
                    }
            }
        }

    }
}