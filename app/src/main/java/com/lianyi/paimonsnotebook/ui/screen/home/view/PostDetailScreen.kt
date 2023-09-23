package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.content.Intent
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
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.view.WebViewScreen
import com.lianyi.paimonsnotebook.ui.screen.home.components.post.PostStructureContentList
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.PostDetailViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class PostDetailScreen : BaseActivity() {
    companion object {
        const val PostId = "postId"
        const val WebStaticUrl = "webstaticUrl"
    }

    private val viewModel by lazy { ViewModelProvider(this)[PostDetailViewModel::class.java] }

    private val webstaticUrl by lazy {
        intent.getStringExtra(WebStaticUrl)
    }
    private val webstatic get() = !webstaticUrl.isNullOrBlank()

    private val articleId by lazy {
        intent.getLongExtra(PostId, 0L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (webstatic) {
            //todo 更改为使用js bridge的webview并设置为默认用户的cookie
            startActivity(Intent(this, WebViewScreen::class.java).apply {
                putExtra("url", webstaticUrl)
            })
            finish()
        } else {
            viewModel.loadArticleContent(articleId)
        }

        setContent {

            PaimonsNotebookTheme(this) {
                Crossfade(
                    targetState = viewModel.postLoadingState, modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
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

        if (viewModel.postFullData != null) {
            PostStructureContentList(viewModel.postFullData!!, fontSize = 14.sp)
        }
    }
}