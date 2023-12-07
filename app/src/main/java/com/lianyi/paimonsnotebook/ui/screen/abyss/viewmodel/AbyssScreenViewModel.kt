package com.lianyi.paimonsnotebook.ui.screen.abyss.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.MonsterService
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss.SpiralAbyssData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.monster.MonsterData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AbyssScreenViewModel : ViewModel() {

    //本期与上期深渊记录
    var currentAbyssRecord by mutableStateOf<SpiralAbyssData?>(null)
    var previousAbyssRecord by mutableStateOf<SpiralAbyssData?>(null)

    //本期与上期深渊记录 加载状态
    var currentAbyssRecordLoadingState by mutableStateOf(LoadingState.Loading)
    var previousAbyssRecordLoadingState by mutableStateOf(LoadingState.Loading)

    var currentPageIndex by mutableIntStateOf(0)

    private val gameRecordClient = GameRecordClient()

    private var currentUser by mutableStateOf<User?>(null)
    var currentGameRole by mutableStateOf<UserGameRoleData.Role?>(null)
        private set

    // 1当期 2上期
    private val scheduleType = arrayOf(
        "1", "2"
    )

    private val avatarMap = mutableMapOf<Int, AvatarData>()

    private val monsterMap = mutableMapOf<String, MonsterData>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            avatarMap += AvatarService {
                currentAbyssRecordLoadingState = LoadingState.Error
                previousAbyssRecordLoadingState = LoadingState.Error
            }.avatarList.associateBy {
                it.id
            }

            monsterMap += MonsterService{
                currentAbyssRecordLoadingState = LoadingState.Error
                previousAbyssRecordLoadingState = LoadingState.Error
            }.monsterList.associateBy {
                it.name
            }


            AccountHelper.selectedUserFlow.collect {
                currentUser = it
                currentGameRole = currentUser?.getSelectedGameRole()

                setAbyssRecord(currentPageIndex)
            }
        }
    }

    val tabs = arrayOf(
        "本期", "上期"
    )

    var showUserGameRoleDialog by mutableStateOf(false)

    var showConfirmDialog by mutableStateOf(false)

    fun showUserGameRoleDialog() {
        showUserGameRoleDialog = true
    }

    fun dismissUserGameRoleDialog() {
        showUserGameRoleDialog = false
    }

    fun showConfirmDialog() {
        showConfirmDialog = true
    }

    fun dismissConfirmDialog() {
        showConfirmDialog = false
    }

    fun onPageIndexChange(value: Int) {
        currentPageIndex = value

        when (currentPageIndex) {
            0 -> {
                if (currentAbyssRecord == null) {
                    setAbyssRecord(currentPageIndex)
                }
            }

            1 -> {
                if (previousAbyssRecord == null) {
                    setAbyssRecord(currentPageIndex)
                }
            }
        }
    }

    private fun setAbyssRecord(pageIndex: Int) {
        val state = if (currentUser == null || currentGameRole == null) {
            LoadingState.Error
        } else {
            LoadingState.Loading
        }

        when (pageIndex) {
            0 -> {
                currentAbyssRecordLoadingState = state
            }

            1 -> {
                previousAbyssRecordLoadingState = state
            }
        }

        if (state == LoadingState.Error) return

        viewModelScope.launch(Dispatchers.IO) {
            val userAndUid =
                UserAndUid(
                    userEntity = currentUser!!.userEntity,
                    playerUid = PlayerUid.fromGameRole(role = currentGameRole!!)
                )

            val result = gameRecordClient.getSpiralAbyssData(
                user = userAndUid, scheduleType = scheduleType[currentPageIndex]
            )

            if (result.success) {
                val data =
                    result.data.copy(floors = result.data.floors.sortedByDescending { it.index })

                val resultState = if (data.floors.isEmpty()) {
                    LoadingState.Empty
                } else {
                    LoadingState.Success
                }

                when (pageIndex) {
                    0 -> {
                        currentAbyssRecord = data
                        currentAbyssRecordLoadingState = resultState
                    }

                    1 -> {
                        previousAbyssRecord = data
                        previousAbyssRecordLoadingState = resultState
                    }
                }
            } else {
                val resultState = LoadingState.Error

                showConfirmDialog = result.validate

                when (pageIndex) {
                    0 -> {
                        currentAbyssRecordLoadingState = resultState
                    }

                    1 -> {
                        previousAbyssRecordLoadingState = resultState
                    }
                }
            }
        }
    }

    fun getAvatarFromMetadata(avatarId: Int) = avatarMap[avatarId]

    fun getMonsterFromMetadata(monsterName:String) = monsterMap[monsterName]

    fun onChangeGameRole(user: User, role: UserGameRoleData.Role) {
        dismissUserGameRoleDialog()

        currentUser = user
        currentGameRole = role

        setAbyssRecord(currentPageIndex)
    }

    fun goValidateScreen() {
        dismissConfirmDialog()

        if (currentUser == null) {
            "当前用户状态异常".errorNotify()
            return
        }

        HomeHelper.goActivityByIntent {
            setComponentName(HoyolabWebActivity::class.java)
            putExtra("mid", currentUser?.userEntity?.mid ?: "")
        }
    }
}