package com.lianyi.paimonsnotebook.common.components.components

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.get
import com.lianyi.paimonsnotebook.ui.theme.Black

@Composable
fun SlideExitBox(
    activity: Activity,
    content: @Composable () -> Unit
) {
    val screenXMax =
        remember { Resources.getSystem().displayMetrics.widthPixels.toFloat() }//屏幕宽px，转成float方便后续使用

    //关闭的百分比
    val closePerc = remember { 0.5f }
    //Activity的最终偏移目标
    var targetOffsetX by remember { mutableFloatStateOf(0f) }
    //Activity的偏移，动画
    val activityOffset = remember { Animatable(0f) }

    //是否正在被滑动
    var dragging by remember { mutableStateOf(false) }

    var finish by remember { mutableStateOf(false) }

    val decorView = remember {
        (activity.window.decorView as ViewGroup)
    }

    val screen = remember {
        decorView[0]
    }

    //更新状态
    val updateState: (Float) -> Unit = remember {
        {
            val alpha = (.85f - it / screenXMax).coerceAtLeast(0f).coerceAtMost(.8f)

            screen.x = it
            decorView.setBackgroundColor(Black.copy(alpha = alpha).toArgb())
        }
    }

    //监听最终目标的改变和滑动状态的改变
    LaunchedEffect(targetOffsetX, dragging) {
        if (finish) {
            activityOffset.animateTo(
                //最终目标为正，右滑出去，否则左滑出去
                if (targetOffsetX > 0) screenXMax else -screenXMax, tween(300)
            ) {
                updateState.invoke(this.value)
            }
            activity.finish()
            if (Build.VERSION.SDK_INT >= 34) {
                activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, 0,0)
            }else{
                activity.overridePendingTransition(0,0)
            }
        }

        if (dragging) {
            activityOffset.snapTo(targetOffsetX)
            updateState.invoke(targetOffsetX)
        } else {
            activityOffset.animateTo(targetOffsetX, tween(300)) {
                updateState.invoke(this.value)
            }
        }
    }

    Box(
        Modifier
            .draggable(
                rememberDraggableState { delta ->
                    //跟踪滑动偏移,限制了偏移方向为从左到右,且滑动距离最大为超出屏幕
                    targetOffsetX = (delta + targetOffsetX).coerceAtLeast(0f).coerceAtMost(screenXMax)
                },
                Orientation.Horizontal,//横向滑动
                onDragStarted = {
                    dragging = true
                },
                onDragStopped = {
                    dragging = false
                    if (targetOffsetX > closePerc * screenXMax) {
                        finish = true
                        return@draggable
                    } else {
                        finish = false
                    }
                    targetOffsetX = 0f
                }
            )
            .fillMaxSize()
    ) {
        content.invoke()
    }
}