package com.lianyi.paimonsnotebook.ui.screen.items.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.util.compose.remember.rememberStatusBarHeightDp
import com.lianyi.paimonsnotebook.ui.theme.Black_60

/*
* 物品信息内容布局
* */
@Composable
internal fun ItemInformationContentLayout(
    imgUrl: String,
    enabledItemShadow:Boolean = false,
    itemBackgroundResId: Int = -1,
    itemImageContentScale: ContentScale = ContentScale.Crop,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val statusBarHeight = rememberStatusBarHeightDp()

        val imageHeight = remember {
            maxHeight * .6f + 49.dp + statusBarHeight
        }

        //背景
        Image(
            painter = painterResource(id = R.drawable.bg_character),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.FillHeight
        )

        if(itemBackgroundResId != -1){
            Image(
                painter = painterResource(id = itemBackgroundResId), contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                contentScale = ContentScale.FillHeight
            )
        }

        if(enabledItemShadow){
            NetworkImage(
                url = imgUrl,
                modifier = Modifier
                    .offset(y = 6.dp)
                    .fillMaxWidth()
                    .height(imageHeight)
                    .zIndex(0f),
                contentScale = itemImageContentScale,
                tint = Black_60
            )
        }

        NetworkImage(
            url = imgUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .zIndex(0f),
            contentScale = itemImageContentScale
        )

        content.invoke(this)
    }
}