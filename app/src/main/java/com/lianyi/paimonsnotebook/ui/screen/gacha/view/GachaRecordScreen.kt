package com.lianyi.paimonsnotebook.ui.screen.gacha.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPagePlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.widget.button.TitleAndDescriptionActionButton
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.GachaItemsPage
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.GachaRecordOverviewPage
import com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel.GachaRecordScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class GachaRecordScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[GachaRecordScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {

        ContentLoadingLayout(
            loadingState = viewModel.loadingState,
            loadingContent = {
                ContentLoadingPlaceholder()
            },
            emptyContent = {
                EmptyPagePlaceholder(title = "当前没有祈愿数据") {
                    TitleAndDescriptionActionButton(
                        title = "前往祈愿记录设置页",
                        description = "前往祈愿记录设置界面选择获取祈愿记录的方式",
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .fillMaxWidth(),
                        onClick = viewModel::goOptionScreen
                    )
                }
            },
            errorContent = {
                ErrorPlaceholder {
                }
            },
            defaultContent = {

            }
        ) {
            TabBarColumnLayout(
                onTabBarSelect = viewModel::setSelectedPageIndex,
                tabs = viewModel.tabs,
                topSlot = {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_navigation),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .radius(3.dp)
                            .size(32.dp)
                            .clickable {
                                viewModel.goOptionScreen()
                            }
                            .padding(4.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                }
            ) {
                Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                    when (it) {
                        1 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Avatar)
                        2 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Weapon)
                        else -> {
                            if (viewModel.gachaRecordOverview != null) {
                                GachaRecordOverviewPage(
                                    viewModel.gachaRecordOverview!!,
                                    viewModel.overviewItemMap
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}