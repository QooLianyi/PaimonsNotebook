package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AppWidgetRedirectScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme {
            }
        }
    }
}
