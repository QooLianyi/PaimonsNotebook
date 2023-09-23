package com.lianyi.paimonsnotebook.ui.screen.splash.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.MetadataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class SplashScreenViewModel:ViewModel() {

    var showLoading by mutableStateOf(false)
        private set

    fun init(onSuccess:()->Unit){
        /*
        * 此处的逻辑是判断元数据是否全部存在,全部存在直接进入主页
        * 进入主页后再判断是否需要更新
        * */
        viewModelScope.launch(Dispatchers.IO) {
            if(MetadataHelper.allMetadataExists()){
                onSuccess.invoke()
            }else{
                showLoading = true

                MetadataHelper.updateMetadata(onFailed = {
                    "下载元数据时出现了错误,五秒后退出程序".errorNotify()
                    delay(5000)
                    exitProcess(0)
                }, onSuccess = {
                    "元数据下载完毕".notify()

                    PreferenceKeys.MetadataUpdateTime.editValue(System.currentTimeMillis())
                    onSuccess.invoke()
                }){
                    showLoading = false
                }
            }
        }
    }

}