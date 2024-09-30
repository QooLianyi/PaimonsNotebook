package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
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
                TabBarColumnLayout(
                    tabs = viewModel.tabs,
                    onTabBarSelect = viewModel::changeTab,
                ){
                    Crossfade(targetState = viewModel.currentTabIndex, label = "") {
                        when (it) {
                            0 -> {
                                AppWidgetOverview(viewModel.enableMetadata)
                            }

                            else -> {
                                if(viewModel.appWidgetBindingList.isEmpty()){
                                    EmptyPlaceholder("暂时没有已创建的桌面组件")
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
