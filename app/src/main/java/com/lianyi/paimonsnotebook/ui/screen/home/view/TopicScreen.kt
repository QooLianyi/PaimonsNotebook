package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.InfoText
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.util.enums.ViewModelAction
import com.lianyi.paimonsnotebook.ui.screen.home.components.post.TopicPostItem
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.TopicScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

/*
* 主题界面
* */
class TopicScreen : BaseActivity() {

    companion object {
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TopicScreenViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchIO {
            viewModel.viewModelActionFlow.collect {
                if (it == ViewModelAction.Finish) {
                    this@TopicScreen.finish()
                }
            }
        }

        viewModel.update(intent)

        setContent {
            PaimonsNotebookTheme(this) {

                ContentLoadingLayout(
                    loadingState = viewModel.loadingState,
                    loadingContent = {
                        ContentLoadingAnimationPlaceholder()
                    },
                    emptyContent = {
                        EmptyPlaceholder()
                    },
                    errorContent = {
                        ErrorPlaceholder {}
                    },
                    defaultContent = {

                    }
                ) {

                    ContentSpacerLazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackGroundColor),
                        contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {

                            Row(
                                modifier = Modifier
                                    .zIndex(2f)
                                    .offset(y = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                NetworkImage(
                                    url = viewModel.topicInfo?.topic?.cover ?: "",
                                    modifier = Modifier.size(52.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    PrimaryText(text = "${viewModel.topicInfo?.topic?.name}")

                                    Spacer(modifier = Modifier.height(4.dp))

                                    InfoText(text = "${viewModel.topicInfo?.topic?.desc}")
                                }
                            }
                        }

                        stickyHeader {

                            Box(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .fillMaxWidth()
                                    .background(BackGroundColor)
                                    .statusBarsPadding()
                            ) {
                                TabBar(
                                    tabs = viewModel.tabs,
                                    onSelect = viewModel::onTabBarSelect,
                                    tabBarPadding = PaddingValues(horizontal = 16.dp, 8.dp),
                                    textUnSelectSize = 12.sp,
                                    textSelectSize = 14.sp,
                                    index = viewModel.currentIndex
                                )
                            }
                        }

                        if (viewModel.getCurrentList().isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EmptyPlaceholder("这里暂时没有内容,稍后再来看看吧!")
                                }
                            }
                        } else {
                            items(viewModel.getCurrentList()) { item ->
                                TopicPostItem(
                                    item = item,
                                    onClickPostListItem = viewModel::onClickPostListItem,
                                    onClickTag = viewModel::onClickTag
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}