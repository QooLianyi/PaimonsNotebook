package com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.memory.MemoryCache
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ResourceManagerViewModel : ViewModel() {

    private val database by lazy {
        PaimonsNotebookDatabase.database
    }

    private val imageLoader by lazy {
        PaimonsNotebookImageLoader.current
    }

    private val sdf by lazy {
        SimpleDateFormat("yyyy年MM月dd日", TimeHelper.locale)
    }

    val tabs = arrayOf("最近", "计划删除")

    var currentTabIndex by mutableIntStateOf(0)

    var isMultipleSelect by mutableStateOf(false)

    private val mDiskCacheList = mutableStateListOf<DiskCache>()

    val diskCacheDataList = mutableStateListOf<DiskCacheGroupData>()

    val planDeleteDiskCacheList = mutableStateListOf<DiskCacheGroupData.GroupItem>()

    var selectAll by mutableStateOf(false)

    var selectCount by mutableStateOf(0)

    var showResourceDropMenu by mutableStateOf(false)

    var resourceDropMenuOptions = arrayOf("清除过期临时图片")

    var showImageData by mutableStateOf<DiskCacheGroupData.GroupItem?>(null)

    private var showImageGroupData: DiskCacheGroupData? = null

    var showImage by mutableStateOf(false)

    var showImageDetailDropMenu by mutableStateOf(false)

    val imageDetailDropMenuOptions = arrayOf("图片详情", "保存至本地", "删除图片")

    var showImageDetailInfo by mutableStateOf(false)

    //最后调用的时间戳与当前时间戳相差大于此数,则判断为无用图片,默认为7天(604800000L)
    private val deleteTimeStampLimit = 604800000L

    init {
        viewModelScope.launch {
            launch {
                database.diskCacheDao.getAllData().collect {
                    mDiskCacheList.clear()
                    mDiskCacheList.addAll(it)
                    syncDataList()
                }
            }
            launch {
//                snapshotFlow { pageState.currentPage }.collect {
//                    currentTabIndex = it
//                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun syncDataList() {
        diskCacheDataList.clear()
        planDeleteDiskCacheList.clear()

        mDiskCacheList.groupBy { sdf.format(it.createTime) }.forEach { (k, v) ->
            val list = mutableListOf<DiskCacheGroupData.GroupItem>()

            v.forEach { diskCacheData ->

                val diskCache = imageLoader.diskCache?.openSnapshot(diskCacheData.url)?.data?.toFile()
                val groupItem = DiskCacheGroupData.GroupItem(diskCacheData, diskCache)

                if (diskCacheData.isPlanDelete) {
                    planDeleteDiskCacheList += groupItem
                } else {
                    list += groupItem
                }
            }

            if (list.isNotEmpty()) {
                diskCacheDataList += DiskCacheGroupData(k).apply {
                    this.list.addAll(list)
                }
            }

        }

        diskCacheDataList.sortByDescending { it.date }
    }

    fun enableMultipleSelect() {
        isMultipleSelect = true
    }

    private fun disableMultipleSelect() {
        isMultipleSelect = false

        setDiskCacheDataListSelectStatus(false)
    }

    private fun setDiskCacheDataListSelectStatus(status: Boolean) {
        diskCacheDataList.forEach { item ->
            item.isAllSelect = status
            item.list.forEach {
                it.select = status
            }
        }
    }

    fun selectAllAction() {
        setDiskCacheDataListSelectStatus(!selectAll)
        checkIsAllSelect()
    }

    fun showResourceDropMenu() {
        showResourceDropMenu = true
    }

    fun disableResourceDropMenu() {
        showResourceDropMenu = false
    }

    fun clickImage(item: DiskCacheGroupData.GroupItem, groupData: DiskCacheGroupData) {
        if (isMultipleSelect) {
            item.select = !item.select

            groupData.isAllSelect = groupData.list.count { it.select } == groupData.list.size
            checkIsAllSelect()
        } else {
            showImageData = item
            showImageGroupData = groupData
            showImage = true
        }
    }

    fun clickImageGroupActionButton(diskCacheGroupData: DiskCacheGroupData) {
        diskCacheGroupData.list.forEach {
            it.select = !diskCacheGroupData.isAllSelect
        }
        diskCacheGroupData.isAllSelect = !diskCacheGroupData.isAllSelect

        checkIsAllSelect()
    }

    private fun checkIsAllSelect() {
        selectCount = diskCacheDataList.sumOf { it.list.count { it.select } }
        selectAll = selectCount == diskCacheDataList.sumOf { it.list.size }
    }

    private fun deleteImage() {
        showImageData?.let {
            viewModelScope.launch(Dispatchers.IO) {
                showImageGroupData?.list?.remove(showImageData)

                deleteImageFromDatabaseAndCache(it.data.url)
                disableShowImage()

                viewModelScope.launch(Dispatchers.Main) {
                    "已加入计划删除队列".show()
                }
            }
        }
    }

    fun deleteSelectedImage(userCall: Boolean = true) {
        if (!isMultipleSelect && userCall) return

        viewModelScope.launch(Dispatchers.IO) {

            val urls = mutableListOf<String>()

            diskCacheDataList.forEach { item ->
                item.list.forEach {
                    if (it.select) {
                        urls += it.data.url
                    }
                }
            }

            database.diskCacheDao.updatePlanDeleteStatusByUrls(urls, 1)

            disableMultipleSelect()
        }

        if (userCall) "已将选择项加入计划删除队列".show()
    }

    //数据库标记,在下一次启动时删除
    private fun deleteImageFromDatabaseAndCache(url: String) {
        //TODO 目前已知删除disckCache文件会导致删除图片再次缓存图片,再从本地读取缓存为null,解决方法:创建数据库,存储需要删除文件的名称,应用启动前执行删除 [完成]
        imageLoader.memoryCache?.remove(MemoryCache.Key(url))
        database.diskCacheDao.updatePlanDeleteStatus(url, 1)
    }

    fun disableShowImage() {
        showImage = false
    }

    fun showImageDetailDropMenu() {
        showImageDetailDropMenu = true
    }

    fun disableImageDetailDropMenu() {
        showImageDetailDropMenu = false
    }

    fun imageDetailDropMenuAction(index: Int) {
        when (index) {
            0 -> showImageDetailInfo()
            1 -> FileHelper.saveImage(showImageData!!.data.url)
            2 -> deleteImage()
        }
        disableImageDetailDropMenu()
    }

    private fun showImageDetailInfo() {
        showImageDetailInfo = true
    }

    fun getPropertiesDialogData(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        showImageData?.data?.let {
            list += "名称" to it.name
            list += "描述" to it.description
            list += "类型" to it.type.name
            list += "创建时间" to it.getCreateTimeWithDateFormat()
            list += "图片来源" to it.createFrom
            list += "最后使用时间" to it.getLastUseTimeWithDateFormat()
            list += "最后调用处" to it.lastUseFrom
            list += "使用次数" to "${it.useCount}次"
            list += "图片地址" to it.url
        }
        return list
    }

    fun propertiesDialogAction(index: Int) {
        disableImageDetailInfo()
    }

    fun disableImageDetailInfo() {
        showImageDetailInfo = false
    }

    fun changeTab(index: Int) {
        //列表排序
//        when (index) {
//            0 -> {
//                if(currentTabIndex!=0){
//                    diskCacheDataList.sortBy { it.date }
//                }
//            }
//            1 -> {
//                if(currentTabIndex!=1){
//                    diskCacheDataList.sortByDescending { it.date }
//                }
//            }
//        }
        currentTabIndex = index
    }

    fun resourceManagerDropMenuAction(index: Int) {
        when (index) {
            0 -> clearTempImage()
        }
        disableResourceDropMenu()
    }

    private fun clearTempImage() {
        val currentTime = System.currentTimeMillis()
        var deleteCount = 0
        diskCacheDataList.forEach { group ->
            group.list.forEach { item ->
                val dif = currentTime - item.data.lastUseTime
                if (item.data.type == DiskCacheDataType.Temp && dif > deleteTimeStampLimit) {
                    item.select = true
                    deleteCount++
                }
            }
        }

        deleteSelectedImage(false)
        if (deleteCount == 0) {
            "当前没有过期的临时图片"
        } else {
            "已将${deleteCount}个图片加入删除队列"
        }.show()
    }

    fun removeDeletePlanFromDatabase(item: DiskCacheGroupData.GroupItem) {
        viewModelScope.launch(Dispatchers.IO) {
            database.diskCacheDao.updatePlanDeleteStatus(item.data.url, 0)
        }
        "已从删除列表移除".show()
    }

    fun copyPropertiesValue(value: String) {
        SystemService.setClipBoardText(value)
        "已复制到剪切板".notify()
    }

    fun onBackPressed(onFinish:()->Unit) {
        if (showImageDetailInfo) {
            disableImageDetailInfo()
        } else if (showImage) {
            disableShowImage()
        } else if (isMultipleSelect) {
            disableMultipleSelect()
        } else {
            onFinish.invoke()
        }
    }
}