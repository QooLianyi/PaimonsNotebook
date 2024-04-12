package com.lianyi.paimonsnotebook.common.components.layout.column

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor

/*
* 顶部tab垂直布局
* */
@Composable
internal fun TabBarColumnLayout(
    tabs: Array<String>,
    backgroundColor: Color = BackGroundColor,
    tabBarPaddingVertical: Dp = 4.dp,
    tabBarPaddingHorizontal: Dp = 12.dp,
    onTabBarSelect: (Int) -> Unit,
    statusBarEnabled: Boolean = true,
    topSlot: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    TopSlotColumnLayout(
        topSlot = {
            TabBar(
                tabs = tabs,
                onSelect = onTabBarSelect,
                tabBarPadding = PaddingValues(tabBarPaddingHorizontal, tabBarPaddingVertical)
            )
            topSlot.invoke(this)
        },
        backgroundColor = backgroundColor,
        statusBarEnabled = statusBarEnabled,
        content = content
    )
}