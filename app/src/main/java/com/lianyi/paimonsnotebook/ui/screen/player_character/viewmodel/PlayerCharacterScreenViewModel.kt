package com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.player_character.view.PlayerCharacterDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerCharacterScreenViewModel : ViewModel() {

    private val avatarService by lazy {
        AvatarService(onMissingFile = this::onMissingFile)
    }

    private val weaponService by lazy {
        WeaponService(onMissingFile = this::onMissingFile)
    }

    var currentUser by mutableStateOf<User?>(null)
        private set

    var currentGameRole by mutableStateOf<UserGameRoleData.Role?>(null)
        private set

    init {
        viewModelScope.launchIO {
            launchMain {
                val user = AccountHelper.selectedUserFlow.value
                setUser(user)
                setGameRole(user?.getSelectedGameRole())
            }

            launchIO {
                //提前初始化service
                avatarService.avatarList
                weaponService.weaponList
            }
        }
    }

    var showGameRoleDialog by mutableStateOf(false)

    var loadingState by mutableStateOf(LoadingState.Loading)
        private set

    val characterList = mutableListOf<CharacterListData.CharacterData>()

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    //显示的角色列表
    val avatarDataList = mutableStateListOf<AvatarData>()

    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    private fun setUser(user: User?) {
        currentUser = user
    }

    private fun setGameRole(role: UserGameRoleData.Role?) {
        this.currentGameRole = role

        viewModelScope.launchIO {
            getPlayerCharacterList()
        }
    }

    private suspend fun getPlayerCharacterList() {
        withContext(Dispatchers.IO) {
            loadingState = LoadingState.Loading
            val user = currentUser
            val role = currentGameRole
            if (user == null || role == null) {
                "用户或角色不存在".errorNotify()
                loadingState = LoadingState.Empty
                return@withContext
            }

            val userAndUid = UserAndUid(user.userEntity, role.getPlayerUid())

            val res = gameRecordClient.getCharacterList(userAndUid)

            if (!res.success) {
                loadingState = LoadingState.Error
                "获取数据失败:${res.message}[${res.retcode}]".errorNotify()
                return@withContext
            }

            setCharacterList(res.data)
        }
    }

    private fun setCharacterList(characterListData: CharacterListData) {
        this.avatarDataList.clear()
        this.characterList.clear()

        this.avatarDataList += characterListData.list.mapNotNull {
            avatarService.avatarMap[it.id]
        }

        this.characterList += characterListData.list

        loadingState = LoadingState.Success
    }

    fun getAvatarDataById(i: Int): AvatarData? {
        return avatarService.avatarMap[i]
    }

    fun getWeaponDataById(i: Int): WeaponData? {
        return weaponService.weaponMap[i]
    }

    fun getWeaponFightPropertyFormatList(
        weaponData: WeaponData,
        i: Int,
        b: Boolean
    ): List<FightPropertyFormat> {
        return weaponService.getFightPropertyFormatList(
            weapon = weaponData,
            level = i,
            promoted = b
        )
    }

    fun onClickListItem(characterData: CharacterListData.CharacterData) {

        val user = currentUser

        if (user == null) {
            "当前用户为空".errorNotify()
            return
        }

        HomeHelper.goActivityByIntentNewTask {
            setComponentName(PlayerCharacterDetailScreen::class.java)

            putExtra(
                PlayerCharacterDetailScreen.PARAM_USER_AND_UID_JSON,
                JSON.stringify(user.getUserAndUid())
            )

            putExtra(
                PlayerCharacterDetailScreen.PARAM_CHARACTER_LIST_JSON,
                JSON.stringify(characterList)
            )

            putExtra(PlayerCharacterDetailScreen.PARAM_SELECTED_CHARACTER_ID, characterData.id)
        }
    }

    fun showChooseGameRoleDialog() {
        showGameRoleDialog = true
    }

    fun onUserGameRoleDialogButtonClick(index: Int) {
        onUserGameRoleDialogDismissRequest()
    }

    fun onSelectedGameRole(user: User, role: UserGameRoleData.Role) {
        setUser(user)
        setGameRole(role)
        onUserGameRoleDialogDismissRequest()
    }

    fun onUserGameRoleDialogDismissRequest() {
        showGameRoleDialog = false
    }
}