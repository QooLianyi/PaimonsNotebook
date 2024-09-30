package com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.util.SettingsHelper
import com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.data.ShortcutsListData

class ShortcutsManagerScreenViewModel : ViewModel() {

    //快捷方式列表,暂时与主页菜单一致
    val shortcutsList = mutableStateListOf<ShortcutsListData>()

    var enableShortcutsList by mutableStateOf(false)

    init {
        //将所有侧边栏功能添加至列表
        shortcutsList += HomeHelper.getShowModalItemData(SettingsHelper.configurationFlow.value.enableMetadata)
            .map {
                ShortcutsListData(it)
            }

        viewModelScope.launchMain {
            dataStoreValues { preferences ->
                enableShortcutsList = preferences[PreferenceKeys.EnableShortcutsList] ?: false

                val mapJson = preferences[PreferenceKeys.ShortcutsList] ?: JSON.EMPTY_OBJ
                loadLocalShortcutsList(mapJson)

                //当启用列表,并且快捷方式列表选中的不为空 则立即设置快捷方式列表
                if (enableShortcutsList && shortcutsList.takeFirstIf { it.selected } != null) {
                    submit(false)
                } else {
                    onDisableShortcutsList()
                }
            }
        }
    }

    //选中的快捷方式个数
    var selectedShortcutsCount by mutableIntStateOf(0)
        private set


    //当禁用快捷方式列表时
    private fun onDisableShortcutsList() {
        //禁用时 清空快捷方式列表
        setDynamicShortcuts(listOf(), PaimonsNotebookApplication.context)
    }

    //加载本地的快捷方式列表
    private fun loadLocalShortcutsList(
        mapJson: String
    ) {
        if (mapJson != JSON.EMPTY_OBJ) {
            selectedShortcutsCount = 0

            val map = JSON.parse<Map<String, Int>>(
                mapJson,
                getParameterizedType(
                    Map::class.java,
                    String::class.java,
                    Int::class.javaObjectType
                )
            )

            shortcutsList.forEach { shortcutsListData ->
                val clsName = shortcutsListData.modalItemData.target.name
                val index = map[clsName]

                if (index != null) {
                    shortcutsListData.index = index
                    selectedShortcutsCount++
                    shortcutsListData.selected = true
                } else {
                    shortcutsListData.reset()
                }
            }

            sortList()
        }
    }

    /*
    * 更改快捷方式排序位置
    *
    * item:ModalItemData
    * index:当前索引
    * isUp:是否是向前排序
    * */
    fun changeShortcutsPosition(
        item: ShortcutsListData,
        index: Int,
        isUp: Boolean = false
    ) {
        if (!enableShortcutsList) return

        val newIndex =
            if (isUp) {
                val temp = index - 1
                if (temp < 0) {
                    0
                } else {
                    temp
                }
            } else {
                val temp = index + 1
                if (temp >= shortcutsList.size) {
                    shortcutsList.size - 1
                } else {
                    temp
                }
            }

        swapListItem(item, index, newIndex)
    }


    //当点击快捷方式列表项时
    fun onShortcutsListItemClick(index: Int, item: ShortcutsListData) {
        if (!enableShortcutsList) return

        val firstUnselectedItemIndex = shortcutsList.indexOfFirst { !it.selected }

        //当第一个索引为4,并且点击的item未选中时
        if (firstUnselectedItemIndex == 4 && !item.selected) {
            "只能添加4个功能到快捷方式列表中".warnNotify(false)
            return
        }

        if (item.selected) {
            selectedShortcutsCount--

            item.reset()
            sortList()
        } else {
            selectedShortcutsCount++
            item.selected = true

            swapListItem(item, index, firstUnselectedItemIndex)
        }
    }

    //为列表排序
    private fun sortList() {
        shortcutsList.sortBy { it.index }
    }

    //交换列表数据
    private fun swapListItem(
        item: ShortcutsListData,
        fromIndex: Int,
        toIndex: Int
    ) {
        val swapItem = shortcutsList[toIndex]
        shortcutsList[toIndex] = item
        shortcutsList[fromIndex] = swapItem

        item.index = toIndex
        swapItem.index = fromIndex
    }

    //提交
    fun submit(notify: Boolean = true) {
        if (!enableShortcutsList) return

        val context = PaimonsNotebookApplication.context

        val list = shortcutsList.takeWhile { it.selected }
        val shortcutInfoList = mutableListOf<ShortcutInfo>()

        list.forEach { shortcutsListData ->
            val item = shortcutsListData.modalItemData

            val shortcutInfo = ShortcutInfo.Builder(context, item.name)
                .setIcon(Icon.createWithResource(context, item.icon))
                .setShortLabel(item.name)
                .setLongLabel(item.name)
                .setIntent(Intent(context, item.target).apply {
                    action = "action"
                })
                .build()

            shortcutInfoList += shortcutInfo
        }

        setDynamicShortcuts(shortcutInfoList, context)

        viewModelScope.launchIO {
            //用于存储的结构,key = 类名,value = 顺序
            val map = list.associate {
                it.modalItemData.target.name to it.index
            }

            val json = JSON.stringify(map)
            PreferenceKeys.ShortcutsList.editValue(json)
        }

        if (notify) {
            "已按照当前配置更新快捷方式列表".notify()
        }
    }

    //设置快捷方式列表
    private fun setDynamicShortcuts(
        shortcutInfoList: List<ShortcutInfo>,
        context: Context
    ) {
        val manager = context.getSystemService(ShortcutManager::class.java)

        manager.dynamicShortcuts = shortcutInfoList
    }

    //切换快捷方式列表
    fun toggleShortcutsList() {
        viewModelScope.launchIO {
            val newValue = !enableShortcutsList
            PreferenceKeys.EnableShortcutsList.editValue(newValue)

            if (newValue) {
                "已启用快捷方式列表".notify()
            } else {
                "已禁用快捷方式列表,已选中的功能将不会生效".warnNotify(false)
            }
        }
    }

    //清理列表
    fun clearList() {
        shortcutsList.clear()
    }
}