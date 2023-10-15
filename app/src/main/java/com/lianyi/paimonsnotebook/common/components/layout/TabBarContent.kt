package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor

@Composable
internal fun TabBarContent(
    tabs: Array<String>,
    backgroundColor: Color = BackGroundColor,
    tabBarPaddingVertical: Dp = 4.dp,
    tabBarPaddingHorizontal: Dp = 12.dp,
    onTabBarSelect: (Int) -> Unit,
    statusBarEnabled: Boolean = true,
    topSlot: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (statusBarEnabled) {
            StatusBarPaddingSpacer()
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabBar(
                tabs = tabs,
                onSelect = onTabBarSelect,
                tabBarPadding = PaddingValues(tabBarPaddingHorizontal, tabBarPaddingVertical)
            )
            topSlot.invoke(this@Row)
        }

        content.invoke(this)
    }
}