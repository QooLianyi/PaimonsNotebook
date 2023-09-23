package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.lianyi.paimonsnotebook.common.extension.modifier.action.doubleClick
import com.lianyi.paimonsnotebook.common.extension.modifier.action.pointerInputDetectTransformGestures
import com.lianyi.paimonsnotebook.ui.theme.Black_60

@Composable
fun FullScreenImage(
    url: String,
    backgroundColor: Color = Black_60,
    onClick: () -> Unit = {},
) {
    var scale by remember {
        mutableStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val alphaAnim = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit){
        alphaAnim.animateTo(1f, tween(300))
    }

    Box(modifier = Modifier
        .alpha(alphaAnim.value)
        .fillMaxSize()
        .background(backgroundColor)
        .pointerInputDetectTransformGestures(
            isTransformInProgressChanged = {

            }) { _, pan, zoom, _ ->
            offset += pan
            scale = (zoom * scale).coerceAtLeast(1f)
        }
        .doubleClick({
            onClick()
        }, {
            scale = 1f
            offset = Offset.Zero
        }),
        contentAlignment = Alignment.Center
    ) {
        NetworkImage(url = url, modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            ), contentScale = ContentScale.FillWidth)
    }
}