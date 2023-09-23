package com.lianyi.paimonsnotebook.common.components.media

import android.net.Uri
import android.widget.SeekBar
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.data.VideoData
import com.lianyi.paimonsnotebook.common.extension.modifier.action.doubleClick
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* 视频播放器
* videoList:视频列表
* autoPlay:当加载好后是否自动播放
* onVideoFullScreen:当进入/退出全屏时
* */
@Composable
fun VideoPlayer(
    videoList: List<VideoData>,
    autoPlay: Boolean = false,
    onVideoFullScreen: (Boolean) -> Unit = {},
) {
    if (videoList.isEmpty()) return

    val scope = rememberCoroutineScope()

    //是否全屏播放
    var videoFullScreen by remember {
        mutableStateOf(false)
    }
    //是否显示控制器
    var showController by remember {
        mutableStateOf(true)
    }
    //视频是否正在播放
    var videoPlay by remember {
        mutableStateOf(false)
    }

    //当前播放的视频
    var currentVideo by remember {
        mutableStateOf(videoList.first())
    }

    var videoView: VideoView? by remember {
        mutableStateOf(null)
    }

    //当前视频长度与播放进度
    var currentPosition by remember {
        mutableStateOf(0)
    }
    var videoDuration by remember {
        mutableStateOf(0)
    }
    var changeVideoUrl by remember {
        mutableStateOf(true)
    }

    //是否正在拖动进度条
    var dragProgressBar by remember {
        mutableStateOf(false)
    }

    //控制器组件尺寸，全屏下分辨率变化，需要对应的适配
    val controllerFontSize = remember(videoFullScreen) {
        if (videoFullScreen) 7.sp else 14.sp
    }
    val controllerIconSize = remember(videoFullScreen) {
        if (videoFullScreen) 16.dp else 32.dp
    }
    val controllerPadding = remember(videoFullScreen) {
        if (videoFullScreen) 2.dp else 4.dp
    }


    //清晰度选择
    var showDefinitionSelector by remember {
        mutableStateOf(false)
    }
    //清晰度选择偏移量
    val definitionSelectorOffset =
        animateDpAsState(targetValue = if (showDefinitionSelector) 0.dp else 100.dp)

    //进度条
    LaunchedEffect(videoPlay) {
        scope.launch {
            while (videoPlay) {
                delay(500)
                currentPosition = videoView?.currentPosition ?: 0
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Black)) {

        //VideoView
        AndroidView(factory = {
            VideoView(it).apply {
                //当视频加载完成时
                setOnPreparedListener {
                    videoDuration = it.duration
                }
                //视频播放完成
                setOnCompletionListener {
                    videoPlay = false
                }
                videoView = this
            }
        }, modifier = Modifier
            .fillMaxSize()
            .doubleClick({
                showController = !showController
            }, {
                videoPlay = !videoPlay
            })
        ) {

            if (changeVideoUrl) {
                it.setVideoURI(Uri.parse(currentVideo.url))
                changeVideoUrl = false
            }

            if (autoPlay) {
                it.start()
            }
            if (videoPlay) {
                it.start()
                currentPosition = it.currentPosition
            } else {
                it.pause()
            }
        }

        //控制器部分
        AnimatedVisibility(visible = showController,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {

                //下半部分
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Brush.verticalGradient(listOf(Transparent, Black_50), 0f, 100f))
                ) {

                    //进度条
                    AndroidView(factory = {
                        SeekBar(it).apply {
                            progressDrawable =
                                resources.getDrawable(R.drawable.video_player_progress_bar_style,
                                    null)
                            max = 1000
                            thumb = null
                            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(
                                    seekBar: SeekBar?,
                                    progress: Int,
                                    fromUser: Boolean,
                                ) {
                                    if (fromUser) {
                                        currentPosition =
                                            (videoDuration * (progress / 1000f)).toInt()
                                    }
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                    dragProgressBar = true
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                    videoView?.seekTo(currentPosition)
                                    dragProgressBar = false
                                }
                            })
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        if (!dragProgressBar) {
                            it.progress = ((currentPosition * 1f / videoDuration) * 1000).toInt()
                        }
                    }

                    //播放与进度文字
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = if (videoPlay) R.drawable.ic_pause else R.drawable.ic_play),
                                contentDescription = "播放",
                                modifier = Modifier
                                    .size(controllerIconSize)
                                    .clickable {
                                        videoPlay = !videoPlay
                                    })

                            Text(text = "${TimeHelper.timeParse(currentPosition)}/${
                                TimeHelper.timeParse(videoDuration)
                            }", color = White, fontSize = controllerFontSize)
                        }

                        //清晰度与全屏
                        Row {
                            Column(modifier = Modifier
                                .height(controllerIconSize)
                                .padding(controllerPadding)
                                .clip(RoundedCornerShape(2.dp))
                                .background(White_10)
                                .padding(controllerPadding, 0.dp)
                                .clickable {
                                    showDefinitionSelector = !showDefinitionSelector
                                },
                                verticalArrangement = Arrangement.Center) {
                                Text(text = currentVideo.label,
                                    color = White,
                                    fontSize = controllerFontSize,
                                    textAlign = TextAlign.Center)
                            }

                            Image(painter = painterResource(id = R.drawable.ic_fullscreen),
                                contentDescription = "全屏",
                                modifier = Modifier
                                    .size(controllerIconSize)
                                    .clickable {
                                        videoFullScreen = !videoFullScreen
                                        onVideoFullScreen(videoFullScreen)
                                    })
                        }
                    }
                }

                //视频清晰度选择
                Column(modifier = Modifier
                    .width(100.dp)
                    .offset(x = definitionSelectorOffset.value)
                    .align(Alignment.TopEnd)
                    .background(Black_30)
                ) {
                    LazyColumn {
                        itemsIndexed(videoList) { _: Int, videoData: VideoData ->
                            Text(text = videoData.label,
                                color = if (currentVideo.label == videoData.label) colorPrimary else White,
                                fontSize = controllerFontSize,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable {
                                        currentVideo = videoData
                                        changeVideoUrl = true
                                    }
                                    .fillMaxWidth()
                                    .padding(5.dp))
                        }
                    }
                }
            }
        }
    }
}