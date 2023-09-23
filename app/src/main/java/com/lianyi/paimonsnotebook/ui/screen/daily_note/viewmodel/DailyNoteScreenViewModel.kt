package com.lianyi.paimonsnotebook.ui.screen.daily_note.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.daily_note.util.DailyNoteUtil
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyNoteScreenViewModel : ViewModel() {

    val dailyNoteList = mutableStateListOf<DailyNote>()

    val allowAddDailyNoteUserList = mutableStateListOf<User>()

    var selectedUser: User? = null

    private lateinit var optionDailyNote: DailyNote
    var optionSortValue by mutableFloatStateOf(0f)

    init {
        viewModelScope.launch {
            launch {
                AccountHelper.selectedUserFlow.collect {
                    selectedUser = it
                }
            }
            launch {
                DailyNoteUtil.dailyNoteFlow.collect {
                    dailyNoteList.clear()
                    dailyNoteList.addAll(it)
                    it.forEach {
                        println("list = ${it.dailyNoteEntity.sort}")
                    }
                }
            }
            launch {
                DailyNoteUtil.notInDailyNoteDBUserListFlow.collect {
                    allowAddDailyNoteUserList.clear()
                    allowAddDailyNoteUserList.addAll(it)
                }
            }
        }
    }

    var isRefreshing by mutableStateOf(false)

    var showSelectGameRoleDialog by mutableStateOf(false)

    var showDailyNoteOptionDialog by mutableStateOf(false)

    var showLoading by mutableStateOf(false)

    fun refreshDailyNote() {
        viewModelScope.launch(Dispatchers.IO) {
            "正在获取最新的实时便笺".notify()
            showLoading = true

            DailyNoteUtil.reloadDailyNote()
            showLoading = false
        }
    }

    fun deleteDailyNote(dailyNote: DailyNote) {
        DailyNoteUtil.deleteDailyNote(dailyNote)

        "[${dailyNote.role.nickname}]已被删除".warnNotify(false)
    }

    fun showSelectGameRoleDialog() {
        if (allowAddDailyNoteUserList.isEmpty()) {
            "已经没有能够添加的账号了".notify()
            return
        }

        showSelectGameRoleDialog = true
    }


    fun dismissSelectGameRoleDialog() {
        showSelectGameRoleDialog = false
    }

    //对话框选择游戏角色
    fun onDialogSelectUserGameRole(user: User, role: UserGameRoleData.Role) {
        viewModelScope.launch(Dispatchers.IO) {
            showSelectGameRoleDialog = false
            showLoading = true

            DailyNoteUtil.addDailyNote(user, role)
            showLoading = false
        }
    }

    fun dismissDailyNoteOptionDialog(index: Int) {
        if (index == 0) {
            confirmUpdateDailyNote()
        }
        showDailyNoteOptionDialog = false
    }

    fun showDailyNoteOptionDialog(dailyNote: DailyNote) {
        optionDailyNote = dailyNote
        optionSortValue = dailyNote.dailyNoteEntity.sort.toFloat()

        showDailyNoteOptionDialog = true
    }

    private fun confirmUpdateDailyNote() {
        DailyNoteUtil.updateDailyNote(optionDailyNote.dailyNoteEntity.copy(sort = optionSortValue.toInt()))
        "修改成功".notify()
    }

    fun goHoyolabActivity() {
        if (selectedUser == null) {
            "还未设置默认用户".errorNotify()
            return
        }

        HomeHelper.goActivity(HoyolabWebActivity::class.java)
    }

    val expeditionsAvatarIconDiskCache = DiskCache(
        url = "",
        name = "角色头像",
        description = "探索派遣角色图标",
        createFrom = "实时便笺",
        lastUseFrom = "实时便笺",
        type = DiskCacheDataType.Stable
    )
}