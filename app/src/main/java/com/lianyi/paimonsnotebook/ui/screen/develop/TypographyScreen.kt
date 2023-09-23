package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note_widget.DailyNoteWidget2X1RemoteViewsPreview

class TypographyScreen : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor),
                    contentAlignment = Alignment.Center
                ) {
                }
            }
        }
    }
}