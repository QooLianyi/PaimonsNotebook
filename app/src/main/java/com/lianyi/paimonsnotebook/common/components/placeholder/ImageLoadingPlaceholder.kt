package com.lianyi.paimonsnotebook.common.components.placeholder

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Gray_F5

/*
* 图片加载占位符
* */
@Composable
fun ImageLoadingPlaceholder() {

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .background(Gray_F5)
    ) {

        val anim = remember {
            Animatable(360f, Float.VectorConverter)
        }

        LaunchedEffect(Unit) {
            anim.animateTo(0f, infiniteRepeatable(tween(2000)))
        }

        Image(
            painter = painterResource(id = R.drawable.icon_klee_square), contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.Center)
                .graphicsLayer {
                    this.rotationY = anim.value
                }
        )

    }

}