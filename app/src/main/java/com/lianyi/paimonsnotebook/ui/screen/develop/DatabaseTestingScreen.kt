package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class DatabaseTestingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme {
            }
        }
    }
}
