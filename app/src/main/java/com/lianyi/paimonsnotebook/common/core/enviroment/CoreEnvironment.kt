package com.lianyi.paimonsnotebook.common.core.enviroment

import android.os.Build
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api.PublicDataApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

/*
* 核心环境参数
* */
object CoreEnvironment {
    private val publicDataApiClient by lazy {
        PublicDataApiClient()
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                dataStoreValues {
                    DeviceId = it[PreferenceKeys.DeviceId] ?: ""

                    if (DeviceId.isBlank()) {
                        launch {
                            generateDeviceId()
                        }
                    }
                }
            }
            launch {
                publicDataApiClient.getExtList()
                val result = publicDataApiClient.getFp()
                DeviceFp = if (result.success) {
                    result.data.device_fp
                } else {
                    "${(1000000000..9999999999).random()}"
                }
                PreferenceKeys.DeviceFp.editValue(DeviceFp)
            }
        }
    }

    //原神游戏id
    const val GameBizGenshin = "hk4e_cn"

    // 米游社 Rpc 版本
    const val XrpcVersion = "2.59.1"

    val ClientType = EnvironmentClientType.BBS

    // 米游社请求UA
    const val HoyolabUA = "Mozilla/5.0 (Windows NT 10.0 Win64 x64) miHoYoBBS/$XrpcVersion"

    // 米游社移动端请求UA
    val HoyolabMobileUA =
        "Mozilla/5.0 (Linux Android ${Build.VERSION.RELEASE}) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/106.0.5249.126 Mobile Safari/537.36 miHoYoBBS/$XrpcVersion"

    //米游社移动端网页UA
    const val HoyolabMobileWebUA =
        "Mozilla/5.0 (Linux; Android 12) Mobile miHoYoBBS/$XrpcVersion"

    const val PaimonsNotebookUA = "PaimonsNotebook/${PaimonsNotebookApplication.version}"

    var DeviceFp = ""
        private set

    var DeviceId = ""
        private set

    //生成设备id
    private suspend fun generateDeviceId() {
        PreferenceKeys.DeviceId.editValue(UUID.randomUUID().toString())
    }

}