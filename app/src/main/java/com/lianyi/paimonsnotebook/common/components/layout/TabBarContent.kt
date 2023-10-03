package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Primary_2

@Composable
internal fun TabBarContent(
    tabs: Array<String> = arrayOf("选项A", "选项B", "选项C"),
    tabBarHeight: Dp = 45.dp,
    backgroundColor: Color = BackGroundColor,
    indicatorColor: Color = Primary_2,
    contentPadding: PaddingValues = PaddingValues(
        start = 12.dp,
        end = 12.dp,
        top = 6.dp,
        bottom = 0.dp
    ),
    tabBarPadding: PaddingValues = PaddingValues(0.dp),
    onTabBarSelect: (Int) -> Unit,
    statusBarEnabled: Boolean = true,
    topSlot: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
            )
    ) {
        if (statusBarEnabled) {
            StatusBarPaddingSpacer()
        }

        Row(Modifier.height(tabBarHeight)) {
            TabBar(
                tabs = tabs,
                onSelect = onTabBarSelect,
                modifier = Modifier
                    .padding(tabBarPadding)
                    .fillMaxHeight(),
                indicatorColor = indicatorColor
            )
            topSlot.invoke(this@Row)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            content.invoke(this)
        }
    }
}