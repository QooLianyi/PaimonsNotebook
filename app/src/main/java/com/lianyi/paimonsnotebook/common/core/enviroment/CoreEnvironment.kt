package com.lianyi.paimonsnotebook.common.core.enviroment

import android.os.Build
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.AppSignInfoData
import com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api.PublicDataApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.UUID

/*
* 核心环境参数
* */
object CoreEnvironment {
    private val publicDataApiClient by lazy {
        PublicDataApiClient()
    }

    var skipSplashScreen = false

    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
//            launch {
//                setAppSignInfo()
//            }
            launch {
                dataStoreValuesFirst {
//                    return@dataStoreValuesFirst

                    DeviceId = it[PreferenceKeys.DeviceId] ?: ""
                    BBSDeviceId = it[PreferenceKeys.BBSDeviceId] ?: ""
                    DeviceId40 = it[PreferenceKeys.DeviceId40] ?: ""

                    DeviceIdSeed = it[PreferenceKeys.DeviceIdSeed] ?: ""
                    DeviceIdSeedTime = it[PreferenceKeys.DeviceIdSeedTime] ?: -1L

                    skipSplashScreen = !(it[PreferenceKeys.EnableMetadata]
                        ?: true) || (it[PreferenceKeys.InitialMetadataDownload] ?: false)

                    if (DeviceId.isBlank()) {
                        generateDeviceId()
                    }

                    if (BBSDeviceId.isBlank()) {
                        generateBBSDeviceId()
                    }

                    //生成40位的deviceId，通过当前的deviceId
                    if (DeviceId40.isBlank()) {
                        generateDeviceId40()
                    }

                    if (DeviceIdSeed.isBlank()) {
                        DeviceIdSeed = UUID.randomUUID().toString()
                        PreferenceKeys.DeviceIdSeed.editValue(DeviceIdSeed)
                        PreferenceKeys.DeviceIdSeedTime.editValue(System.currentTimeMillis())
                    }

                    DeviceFp = it[PreferenceKeys.DeviceFp] ?: ""
                    setFp(DeviceFp)
                }
            }
        }
    }

    //authorize_key 此处使用的是云·星铁的
    const val AuthorizeKeyStarRailCould = "e45a5ea9b62b87aa"

    //星铁的authorize_key
    const val AuthorizeKeyStarRail = "c90mr1bwo2rk"

    //app_key 此处使用的是云·星铁 2.1的app_sign
    var AuthorizeAppSign: String = "9ddde935852443ac9ecc5e794f9917f0"
        private set

    //签名版本(程序版本)
    var AuthorizeAppSignVersion = "2.1.0"
        private set

    const val SDKVersion = "2.22.1"

    //原神游戏id
    const val GameBizGenshin = "hk4e_cn"

    // 米游社 Rpc 版本
    const val XrpcVersion = "2.75.2"

    const val ClientType = EnvironmentClientType.BBS

    // 米游社移动端请求UA
    val HoyolabMobileUA =
        "Mozilla/5.0 (Linux; Android ${Build.VERSION.RELEASE}; ${Build.MODEL} Build/${Build.USER}; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/95.0.4638.74 Mobile Safari/537.36 miHoYoBBS/$XrpcVersion"

    //米游社移动端网页UA
    const val HoyolabMobileWebUA =
        "Mozilla/5.0 (Linux; Android 12) Mobile miHoYoBBS/$XrpcVersion"

    //TODO 添加版本限制(maybe)
    const val PaimonsNotebookUA = "PaimonsNotebook/${PaimonsNotebookApplication.version}"

    //原神4 星铁8 ZZZ 12
    const val APP_ID = "8"

    var DeviceFp = ""
        private set

    var DeviceId = ""
        private set

    var DeviceId40 = ""
        private set

    var BBSDeviceId = ""
        private set

    var DeviceIdSeed = ""
        private set

    var DeviceIdSeedTime = 0L
        private set

    //生成米游社设备id
    private suspend fun generateDeviceId() {
        PreferenceKeys.DeviceId.editValue(UUID.randomUUID().toString())
    }

    //扫码登录设备Id
    private suspend fun generateDeviceId40() {
        val namespaceUuid = UUID.fromString("9450ea74-be9c-35c0-9568-f97407856768")
        val uuid = UUID.nameUUIDFromBytes("$DeviceId:$namespaceUuid".toByteArray(Charsets.UTF_8))

        val uuidBytes = ByteBuffer.wrap(ByteArray(16))
            .putLong(uuid.mostSignificantBits)
            .putLong(uuid.leastSignificantBits)
            .array()

        val sha1Digest = MessageDigest.getInstance("SHA-1")
        val hashBytes = sha1Digest.digest(uuidBytes)

        PreferenceKeys.DeviceId40.editValue(hashBytes.joinToString("") { "%02x".format(it) })
    }

    private suspend fun generateBBSDeviceId() {
        val id = getRandomChars(16)
        PreferenceKeys.BBSDeviceId.editValue(id)
    }

    //设置App签名
    private suspend fun setAppSignInfo() {
        dataStoreValuesFirst {
            val value = it[PreferenceKeys.AuthorizeAppSign] ?: JSON.EMPTY_OBJ

            if (value == JSON.EMPTY_OBJ) {
                return@dataStoreValuesFirst
            }

            val info = JSON.parse<AppSignInfoData>(value)

            info.apply {
                appSign?.apply {
                    AuthorizeAppSign = this
                }
                appVersion?.apply {
                    AuthorizeAppSignVersion = this
                }
            }

            //当本地的值为不空表示需要使用本地的app_sign
            if (value.isNotEmpty()) {
                AuthorizeAppSign = value
            }
        }
    }

    private suspend fun setFp(fp: String) {
        publicDataApiClient.getExtList()

        val result = publicDataApiClient.getFp(fp)

        val newFp = if (result.success) {
            result.data.device_fp
        } else {
            "${(1000000000..9999999999).random()}"
        }
        DeviceFp = newFp

        PreferenceKeys.DeviceFp.editValue(newFp)
    }

    private fun getRandomChars(times: Int) = with(StringBuilder()) {
        val range = "abcdefghijklmnopqrstuvwxyz1234567890"
        repeat(times) {
            this.append(range.random())
        }
        this.toString()
    }

}