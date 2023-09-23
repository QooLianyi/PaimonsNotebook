package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.IconTextHintSlotItem
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel.AppWidgetBindingScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Gray_Dark
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper

class AppWidgetBindingScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AppWidgetBindingScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.appwidgetId = intent.getIntExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, -1)
        viewModel.appwidgetClassName =
            intent.getStringExtra(AppWidgetHelper.PARAM_APPWIDGET_CLASS_NAME)

        setContent {
            PaimonsNotebookTheme {
                if (viewModel.hasUser) {
                    if (viewModel.appwidgetId == null || viewModel.appwidgetClassName == null) {
                        ErrorPlaceholder(
                            """
                            缺少必要的参数
                            appwidgetId=:[${viewModel.appwidgetId}]
                            appwidgetType:[${viewModel.appwidgetClassName}]
                        """.trimIndent()
                        )
                    } else {
                        UserGameRolesDialog(
                            onButtonClick = {
                                goSystemHome()
                            },
                            onDismissRequest = {
                                goSystemHome()
                            },
                            onSelectRole = { user, role ->
                                viewModel.bindAppwidget(user, role)
                                goSystemHome()
                            }
                        )
                    }
                } else {
                    Box {
                        ErrorPlaceholder("没有可用的账号")
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            IconTextHintSlotItem(
                                title = "登录账号",
                                description = "前往账号管理界面添加一个账号以使用小组件",
                                backGroundColor = CardBackGroundColor_Gray_Dark,
                                onClick = {
                                    viewModel.goAccountManagerScreen()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            NavigationPaddingSpacer()
                        }
                    }
                }
            }
        }
    }

    private fun goSystemHome() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}
