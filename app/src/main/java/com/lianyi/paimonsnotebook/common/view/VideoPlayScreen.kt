package com.lianyi.paimonsnotebook.common.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.media.VideoPlayer
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.viewmodel.VideoPlayViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class VideoPlayScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[VideoPlayViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        setContent {
            PaimonsNotebookTheme {
//                val uiController = rememberSystemUiController()
//                uiController.isNavigationBarVisible = false

                VideoPlayer(videoList = viewModel.videoList){
                    requestedOrientation = if(it){
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }else{
                        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    }
                }
            }
        }
    }

    private fun initData(){
        val json = intent.getStringExtra("video_list")
        if(json.isNullOrBlank()){
            "播放列表为空".show()
            finish()
        }
        viewModel.videoList.addAll(JSON.parseList(json!!))
        println(viewModel.videoList)
    }

}
