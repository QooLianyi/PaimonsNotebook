package com.lianyi.paimonsnotebook.common.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.media.VideoPlayer
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.viewmodel.VideoPlayViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class VideoPlayScreen : BaseActivity(enableGesture = false) {

    private val viewModel by lazy {
        ViewModelProvider(this)[VideoPlayViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent)

        setContent {
            PaimonsNotebookTheme(hideStatusBar = true, hideNavigationBar = true) {
                VideoPlayer(
                    videoList = viewModel.videoList,
                    videoFullScreen = viewModel.fullScreen,
                    onVideoExit = {
                        viewModel.onVideoExit {
                            finish()
                        }
                        setOrientation()
                    }
                ) {
                    viewModel.fullScreen = it
                    setOrientation()
                }
            }
        }
    }

    private fun setOrientation() {
        requestedOrientation = if (viewModel.fullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
