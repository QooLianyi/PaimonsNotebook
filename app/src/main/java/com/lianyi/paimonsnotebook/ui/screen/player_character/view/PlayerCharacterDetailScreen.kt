package com.lianyi.paimonsnotebook.ui.screen.player_character.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel.PlayerCharacterDetailScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class PlayerCharacterDetailScreen : BaseActivity() {

    companion object {
        //当前角色与uid
        const val PARAM_USER_AND_UID_JSON = "user_and_uid_json"

        //全部角色的列表集合
        const val PARAM_CHARACTER_LIST_JSON = "character_list_json"

        //选中的角色列表集合
        const val PARAM_SELECTED_CHARACTER_LIST_JSON = "selected_character_list_json"
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerCharacterDetailScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent) {
            finish()
        }

        setContent {
            PaimonsNotebookTheme(this) {

                ItemScreenLoadingState(loadingState = viewModel.loadingState) {
//                    ItemInformationContentLayout(
//
//                    )
                }
            }
        }
    }
}