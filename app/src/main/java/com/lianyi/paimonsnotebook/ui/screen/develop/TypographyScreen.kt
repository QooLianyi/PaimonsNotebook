package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.compose.setContent
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme


class TypographyScreen : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            PaimonsNotebookTheme {
            }

        }
    }
}