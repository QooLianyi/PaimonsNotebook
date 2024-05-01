package com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.isEmptyObj
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.util.enums.ViewModelAction
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementData
import com.lianyi.paimonsnotebook.ui.screen.achievement.data.AchievementGoalOverviewData
import com.lianyi.paimonsnotebook.ui.screen.achievement.util.enums.AchievementEditActionType
import com.lianyi.paimonsnotebook.ui.screen.achievement.util.enums.AchievementGoalScreenSortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AchievementGoalScreenViewModel : ViewModel() {

    /*
    * 是否禁用与成就记录的交互
    *
    * false时不响应操作
    * */
    private var disableInteraction = false

    init {
        viewModelScope.launchIO {
            dataStoreValues {
                //禁用交互默认值为false
                val newValue = it[PreferenceKeys.DisableAchievementInteraction] ?: false

                disableInteraction = newValue
            }
        }
    }

    private val achievementsDao = PaimonsNotebookDatabase.database.achievementsDao

    var goalOverviewData by mutableStateOf<AchievementGoalOverviewData?>(null)
        private set

    val achievementList = mutableStateListOf<AchievementData>()

    //如果是从搜索结果进入此界面的话会携带此参数
    private var targetId: Int = -1

    //滚动item的索引,当传入targetId时此变量更新通知界面刷新
    var targetItemIndex by mutableIntStateOf(-1)
        private set

    /*
    * 完成的成就的ID映射
    *
    * key = id,value = achievements(数据库实体)
    * */
    private var finishAchievementIdMap = mutableStateMapOf<Int, Achievements?>()

    //当前成就操作类型
    private var currentAchievementEditActionType = AchievementEditActionType.None

    //显示操作确认对话框
    var showActionConfirmDialog by mutableStateOf(false)
        private set

    //用于向activity发送消息进行一些操作
    private val _viewModelActionFlow = MutableStateFlow(ViewModelAction.None)
    val viewModelActionFlow = _viewModelActionFlow.asStateFlow()

    //排序类型
    private var sortType = AchievementGoalScreenSortType.FinishStatusDesc

    //分类完成数量
    var goalFinishCount by mutableIntStateOf(0)

    //分类总数
    var goalTotal = 0

    fun init(intent: Intent) {
        val list = JSON.parseList<AchievementData>(intent.getStringExtra("list") ?: JSON.EMPTY_LIST)

        val goalJson = intent.getStringExtra("goal") ?: JSON.EMPTY_OBJ
        //判断列表与
        if (list.isEmpty() || goalJson.isEmptyObj()) {
            "没有可显示的数据".errorNotify()

            viewModelScope.launchIO {
                _viewModelActionFlow.emit(ViewModelAction.Finish)
            }

            return
        }

        //此处一定不会为null
        goalOverviewData = JSON.parse<AchievementGoalOverviewData>(goalJson)

        targetId = intent.getIntExtra("target_id", -1)
        achievementList += list

        //初始化完成数量
        goalFinishCount = goalOverviewData!!.finishCount
        goalTotal = goalOverviewData!!.total

        //初始化完成的map,同步完成状态
        viewModelScope.launchIO {
            val ids = achievementList.map { it.id }

            finishAchievementIdMap += achievementsDao.getAchievementListByUserIdAndIds(
                userId = goalOverviewData!!.userId,
                ids
            ).associateBy {
                it.id
            }

            //设置成就完成的数量
            goalFinishCount = finishAchievementIdMap.values.size

            switchSortType()
        }

        //当传入了Id,使列表滚动到此处
        if (targetId != -1) {
            targetItemIndex = achievementList.indexOfFirst { it.id == targetId }
        }
    }

    fun onClickItem(achievementData: AchievementData) {
        //禁止交互时直接返回
        if (disableInteraction) {
            "成就记录交互已禁用,点击页面顶部的[锁]图标以解锁".warnNotify(false)
            return
        }

        viewModelScope.launchIO {
            val mapValue = finishAchievementIdMap[achievementData.id]

            finishAchievementIdMap[achievementData.id] =
                if (mapValue == null) {
                    goalFinishCount++
                    val achievements = achievementData.toDatabaseEntity(goalOverviewData!!.userId)

                    achievementsDao.insert(achievements)
                    achievements
                } else {
                    goalFinishCount--
                    achievementsDao.delete(mapValue)
                    null
                }
        }
    }

    /*
    * 获取成就状态
    *
    * 如果是完成的成就,返回true
    * */
    fun getItemStatus(item: AchievementData) = getAchievementEntity(item.id) != null

    //获取成就实体
    fun getAchievementEntity(id: Int) = finishAchievementIdMap[id]

    //当成就操作执行时
    fun onAchievementActionInvoke(actionType: AchievementEditActionType) {

        currentAchievementEditActionType = when (actionType) {

            /*
            * 此处偷懒
            * 界面上的交互切换按钮传入的总是EnableInteraction
            * 根据当前的禁用状态进行判断
            * 禁用状态为true,操作设置为EnableInteraction
            * false,操作设置为DisableInteraction
            * */
            AchievementEditActionType.EnableInteraction ->
                if (disableInteraction) AchievementEditActionType.EnableInteraction else AchievementEditActionType.DisableInteraction

            else -> actionType
        }

        showActionConfirmDialog = true
    }

    //当确认成就操作时
    fun onConfirmAchievementAction() {
        when (currentAchievementEditActionType) {
//            AchievementEditActionType.AddAll -> {
//                finishAchievementIdMap.clear()
//                achievementList.forEach {
//                    finishAchievementIdMap[it.id] =
//                        UIAFHelper.AchievementStatus.STATUS_REWARD_TAKEN
//                }
//                "已将当前分类的所有成就完成状态设置为完成".warnNotify(false)
//            }
//
//            AchievementEditActionType.RemoveAll -> {
//                finishAchievementIdMap.clear()
//                achievementList.forEach {
//                    finishAchievementIdMap[it.id] =
//                        UIAFHelper.AchievementStatus.STATUS_UNFINISHED
//                }
//                "已将当前分类的所有成就完成状态设置为未完成".warnNotify(false)
//            }

            AchievementEditActionType.DoNotSave -> {
                viewModelScope.launchIO {
                    _viewModelActionFlow.emit(ViewModelAction.Finish)
                    "此次的成就修改未保存(如果有的话)".warnNotify(false)
                }
            }

            AchievementEditActionType.DisableInteraction, AchievementEditActionType.EnableInteraction -> {
                viewModelScope.launchIO {
                    PreferenceKeys.DisableAchievementInteraction.editValue(!disableInteraction)
                    if (disableInteraction) {
                        "成就记录交互已禁用".warnNotify(false)
                    } else {
                        "成就记录交互已启用".notify()
                    }
                }
            }

            else -> {

            }
        }

        onAchievementDialogDismissRequest()
    }

    //获取成就操作确认对话框的显示内容
    fun getConfirmAchievementShowContent() = when (currentAchievementEditActionType) {
        AchievementEditActionType.AddAll -> "确认要将所有的成就设置为完成吗?(如果不小心确认了此操作,可以通过顶部第4个剪切板图标的按钮进行不保存修改退出操作)"
        AchievementEditActionType.RemoveAll -> "确认要将所有的成就设置为未完成吗?(如果不小心确认了此操作,可以通过顶部第4个剪切板图标的按钮进行不保存修改退出操作)"
        AchievementEditActionType.DoNotSave -> "确认要不保存此次的设置并退出吗?(如果此次进行了误触等不确定情况的操作,为避免记录错误的成就状态,可不保存此次修改直接退出)"
        AchievementEditActionType.DisableInteraction -> "确认要禁用成就记录交互吗?(禁用后可正常浏览成就,但无法通过与成就列表项进行交互修改成就的完成状态)"
        AchievementEditActionType.EnableInteraction -> "确认要启用成就记录交互吗?(启用后可通过点击记录列表项进行修改完成状态,但可能发生误触造成记录错误)"
        else -> "当你看到这个内容时,说明程序发生了未知的错误,告知开发者以解决此问题"
    }

    fun onAchievementDialogDismissRequest() {
        showActionConfirmDialog = false
    }

    //切换排序类型
    fun switchSortType() {
        sortType = if (sortType == AchievementGoalScreenSortType.FinishStatusDesc) {
            achievementList.sortBy {
                getAchievementEntity(it.id)?.current
            }

            AchievementGoalScreenSortType.FinishStatusAsc
        } else {
            achievementList.sortBy {
                it.id
            }

            AchievementGoalScreenSortType.FinishStatusDesc
        }
    }


    //当activity退出时
    fun onActivityDestroy() {
        viewModelScope.launchIO {
            if (currentAchievementEditActionType != AchievementEditActionType.DoNotSave) {
                "此次的操作没有保存".warnNotify(false)
                return@launchIO
            }

            val list = mutableListOf<Achievements>()

            finishAchievementIdMap.values.forEach {
                if (it != null) {
                    list += it
                }
            }

            //更新
            achievementsDao.update(list)
        }
    }
}