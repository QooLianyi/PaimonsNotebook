package com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeStampType
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.ImageHeaderData
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.view.ImageDetailScreen
import kotlinx.coroutines.launch
import java.io.File

class ResourceManagerViewModel : ViewModel() {

    private val dao = PaimonsNotebookDatabase.database.diskCacheDao

    val tabs = arrayOf("全部", "计划删除")

    var currentTabIndex by mutableIntStateOf(0)

    val diskCacheDataList = mutableStateListOf<Pair<ImageHeaderData, List<DiskCache>>>()

    val diskCachePlanDeleteDataList = mutableStateListOf<Pair<ImageHeaderData, List<DiskCache>>>()

    val selectedUrls = mutableStateOf(setOf<String>())

    val isSelectionMode = mutableStateOf(false)

    var planDeleteListIsEmpty by mutableStateOf(false)

    private var currentUserPointerPosition = Offset.Zero

    var showConfirm by mutableStateOf(false)

    init {
        viewModelScope.launch {
            launch {
                dao.getData().collect { diskCacheList ->
                    diskCacheDataList.clear()

                    diskCacheDataList += diskCacheList.groupBy {
                        TimeHelper.getTime(it.createTime, TimeStampType.YYYY_MM_DD)
                    }.map {
                        ImageHeaderData(
                            text = it.key,
                            count = it.value.size
                        ) to it.value
                    }
                }
            }
            launchMain {
                snapshotFlow { selectedUrls.value.size }.collect {
                    isSelectionMode.value = selectedUrls.value.isNotEmpty()
                }
            }
            launch {
                dao.getPlanDeleteData().collect {
                    diskCachePlanDeleteDataList.clear()
                    diskCachePlanDeleteDataList += ImageHeaderData(
                        text = "计划删除的图片",
                        count = it.size
                    ) to it

                    planDeleteListIsEmpty = it.isEmpty()
                }
            }
        }
    }

    fun onClickImage(selected: Boolean, diskCache: DiskCache) {
        if (isSelectionMode.value) {
            if (selected && currentUserPointerPosition == Offset.Zero) {
                selectedUrls.value -= diskCache.url
            } else {
                selectedUrls.value += diskCache.url
            }
        } else {
            HomeHelper.goActivityByIntent {
                setComponentName(ImageDetailScreen::class.java)
                putExtra(ImageDetailScreen.EXTRA_IMAGE_URL, diskCache.url)
                if (currentTabIndex == 1) {
                    putExtra(ImageDetailScreen.EXTRA_REMOVE_PLAN_DELETE, true)
                }
            }
        }
    }

    fun changeCurrentUserPointerPosition(offset: Offset) {
        currentUserPointerPosition = offset
    }

    fun addSelectedUrl(url: String) {
        selectedUrls.value += url
    }

    fun getCacheImage(diskCache: DiskCache): File? =
        PaimonsNotebookImageLoader.getCacheImageFileByUrl(diskCache.url)

    private fun showConfirm() {
        showConfirm = true
    }

    fun dismissConfirm() {
        showConfirm = false
    }

    fun selectionAll(allSelected: Boolean, list: List<DiskCache>) {
        val urls = list.map { it.url }
        if (allSelected) {
            selectedUrls.value -= urls
        } else {
            selectedUrls.value += urls
        }
    }

    //进行标记,下次启动时删除
    fun deleteSelectedImage() {
        showConfirm()
    }

    //确认删除
    fun confirmDeleteSelectedImage() {
        viewModelScope.launchIO {
            if (currentTabIndex == 0) {
                dao.setPlanDeleteFlag(selectedUrls.value.toList(), 1)

                "已将所选图片加入计划删除队列".notify()
            } else {
                dao.setPlanDeleteFlag(selectedUrls.value.toList(), 0)

                "已将所选图片移出计划删除队列".notify()
            }

            cleanSelectedUrls()
            dismissConfirm()
        }
    }

    private fun cleanSelectedUrls() {
        selectedUrls.value = setOf()
    }

    fun onChangePage(i: Int) {
        currentTabIndex = i
        cleanSelectedUrls()
    }
}