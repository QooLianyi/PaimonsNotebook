package com.lianyi.paimonsnotebook.ui.screen.develop.template.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.develop.template.viewmodel.ScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class Screen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {

            }
        }
    }
}