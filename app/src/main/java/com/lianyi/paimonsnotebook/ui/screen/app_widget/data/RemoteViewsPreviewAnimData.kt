package com.lianyi.paimonsnotebook.ui.screen.app_widget.data

import androidx.compose.animation.Animatable
import androidx.compose.ui.graphics.Color
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

class RemoteViewsPreviewAnimData(
    textColor: Color = Black,
    backgroundColor: Color = White,
    imageTintColor: Color = Black,
    backgroundRadius: Float = 0f
) {
    val textColor by lazy {
        Animatable(textColor)
    }
    val backgroundColor by lazy {
        Animatable(backgroundColor)
    }
    val imageTintColor by lazy {
        Animatable(imageTintColor)
    }

    val backgroundRadius by lazy {
        androidx.compose.animation.core.Animatable(backgroundRadius)
    }

    suspend fun changeTextColor(color: Color) {
        textColor.animateTo(color)
    }

    suspend fun changeBackgroundColor(color: Color) {
        backgroundColor.animateTo(color)
    }

    suspend fun changeImageTintColor(color: Color) {
        imageTintColor.animateTo(color)
    }

    suspend fun changeBackgroundRadius(radius:Float) {
        backgroundRadius.animateTo(radius)
    }

}