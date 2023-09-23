package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.layout.HtmlTextLazyColumn
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.DividerText
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.time.TimeStampType
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.PostDetailViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AnnouncementScreen : ComponentActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[PostDetailViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            PaimonsNotebookTheme {

                Crossfade(targetState = viewModel.postLoadingState) {
                    when (it) {
                        LoadingState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingAnimationPlaceholder()
                            }
                        }

                        LoadingState.Success -> {
                            LoadingSuccessScreen()
                        }

                        LoadingState.Error -> {
                            ErrorPlaceholder("文章获取失败")
                        }

                        else -> {
                            TitleText(text = "默认", fontSize = 18.sp)
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun LoadingSuccessScreen() {
        viewModel.postFullData?.post?.post?.apply {

            HtmlTextLazyColumn(
                htmlText = content,
                videoCover = viewModel.videoCover,
                activity = this@AnnouncementScreen,
                onVideoClick = {
                    viewModel.playVideo(this@AnnouncementScreen)
                },
                onHyperlinkClick = {
                    viewModel.hyperlinkNavigate(this@AnnouncementScreen, it)
                },
                diskCacheTemplate = viewModel.getHtmlImageDiskCacheData(this)
            ) {
                TitleText(text = subject, fontSize = 18.sp)

                DividerText(
                    text = "文章发表:${
                        TimeHelper.getTime(
                            created_at.toLong() * 1000L,
                            TimeStampType.MM_DD
                        ).replace(" ", "-")
                    }", modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}