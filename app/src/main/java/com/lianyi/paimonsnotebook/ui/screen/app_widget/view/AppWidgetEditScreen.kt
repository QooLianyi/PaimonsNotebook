package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.app_widget.AppWidgetComponent
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup.AppWidgetEditActionPanel
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.widget.SelectedArea
import com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel.AppWidgetEditScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White

class AppWidgetEditScreen : BaseActivity(
    enableSensor = false,
    initOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
) {

    private val viewModel by lazy {
        ViewModelProvider(this)[AppWidgetEditScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme {
                val focusManager = LocalFocusManager.current

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor),
                ) {

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = viewModel::onStartDragSelectComponent,
                                onDragEnd = viewModel::onEndDragSelectComponent,
                                onDrag = viewModel::onDraggingSelectComponent
                            )
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = viewModel::onDoubleClickBackground)
                        }
                    )

                    Box(
                        modifier = Modifier
                            .radius(8.dp)
                            .size(160.dp, 80.dp)
                            .background(White)
                            .align(Alignment.Center)
                    )

                    viewModel.appWidgetEditActionViewModel.currentEditData.components.forEach {
                        AppWidgetComponent(
                            component = it,
                            onClick = viewModel.appWidgetEditActionViewModel::onClickComponent,
                            onDrag = viewModel.appWidgetEditActionViewModel::onDragComponent
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_circle_empty),
                            contentDescription = null,
                            modifier = Modifier
                                .radius(4.dp)
                                .size(24.dp)
                                .clickable {
                                    focusManager.clearFocus()
                                }
                        )

                        NavigationBarPaddingSpacer()
                    }

                    SelectedArea(
                        offset = viewModel.selectedAreaOffset,
                        width = viewModel.selectedAreaWidth,
                        height = viewModel.selectedAreaHeight
                    )

//                    AppWidgetFloatComponentInfoPanel(
//                        popupProvider = viewModel.floatPositionProvider,
//                        onDrag = viewModel::onUpdateFloatPanelPosition
//                    )

                    AppWidgetEditActionPanel(
                        viewModel = viewModel.appWidgetEditActionViewModel
                    )
                }
            }
        }
    }
}