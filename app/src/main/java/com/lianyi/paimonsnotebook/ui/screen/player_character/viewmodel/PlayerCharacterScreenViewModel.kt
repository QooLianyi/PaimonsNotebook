package com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirstLambda
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerCharacterScreenViewModel : ViewModel() {

    init {
        viewModelScope.launchIO {
            launchMain {
                val useDefaultUser = dataStoreValuesFirstLambda {
                    this[PreferenceKeys.AlwaysUseDefaultUser] ?: false
                }
                if (useDefaultUser) {
                    setUser(AccountHelper.selectedUserFlow.value)
                }

                getCharacterList()
            }
        }
    }

    var loadingState by mutableStateOf(LoadingState.Loading)
        private set

    var currentUser by mutableStateOf<User?>(null)

    var currentCharacterDetailLoadingState by mutableStateOf(LoadingState.Loading)
        private set

    var currentCharacterDetail by mutableStateOf<CharacterDetailData?>(null)
        private set

    var showCharacterDetailPopupWindow by mutableStateOf(false)
        private set

    val characterList = mutableStateListOf<CharacterListData>()

    private val avatarService by lazy {
        AvatarService(onMissingFile = this::onMissingFile)
    }

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    //显示的角色列表
    val avatarDataList = mutableStateListOf<AvatarData>()

    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    fun setUser(user: User?) {
        currentUser = user
    }

    private suspend fun getCharacterList() {
        withContext(Dispatchers.IO) {
            loadingState = LoadingState.Loading
            val user = currentUser
            if (user == null) {
                loadingState = LoadingState.Empty
                return@withContext
            }

            val userAndUid = user.getUserAndUid()
            if (userAndUid == null) {
                "获取数据失败:当前账号没有默认角色".errorNotify()
                return@withContext
            }

            val res = gameRecordClient.getCharacterList(userAndUid)

            if (!res.success) {
                loadingState = LoadingState.Error
                "获取数据失败:${res.message}".warnNotify()
                return@withContext
            }

            setCharacterList(res.data)
        }
    }

    private fun setCharacterList(characterListData: CharacterListData) {
        this.avatarDataList.clear()
        this.avatarDataList += characterListData.list.mapNotNull {
            avatarService.avatarMap[it.id]
        }
    }

    private fun getCharacterDetailById(characterId: Int) {
        viewModelScope.launchIO {
            val user = currentUser

            if (user == null) {
                loadingState = LoadingState.Empty
                return@launchIO
            }

            val userAndUid = user.getUserAndUid()
            if (userAndUid == null) {
                "获取数据失败:当前账号没有默认角色".errorNotify()
                return@launchIO
            }

            currentCharacterDetailLoadingState = LoadingState.Loading
            showCharacterDetailPopupWindow = true

            val res = gameRecordClient.getCharacterDetail(userAndUid, listOf(characterId))

            if (!res.success) {
                "获取数据失败:${res.message}"
                return@launchIO
            }

            currentCharacterDetailLoadingState = LoadingState.Success
        }
    }

    fun getPropertyNameById(key: String): CharacterDetailData.PropertyMapData? {
        val propertyMap = currentCharacterDetail?.property_map ?: return null

        return propertyMap[key]
    }


}