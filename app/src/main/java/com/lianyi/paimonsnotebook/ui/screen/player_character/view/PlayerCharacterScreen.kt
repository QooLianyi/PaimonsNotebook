package com.lianyi.paimonsnotebook.ui.screen.player_character.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel.PlayerCharacterScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class PlayerCharacterScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerCharacterScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {

            }
        }
    }
}