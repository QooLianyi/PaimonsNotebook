package com.lianyi.paimonsnotebook.ui.screen.splash.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirstLambda
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    var showLoading by mutableStateOf(false)
        private set

    var currentMetadataLoadCount by mutableIntStateOf(0)
        private set
    var maxMetadataCount by mutableIntStateOf(0)
        private set

    var showEnableMetadataHint by mutableStateOf(false)
        private set

    var enableMetadata = false

    var initialMetadataDownload = false


    fun initParam(callback: () -> Unit) {
        viewModelScope.launchIO {
            dataStoreValuesFirst {
                enableMetadata = it[PreferenceKeys.EnableMetadata] ?: true
                showEnableMetadataHint = it[PreferenceKeys.OnLaunchShowEnableMetadataHint] ?: true
                initialMetadataDownload = it[PreferenceKeys.InitialMetadataDownload] ?: false
            }

            println("enableMetadata = ${enableMetadata}")
            println("showEnableMetadataHint = ${showEnableMetadataHint}")
            println("initialMetadataDownload = ${initialMetadataDownload}")

            callback.invoke()
        }
    }

    fun metadataDownloadInit(onSuccess: () -> Unit) {
        /*
        * 此处的逻辑是判断元数据是否进行了初始化下载，若已完成，直接进入程序
        * 进入主页后再判断是否需要更新
        * 完整性验证在使用对应元数据的功能进行
        * */
        viewModelScope.launch(Dispatchers.IO) {
            //启用元数据
            PreferenceKeys.EnableMetadata.editValue(true)

            val initialMetadataDownload = dataStoreValuesFirstLambda {
                this[PreferenceKeys.InitialMetadataDownload] ?: false
            }

            if (initialMetadataDownload) {
                onSuccess.invoke()
                return@launch
            }

            showLoading = true

            MetadataHelper.updateMetadata(updateMap = true,
                onFailed = {
                    "下载元数据时出现了错误".errorNotify()
                }, onSuccess = {
                    "元数据下载完毕".notify()

                    //下载成功后更新标识
                    PreferenceKeys.MetadataUpdateTime.editValue(System.currentTimeMillis())
                    PreferenceKeys.InitialMetadataDownload.editValue(true)

                    onSuccess.invoke()
                }, onLoadMetadataFile = {
                    currentMetadataLoadCount++
                    maxMetadataCount = it
                }) {
                showLoading = false
            }
        }
    }

    fun onSkipMetadataDownload(callback: () -> Unit) {
        viewModelScope.launchIO {
            PreferenceKeys.EnableMetadata.editValue(false)

            callback.invoke()
        }
    }

    fun disabledOnLaunchShowMetadataHint(onComplete: () -> Unit) {
        viewModelScope.launchIO {
            PreferenceKeys.OnLaunchShowEnableMetadataHint.editValue(false)

            onComplete.invoke()
        }
    }
}