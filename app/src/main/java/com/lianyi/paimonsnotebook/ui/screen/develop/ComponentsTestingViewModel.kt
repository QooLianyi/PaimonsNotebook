package com.lianyi.paimonsnotebook.ui.screen.develop

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import com.geetest.sdk.GT3ConfigBean
import com.geetest.sdk.GT3ErrorBean
import com.geetest.sdk.GT3GeetestUtils
import com.geetest.sdk.GT3Listener
import com.lianyi.paimonsnotebook.common.data.sort.FilterSortData
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.geetest.GeeTestClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

@OptIn(ExperimentalCoilApi::class)
class ComponentsTestingViewModel : ViewModel() {
    var deleteUnfold by mutableStateOf(false)

    var sliderValue by mutableStateOf(0f)

    var tempText by mutableStateOf("")

    var loadUrl by mutableStateOf("")

    var currentIndex by mutableStateOf(0)

    val sliderValueAnim = Animatable(0f, Float.VectorConverter)

    var addChecked by mutableStateOf(false)

    var diskCacheImages = mutableStateListOf<Pair<DiskCache, File?>>()

    var currentImage by mutableStateOf<File?>(null)

    private val imageLoader = PaimonsNotebookImageLoader.current

    var blurProgressSuccess = false

    val filterList = listOf(
        FilterSortData("默认排序", 0f, mutableStateOf(SortOrderBy.Ascend)),
    )

    init {
        viewModelScope.launch {
            PaimonsNotebookDatabase.database.diskCacheDao.getAllData().collect {
                diskCacheImages.clear()
                diskCacheImages.addAll(it.map { it to imageLoader.diskCache?.get(it.url)?.data?.toFile() })
            }
        }
    }

    fun setSlider(value: Float, scope: CoroutineScope) {
        scope.launch {
            sliderValueAnim.animateTo(value, animationSpec = tween(200))
        }
    }

    fun onBackPressed(onFinish: () -> Unit) {
        onFinish()
    }

    val geeTestClient by lazy {
        GeeTestClient()
    }


    fun startGeeTest(context: Context) {
        val gT3GeetestUtils = GT3GeetestUtils(context)
        val gT3ConfigBean = GT3ConfigBean().apply {
            pattern = 1
            isCanceledOnTouchOutside = false
            lang = "zh-cn"
            timeout = 10000
            webviewTimeout = 10000
        }

        gT3ConfigBean.listener = object : GT3Listener() {

            override fun onDialogReady(duration: String?) {
                println("duration = ${duration}")
                super.onDialogReady(duration)
            }

            override fun onDialogResult(json: String?) {
                println("onDialogResult = ${json}")
                val user = AccountHelper.selectedUserFlow.value

                if (user != null) {
                    viewModelScope.launch {
                        val result = geeTestClient.verifyVerification(user.userEntity, JSONObject(json?:"{}"))
                        println("result = ${result}")
                    }
                } else {
                    gT3GeetestUtils.destory()
                }
                super.onDialogResult(json)
            }

            override fun onReceiveCaptchaCode(p0: Int) {
                println("onReceiveCaptchaCode = ${p0}")
            }

            override fun onStatistics(p0: String?) {
                println("onStatistics = ${p0}")
            }

            override fun onClosed(p0: Int) {
                println("onClosed = ${p0}")
            }

            override fun onSuccess(p0: String?) {
                println("onSuccess = ${p0}")
            }

            override fun onFailed(p0: GT3ErrorBean?) {
                println("onFailed = ${p0}")
            }

            override fun onButtonClick() {
                val user = AccountHelper.selectedUserFlow.value
                if (user != null) {
                    viewModelScope.launch {
                        val result = geeTestClient.createVerification(user.userEntity)

                        println(result)
                        if(result.success){
                            gT3ConfigBean.api1Json = JSONObject(JSON.stringify(result.data))
                            gT3GeetestUtils.getGeetest()
                        }else{
                            gT3GeetestUtils.destory()
                        }
                    }
                } else {
                    gT3GeetestUtils.destory()
                }
            }
        }

        gT3GeetestUtils.init(gT3ConfigBean)

        gT3GeetestUtils.startCustomFlow()

    }

}