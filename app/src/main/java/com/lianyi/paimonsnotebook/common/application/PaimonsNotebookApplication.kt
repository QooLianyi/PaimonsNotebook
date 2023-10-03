package com.lianyi.paimonsnotebook.common.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.ShortcutManager
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import com.lianyi.paimonsnotebook.BuildConfig
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class PaimonsNotebookApplication : Application()  {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context
        val context by lazy {
            mContext
        }

        val version by lazy {
            BuildConfig.VERSION_NAME
        }

        val name by lazy {
            context.getString(R.string.app_name)
        }

        val qqGroupKey by lazy {
            "qhNCaJ5EPHebQIX4-G2mpQu86f-WlAc7"
        }

        val githubUrl by lazy {
            "https://github.com/QooLianyi/PaimonsNotebook"
        }

        val isDebug by lazy {
            context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
    }


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())

//        initActivityLifecycleCallbacks()

//        initShortcutManager()

        executeDiskCachePlanDelete()
    }

    private fun initShortcutManager() {
        val manager = getSystemService(ShortcutManager::class.java)

//        val shortcutInfo = ShortcutInfo.Builder(context,"daily_note")
//            .setIcon(Icon.createWithResource(context, R.drawable.ic_settings))
//            .setShortLabel("实时便笺")
//            .setLongLabel("实时便笺")
//            .setIntent(Intent(context,DailyNoteScreen::class.java).apply {
//                action = "start"
//            })
//            .build()

        manager.dynamicShortcuts = listOf()
    }

    //监听全部activity的状态
    private fun initActivityLifecycleCallbacks() {

        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
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

    //清除计划删除图片文件
    private fun executeDiskCachePlanDelete() {
        CoroutineScope(Dispatchers.IO).launch {
            PaimonsNotebookDatabase.database.diskCacheDao.apply {

                getPlanDeleteData().first().forEach {
                    PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)?.delete()
                    PaimonsNotebookImageLoader.getCacheImageMetadataFileByUrl(it.url)?.delete()
                }

                removePlanDelete()
            }
        }
    }

}