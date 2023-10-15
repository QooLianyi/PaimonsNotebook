package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.media.FullScreenImage
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.ui.screen.home.components.post.PostStructureContentList
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.PostDetailViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class PostDetailScreen : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[PostDetailViewModel::class.java] }

    private val articleId by lazy {
        intent.getLongExtra(PostHelper.PARAM_POST_ID, 0L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadArticleContent(articleId)

        setContent {
            PaimonsNotebookTheme(this) {
                Crossfade(
                    targetState = viewModel.postLoadingState, modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor), label = ""
                ) {
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
                            Content()
                        }

                        LoadingState.Error -> {
                            ErrorPlaceholder("文章获取失败")
                        }

                        else -> {
                            EmptyPlaceholder()
                        }
                    }
                }

            }
        }
    }

    @Composable
    fun Content() {
        if (viewModel.postFullData != null) {
            PostStructureContentList(
                viewModel.postFullData!!,
                fontSize = 14.sp,
                onClickImage = viewModel::showFullScreenImage,
                onClickLink = viewModel::hyperlinkNavigate,
                onClickLinkCard = viewModel::hyperlinkNavigate,
                onClickVideo = viewModel::onClickVideo
            )
        }

        if (viewModel.showFullScreenImage) {
            FullScreenImage(
                url = viewModel.fullScreenImgUrl,
                onClick = viewModel::dismissFullScreenImage
            )
        }
    }
}