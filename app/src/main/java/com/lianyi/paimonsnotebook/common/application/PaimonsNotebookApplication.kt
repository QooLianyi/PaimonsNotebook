package com.lianyi.paimonsnotebook.common.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import cat.ereza.customactivityoncrash.config.CaocConfig
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import com.lianyi.paimonsnotebook.BuildConfig
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.util.builder.imageLoader
import com.lianyi.paimonsnotebook.common.util.coil.MergeInterceptor
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirstLambda
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.request.emptyOkHttpClient
import com.lianyi.paimonsnotebook.common.view.CrashScreen
import com.lianyi.paimonsnotebook.ui.screen.splash.view.SplashScreen
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


class PaimonsNotebookApplication : Application(), ImageLoaderFactory {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context
        val context by lazy {
            mContext
        }

        const val version = BuildConfig.VERSION_NAME

        const val versionCode = BuildConfig.VERSION_CODE

        const val PaimonsNotebookUA = "${CoreEnvironment.PaimonsNotebookUA}/${version}"

        val name by lazy {
            context.getString(R.string.app_name)
        }

        val qqGroupKey by lazy {
            "qhNCaJ5EPHebQIX4-G2mpQu86f-WlAc7"
        }

        val githubUrl by lazy {
            "https://github.com/QooLianyi/PaimonsNotebook"
        }

        //从git上获取最新release
        val latestReleaseUrl by lazy {
            "https://api.github.com/repos/QooLianyi/PaimonsNotebook/releases/latest"
        }

        val isDebug by lazy {
            context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        //调用核心环境初始化
        CoreEnvironment.init()

        //crashScreen
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
            .enabled(true)
            .showErrorDetails(true)
            .showRestartButton(false)
            .logErrorOnRestart(false)
            .minTimeBetweenCrashesMs(300)
            .restartActivity(SplashScreen::class.java)
            .errorActivity(CrashScreen::class.java)
            .apply()

        //release环境
        if (!BuildConfig.DEBUG) {
            //启用AppCenter
            AppCenter.start(
                this,
                BuildConfig.APPCENTER_SECRET,
                Analytics::class.java,
                Crashes::class.java
            )
            //执行计划删除
            executeDiskCachePlanDelete()
        }

        //debug环境
        if (BuildConfig.DEBUG) {
            //debug包启用activity生命周期回调
            initActivityLifecycleCallbacks()

//            StrictMode.enableDefaults()

            //启用LeakedClosableViolation跟踪
//            try {
//                Class.forName("dalvik.system.CloseGuard")
//                    .getMethod("setEnabled", Boolean::class.javaPrimitiveType)
//                    .invoke(null, true)
//                println("dalvik.system.CloseGuard : 启用成功")
//            } catch (e: ReflectiveOperationException) {
//                println("dalvik.system.CloseGuard : 启用失败")
//            }
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
        FileHelper.clearTempFile()
    }

    //监听全部activity的状态
    private fun initActivityLifecycleCallbacks() {

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                println("activity:${activity.componentName.className} onActivityCreated")
            }

            override fun onActivityStarted(activity: Activity) {
                println("activity:${activity.componentName.className} onActivityStarted")
            }

            override fun onActivityResumed(activity: Activity) {
                println("activity:${activity.componentName.className} onActivityResumed")
            }

            override fun onActivityPaused(activity: Activity) {
                println("activity:${activity.componentName.className} onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                println("activity:${activity.componentName.className} onActivityStopped")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                println("activity:${activity.componentName.className} onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                println("activity:${activity.componentName.className} onActivityDestroyed")
            }
        })

    }

    //最后调用的时间戳与当前时间戳相差大于此数,则判断为无用图片,默认为7天(604800000L)
    private val deleteTimeStampLimit = 604800000L

    //清除计划删除图片文件
    private fun executeDiskCachePlanDelete() {
        CoroutineScope(Dispatchers.IO).launch {
            //TODO 目前已知删除disckCache文件会导致删除图片再次缓存图片,再从本地读取缓存为null
            launchIO {
                val autoClean = dataStoreValuesFirstLambda {
                    this[PreferenceKeys.EnableAutoCleanExpiredImages] ?: true
                }

                if (!autoClean) return@launchIO

                PaimonsNotebookDatabase.database.diskCacheDao.apply {
                    updateAllDataPlanDeleteStatus(System.currentTimeMillis(), deleteTimeStampLimit)

                    getPlanDeleteData().first().forEach {
                        PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)?.delete()
                        PaimonsNotebookImageLoader.getCacheImageMetadataFileByUrl(it.url)?.delete()
                    }

                    removeAllPlanDeleteData()
                }
            }


            //删除私有目录下的临时文件
            launchIO {
                FileHelper.clearTempFile()
            }
        }
    }

    private val imageCachePath by lazy {
        context.filesDir.resolve("image_cache")
    }

    private val imageCache by lazy {
        DiskCache.Builder()
            .directory(imageCachePath)
            .maxSizePercent(1.0)
            .build()
    }

    override fun newImageLoader(): ImageLoader =
        imageLoader {
            components {
                callFactory(emptyOkHttpClient)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add { result, options, _ -> ImageDecoderDecoder(result.source, options, false) }
                } else {
                    add { result, options, _ -> GifDecoder(result.source, options, false) }
                }

                add(MergeInterceptor)
            }
            diskCache(imageCache)
            crossfade(300)
            error(R.drawable.ic_image_error)
            respectCacheHeaders(false)
        }
}