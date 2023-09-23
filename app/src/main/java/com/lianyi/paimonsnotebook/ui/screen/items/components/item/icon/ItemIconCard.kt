package com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType

/*
* 物品图标
*
* 层级:背景 -> 图标
* */
@Composable
internal fun ItemIconCard(
    url: String,
    star: Int = 0,
    borderRadius:Dp = 0.dp,
    size: Dp = 70.dp
) {
    Box(
        modifier = Modifier
            .radius(borderRadius)
            .size(size)
    ) {

        Image(
            painter = painterResource(id = QualityType.getQualityBgByType(star)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size + 2.dp, size + 12.dp)
                .align(Alignment.Center)
        )

        NetworkImage(
            url = url,
            modifier = Modifier
                .size(size)
        )
    }
}