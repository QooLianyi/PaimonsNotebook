package com.lianyi.paimonsnotebook.ui.screen.achievement.components.achievement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black

/*
* 成就分类顶部操作按钮
*
* */
@Composable
fun AchievementActionButton(
    resId: Int,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier
            .radius(2.dp)
            .size(24.dp)
            .clickable {
                onClick.invoke()
            },
        tint = Black
    )
}