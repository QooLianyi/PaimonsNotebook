package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.layout.TabBarContent
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.page.AlreadyBindingAppWidgetPage
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.page.AppWidgetOverview
import com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel.AppWidgetScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AppWidgetScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AppWidgetScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()

        setContent {
            PaimonsNotebookTheme(this) {
                TabBarContent(
                    tabs = viewModel.tabs,
                    tabBarPadding = PaddingValues(12.dp,4.dp),
                    contentPadding = 0.dp,
                    onTabBarSelect = viewModel::changeTab
                ){
                    Crossfade(targetState = viewModel.currentTabIndex, label = "") {
                        when (it) {
                            0 -> {
                                AppWidgetOverview()
                            }

                            else -> {
                                if(viewModel.appWidgetBindingList.isEmpty()){
                                    EmptyPlaceholder("暂时没有已经绑定的小组件")
                                }else{
                                    AlreadyBindingAppWidgetPage(list = viewModel.appWidgetBindingList, onClick = viewModel::goAppWidgetConfigurationScreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
