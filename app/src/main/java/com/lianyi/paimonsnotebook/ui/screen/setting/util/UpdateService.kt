package com.lianyi.paimonsnotebook.ui.screen.setting.util

import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.ProgressListener
import com.lianyi.paimonsnotebook.common.util.request.ProgressResponseBody
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsByteResult
import com.lianyi.paimonsnotebook.common.util.request.getAsJsonNative
import com.lianyi.paimonsnotebook.ui.screen.setting.data.GithubLatestData
import okhttp3.OkHttpClient
import java.io.File
import java.net.URLEncoder

/*
* 更新服务
*
* */
class UpdateService {

    companion object {
        val updateRemoteEndpoints = listOf(
            "github",
            "mirror.ghproxy(推荐)",
            "cdn.jsdelivr"
        )
    }

    private var _githubLatestDataCache: GithubLatestData? = null

    lateinit var newVersionPackage: File
        private set

    //如果上次请求的时间小于十分钟就跳过此次请求
    private val skipQueryLatestInfo: Boolean
        get() = System.currentTimeMillis() - latestQueryTimestamp >= 600000L

    private var latestQueryTimestamp = 0L

    //检查新版本
    suspend fun checkNewVersion(
        onFoundNewVersion: () -> Unit,
        onFail: () -> Unit,
        onNotFoundNewVersion: () -> Unit
    ) {
        if (skipQueryLatestInfo) {
            val res = buildRequest {
                url(PaimonsNotebookApplication.latestReleaseUrl)
            }.getAsJsonNative<GithubLatestData>(getParameterizedType(GithubLatestData::class.java))

            //更新数据缓存
            _githubLatestDataCache = res
            latestQueryTimestamp = System.currentTimeMillis()

            //当最后一个版本没有内容或文件列表为空,检查更新失败
            //此处isNullOrEmpty是必须的,无网络时assets为空,getAsJsonNative内部处理了返回值res不会为空
            if (res?.assets.isNullOrEmpty() || res == null) {
                onFail.invoke()
                return
            }
        }

        checkNewVersionFromCache(
            onFoundNewVersion = onFoundNewVersion,
            onNotFoundNewVersion = onNotFoundNewVersion
        )
    }

    private fun checkNewVersionFromCache(
        onFoundNewVersion: () -> Unit,
        onNotFoundNewVersion: () -> Unit
    ) {
        if (compareVersion()) {
            onFoundNewVersion.invoke()
        } else {
            onNotFoundNewVersion.invoke()
        }
    }

    /*
    * 比对版本
    * true为需要更新,false不需要
    *
    * 原理是从tag_name中分割后面附带的version_code,如果大于当前version_code则需要更新
    * */
    private fun compareVersion(): Boolean {
        if (_githubLatestDataCache == null) return false

        val currentVersion = PaimonsNotebookApplication.versionCode
        val tagNameSplit = _githubLatestDataCache!!.tag_name.split("-")

        //当tagNameSplit的size==1时,表明没有在tag中设置版本号,直接返回false
        if (tagNameSplit.size == 1) {
            return false
        }

        val latestVersion = tagNameSplit.last().toIntOrNull() ?: 0

        return latestVersion > currentVersion
    }

    //下载新版本安装包
    suspend fun downloadNewVersionPackage(
        remoteEndpointName: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
        onProgress: (progress: Float) -> Unit
    ) {
        //当缓存内容为空时直接返回
        if (_githubLatestDataCache == null || _githubLatestDataCache?.assets?.isEmpty() == true) {
            onFail.invoke()
            return
        }

        val requestUrl = getRequestUrlByEndpointName(remoteEndpointName)

        val saveFile = FileHelper.getPackageSaveFile(_githubLatestDataCache!!.name)

        try {
            val progressListener = object : ProgressListener {
                override fun update(
                    url: String,
                    bytesRead: Long,
                    contentLength: Long,
                    done: Boolean
                ) {
                    onProgress.invoke((bytesRead.toDouble() / contentLength).toFloat())
                }
            }

            val client = OkHttpClient.Builder().addInterceptor {
                val response = it.proceed(it.request())
                response.newBuilder().body(
                    ProgressResponseBody(
                        response.request.url.toUrl().toString(), response.body!!, progressListener
                    )
                ).build()
            }.build()

            val res = buildRequest {
                url(requestUrl)
            }.getAsByteResult(client)

            if (res.first && res.second != null) {
                FileHelper.saveFile(saveFile, res.second!!) {}

                newVersionPackage = saveFile

                onSuccess.invoke()
            } else {
                onFail.invoke()
            }
        } catch (_: Exception) {
            onFail.invoke()
        }
    }

    private fun getRequestUrlByEndpointName(
        name: String
    ): String {
        val asset =
            _githubLatestDataCache?.assets?.takeFirstIf { it.name == "app-release.apk" }

        return when (name) {
            "github" -> {
                asset?.browser_download_url ?: ""
            }

            "mirror.ghproxy(推荐)" -> {
                "https://mirror.ghproxy.com/?q=${
                    URLEncoder.encode(
                        asset?.browser_download_url ?: "",
                        "utf-8"
                    )
                }"
            }

            "cdn.jsdelivr" -> {
                "https://cdn.jsdelivr.net/gh/QooLianyi/PaimonsNotebook/app/release/app-release.apk"
            }

            else -> {
                "选择了未知的站点".errorNotify()
                ""
            }
        }
    }
}