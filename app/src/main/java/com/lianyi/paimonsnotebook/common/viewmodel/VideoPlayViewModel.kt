package com.lianyi.paimonsnotebook.common.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lianyi.paimonsnotebook.common.data.VideoData

class VideoPlayViewModel:ViewModel() {

    val videoUri by lazy {
        println("video = 加载")
        Uri.parse("https://player.bilibili.com/player.html?aid=261237889&bvid=BV14e411j7Fv")
    }

    var videoList = mutableStateListOf<VideoData>()

}