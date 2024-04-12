package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.media.FullScreenImage
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.home.components.post.PostStructureContentList
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.PostDetailViewModel
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

                ContentLoadingLayout(
                    loadingState = viewModel.postLoadingState,
                    loadingContent = {
                        ContentLoadingAnimationPlaceholder()
                    },
                    emptyContent = {
                        EmptyPlaceholder()
                    },
                    errorContent = {
                        ErrorPlaceholder("文章获取失败")
                    },
                    defaultContent = {
                        EmptyPlaceholder()
                    }
                ) {
                    Content()
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
                onClickVideo = viewModel::onClickVideo,
                onClickTag = viewModel::onClickTag
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