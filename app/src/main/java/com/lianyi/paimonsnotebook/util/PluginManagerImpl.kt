package com.lianyi.paimonsnotebook.util

import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import android.content.res.Resources
import dalvik.system.DexClassLoader
import java.io.File

object PluginManagerImpl {
    lateinit var resources: Resources
    lateinit var dexClassLoader: DexClassLoader
    lateinit var mContext: Context
    fun init(context: Context){
        mContext = context
        val path = context.getExternalFilesDir(null)
        val cachePath = File(path,"cache")
        if(!cachePath.exists()){
            cachePath.mkdirs()
        }
        resources = createResource("${path}/plugin/uigf-excel-debug.apk")
        dexClassLoader = DexClassLoader("${path}/plugin/uigf-excel-debug.apk",cachePath.absolutePath,"",
            context.classLoader)
    }

    private fun createResource(apkPath:String):Resources{
        val manager = mContext.packageManager
        val packInfo = manager.getPackageArchiveInfo(apkPath,GET_META_DATA)!!
        packInfo.applicationInfo.apply {
            publicSourceDir = apkPath
            sourceDir = apkPath
        }
        try {
            return manager.getResourcesForApplication(packInfo.applicationInfo)
        }catch (e:Exception){
            error("CAN NOT CREATE RESOURCE")
        }
    }

}