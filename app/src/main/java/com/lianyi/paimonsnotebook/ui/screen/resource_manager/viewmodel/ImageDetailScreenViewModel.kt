package com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.view.ImageDetailScreen
import java.io.File

class ImageDetailScreenViewModel : ViewModel() {

    private val dao =
        PaimonsNotebookDatabase.database.diskCacheDao

    var cacheFile: File? = null
        private set
    var size = IntSize.Zero
        private set

    var showPropertyDialog by mutableStateOf(false)

    var showFullScreen by mutableStateOf(false)

    var diskCacheData = DiskCache("")
        private set

    var imageUrl = ""
        private set

    val propertyList: List<Pair<String, String>> by lazy {
        listOf(
            "名称" to diskCacheData.name,
            "描述" to diskCacheData.description,
            "类型" to diskCacheData.typeName,
            "创建时间" to diskCacheData.createTimeFormat,
            "图片来源" to diskCacheData.createFrom,
            "最后使用时间" to diskCacheData.lastUseTimeFormat,
            "最后调用处" to diskCacheData.lastUseFrom,
            "使用次数" to "${diskCacheData.useCount}次",
            "图片地址" to diskCacheData.url
        )
    }

    var showRequestPermissionDialog by mutableStateOf(false)

    lateinit var checkStoragePermission: () -> Boolean

    private var removePlanDelete = false

    fun setImageCacheFileFromUrl(intent: Intent?, onError: () -> Unit) {
        val stringExtra = intent?.getStringExtra(ImageDetailScreen.EXTRA_IMAGE_URL)
        removePlanDelete =
            intent?.getBooleanExtra(ImageDetailScreen.EXTRA_REMOVE_PLAN_DELETE, false) ?: false

        if (stringExtra.isNullOrEmpty()) {
            return
        }

        cacheFile = PaimonsNotebookImageLoader.getCacheImageFileByUrl(stringExtra)

        if (cacheFile != null) {
            viewModelScope.launchIO {
                diskCacheData = dao.getDataByUrl(stringExtra)
            }
        } else {
            "无法读取图片内容,可能是图片没有完全加载导致的,重新加载图片以解决此问题"
                .errorNotify(false)
            onError.invoke()
        }
    }

    fun showPropertyDialog() {
        showPropertyDialog = true
    }

    fun dismissPropertyDialog() {
        showPropertyDialog = false
    }

    fun copyProperty(s: String) {
        SystemService.setClipBoardText(s)
        dismissPropertyDialog()
        "已将属性复制到剪切板".notify()
    }

    fun deleteImage(callback:()->Unit) {
        viewModelScope.launchIO {
            val targetValue = if(removePlanDelete){
                "已移出计划删除队列".notify()
                0
            }else{
                "已加入计划删除队列".notify()
                1
            }
            dao.setPlanDeleteFlag(diskCacheData.url, targetValue)

            callback.invoke()
        }
    }

    fun saveImage() {
        showRequestPermissionDialog =
            !(this::checkStoragePermission.isInitialized && checkStoragePermission.invoke())

        if (showRequestPermissionDialog) return

        FileHelper.saveImage(diskCacheData.url, onSuccess = {
            "图片保存到以下位置:${it}".notify(closeable = true)
        }) {
            "保存失败:本地图片不存在".errorNotify()
        }
    }
}