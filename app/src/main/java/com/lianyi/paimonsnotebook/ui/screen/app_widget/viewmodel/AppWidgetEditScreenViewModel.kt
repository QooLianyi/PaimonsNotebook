package com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.popup.FloatPositionProvider
import com.lianyi.paimonsnotebook.common.extension.string.notify
import kotlin.math.abs

class AppWidgetEditScreenViewModel : ViewModel() {


    private var currentFloatPanelPosition = Offset(50f, 50f)

    var floatPositionProvider by mutableStateOf(FloatPositionProvider(currentFloatPanelPosition))
        private set

    //操作面板对齐方式
    var actionPanelAlignment by mutableStateOf(Alignment.TopEnd)
        private set

    //显示操作面板
    var showActionPanel by mutableStateOf(true)
        private set

    val appWidgetEditActionViewModel by lazy {
        AppWidgetEditActionViewModel(viewModelScope)
    }

    private var selectedAreaStartOffset = Offset.Zero

    private var selectedAreaEndOffset = Offset.Zero

    var selectedAreaOffset by mutableStateOf(Offset.Zero)
        private set

    var selectedAreaWidth by mutableFloatStateOf(0f)
        private set
    var selectedAreaHeight by mutableFloatStateOf(0f)
        private set

    fun onUpdateFloatPanelPosition(offset: Offset, scale: Float) {
        currentFloatPanelPosition += offset.let {
            Offset(it.x * scale, it.y * scale)
        }

        floatPositionProvider = FloatPositionProvider(currentFloatPanelPosition)
    }

    fun changeActionPanelAlignment() {
        actionPanelAlignment =
            if (actionPanelAlignment == Alignment.TopEnd) Alignment.TopStart else Alignment.TopEnd
    }

    fun toggleActionPanel() {
        showActionPanel = !showActionPanel
    }

    fun resetFloatMenu() {
        val offset = Offset(-currentFloatPanelPosition.x, -currentFloatPanelPosition.y + 24)
        onUpdateFloatPanelPosition(offset, 1f)

        "已重置悬浮菜单的状态".notify()
    }

    fun onStartDragSelectComponent(offset: Offset) {
        selectedAreaStartOffset = offset
        selectedAreaEndOffset = offset

        appWidgetEditActionViewModel.onStartDragSelectComponent(offset)
    }

    fun onEndDragSelectComponent() {
        appWidgetEditActionViewModel.onEndDragSelectComponent()

        selectedAreaStartOffset = Offset.Zero
        selectedAreaEndOffset = Offset.Zero

        selectedAreaOffset = Offset.Zero
        selectedAreaWidth = 0f
        selectedAreaHeight = 0f
    }

    fun onDraggingSelectComponent(pointerInputChange: PointerInputChange, offset: Offset) {
        selectedAreaEndOffset += offset

        val width = selectedAreaStartOffset.x - selectedAreaEndOffset.x
        val height = selectedAreaStartOffset.y - selectedAreaEndOffset.y

        selectedAreaOffset = when {
            height > 0 && width > 0 -> {
                selectedAreaEndOffset
            }

            width > 0 -> {
                Offset(selectedAreaEndOffset.x, selectedAreaStartOffset.y)
            }

            height > 0 -> {
                Offset(selectedAreaStartOffset.x, selectedAreaEndOffset.y)
            }

            else -> selectedAreaStartOffset
        }

        selectedAreaWidth = abs(width)
        selectedAreaHeight = abs(height)

        appWidgetEditActionViewModel.onDraggingSelectComponent(
            selectedAreaOffset,
            selectedAreaWidth,
            selectedAreaHeight
        )
    }

    fun onDoubleClickBackground(offset: Offset) {
        appWidgetEditActionViewModel.onDoubleClickBackground(offset)
    }
}