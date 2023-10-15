package com.lianyi.paimonsnotebook.common.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData

class VideoPlayViewModel : ViewModel() {
    private lateinit var vod: PostFullData.Post.Vod

    val videoList = mutableStateListOf<PostFullData.Post.Vod.Resolution>()

    var fullScreen by mutableStateOf(false)

    fun init(intent: Intent) {
        val json = intent.getStringExtra("video_list")
        if(json.isNullOrBlank()){
            "播放列表为空".errorNotify()
            return
        }
        vod = JSON.parse(json)

        videoList += vod.resolutions
    }

    fun onVideoExit(finish:()->Unit){
        if(fullScreen){
            fullScreen = false
            return
        }
        finish.invoke()
    }
}