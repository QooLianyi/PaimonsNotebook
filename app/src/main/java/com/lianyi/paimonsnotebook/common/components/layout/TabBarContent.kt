package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor

@Composable
internal fun TabBarContent(
    tabs: Array<String> = arrayOf("选项A", "选项B", "选项C"),
    backgroundColor: Color = BackGroundColor,
    contentPadding: Dp = 12.dp,
    tabBarPadding: PaddingValues = PaddingValues(0.dp),
    onTabBarSelect: (Int) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(contentPadding)
    ) {
        TabBar(
            tabs = tabs,
            onSelect = onTabBarSelect,
            modifier = Modifier
                .padding(tabBarPadding)
                .height(45.dp)
        )
        content.invoke(this)
    }
}