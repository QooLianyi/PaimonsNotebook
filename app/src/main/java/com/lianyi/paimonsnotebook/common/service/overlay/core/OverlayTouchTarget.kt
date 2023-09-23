package com.lianyi.paimonsnotebook.common.service.overlay.core

import android.view.View
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/*
* 悬浮窗触摸事件目标
*
* viewHolder:需要设置触摸事件的viewHolder
* state:触摸事件进行时更新的目标状态
* manager:窗体管理器
* keepSide:保持贴边,开启后会保持贴边
* contentSlot:内容插槽,仅用于自定义控制器的样式
* onClick:点击事件回调
* */
@Composable
fun OverlayTouchTarget(
    viewHolder: OverlayViewHolder,
    state: OverlayState,
    manager: WindowManager,
    keepSide: Boolean = true,
    contentSize: Int,
    contentSlot: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val radius = remember {
        contentSize / 2f
    }

    val keepSideAnim = remember {
        Animatable(0f)
    }
    val controllerAlphaAnim = remember {
        Animatable(0f)
    }

    //监测偏移动画值的变化,并不断的修改控制器偏移信息,初始偏移量的设置也在此处进行
    LaunchedEffect(keepSideAnim.value, state.showController) {
        if (state.showController) {
            state.controllerOffset =
                IntOffset(keepSideAnim.value.roundToInt(), state.controllerOffset.y)
            updateViewLayout(manager, viewHolder, state)
        }
    }

    LaunchedEffect(state.showController) {
        if (!state.showController) {
            state.showContent = false
            controllerAlphaAnim.animateTo(0f)
            updateViewLayout(manager, viewHolder, state, View.GONE)
        } else if (state.showController && controllerAlphaAnim.value != 1f) {
            controllerAlphaAnim.animateTo(1f)
        }
    }

    AnimatedVisibility(
        visible = controllerAlphaAnim.value > 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier
            .alpha(controllerAlphaAnim.value)
            .pointerInput(Unit) {
                detectDragGestures(onDrag = { _, dragAmount ->
                    val dragAmountOffset =
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())

                    val x = (state.controllerOffset.x + dragAmountOffset.x)
                        .coerceAtLeast(0)
                        .coerceAtMost(state.overlayWidthPx - radius.roundToInt())
                    val y = (state.controllerOffset.y + dragAmountOffset.y)
                        .coerceAtLeast(0)
                        .coerceAtMost(state.overlayHeightPx - contentSize)

                    state.controllerOffset = IntOffset(x, y)

                    //防止在消失时更新ui导致布局还留在界面上
                    if(controllerAlphaAnim.value == 1f){
                        updateViewLayout(manager, viewHolder, state)
                    }
                }, onDragEnd = {
                    if (keepSide) {
                        val mid = state.overlayWidthPx / 2
                        val xPos = state.controllerOffset.x + radius

                        scope.launch {
                            val target = if (xPos > mid) {
                                state.overlayWidthPx - radius
                            } else {
                                0f
                            }
                            keepSideAnim.snapTo(state.controllerOffset.x.toFloat())
                            keepSideAnim.animateTo(target, spring())
                        }
                    }
                })
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onClick.invoke()
                })
            }
        ) {
            contentSlot()
        }
    }
}

private fun updateViewLayout(
    manager: WindowManager,
    viewHolder: OverlayViewHolder,
    state: OverlayState,
    visibility: Int = View.VISIBLE,
) {
    viewHolder.params.x = state.controllerOffset.x
    viewHolder.params.y = state.controllerOffset.y
    viewHolder.view.visibility = visibility
    manager.updateViewLayout(viewHolder.view, viewHolder.params)
}