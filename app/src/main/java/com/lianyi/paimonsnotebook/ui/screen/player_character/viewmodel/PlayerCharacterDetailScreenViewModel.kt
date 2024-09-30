package com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.ui.screen.player_character.view.PlayerCharacterDetailScreen

class PlayerCharacterDetailScreenViewModel : ViewModel() {

    var loadingState by mutableStateOf(LoadingState.Loading)
        private set

    private lateinit var userAndUid: UserAndUid
    private lateinit var characterList: List<Int>

    var currentIndex by mutableIntStateOf(0)

    var currentCharacterDetail by mutableStateOf<CharacterDetailData?>(null)
        private set

    private val avatarService by lazy {
        AvatarService(onMissingFile = this::onMissingFile)
    }
    
    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    fun init(intent: Intent?, onInitFail: () -> Unit) {
        if (intent == null) {
            "参数为空".errorNotify()
            onInitFail.invoke()
            return
        }
        val characterListJson =
            intent.getStringExtra(PlayerCharacterDetailScreen.PARAM_CHARACTER_LIST_JSON)
                ?: JSON.EMPTY_LIST

        val selectedCharacterListJson =
            intent.getStringExtra(PlayerCharacterDetailScreen.PARAM_SELECTED_CHARACTER_LIST_JSON)
                ?: JSON.EMPTY_LIST

        val playerUidJson =
            intent.getStringExtra(PlayerCharacterDetailScreen.PARAM_USER_AND_UID_JSON)
                ?: JSON.EMPTY_OBJ

        if (characterListJson == JSON.EMPTY_LIST || selectedCharacterListJson == JSON.EMPTY_LIST || playerUidJson == JSON.EMPTY_OBJ) {
            "缺少参数".errorNotify()
            onInitFail.invoke()
            return
        }

        val selectedCharacterList = JSON.parse<List<Int>>(
            selectedCharacterListJson,
            getParameterizedType(List::class.java, Int::class.java)
        )
        characterList = JSON.parse<List<Int>>(
            characterListJson,
            getParameterizedType(List::class.java, Int::class.java)
        )

        userAndUid = JSON.parse(playerUidJson)

        if (selectedCharacterList.isEmpty() || characterList.isEmpty()) {
            "角色列表为空或未选择角色".errorNotify()
            onInitFail.invoke()
            return
        }

        viewModelScope.launchIO {
            getCharacterList()
        }
    }

    private suspend fun getCharacterDetail(characterIds: List<Int>) {
        val res = gameRecordClient.getCharacterDetail(userAndUid,characterIds)
        if(!res.success){
            "获取角色数据失败:${res.message}"
            loadingState = LoadingState.Error
            return
        }

        currentCharacterDetail = res.data

        loadingState = LoadingState.Success
    }

    private suspend fun getCharacterList() {


    }

}
