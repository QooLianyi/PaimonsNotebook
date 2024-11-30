package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.placeholder.TextPlaceholder
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.panel.AppWidgetEditTransformItem
import com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel.AppWidgetEditActionViewModel
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.ClickableInformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_50
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Warning
import com.lianyi.paimonsnotebook.ui.theme.Warning_1
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_50

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoxScope.AppWidgetEditActionPanel(
    viewModel: AppWidgetEditActionViewModel,
//    onClickBindingFieldButton:()->Unit
) {
    val focusManager = LocalFocusManager.current

    var show by remember {
        mutableStateOf(true)
    }

    var alignment by remember {
        mutableStateOf(Alignment.TopEnd)
    }

    val offsetXAnim by animateDpAsState(
        targetValue = (if (show) 0.dp else 154.dp) * if (alignment == Alignment.TopEnd) 1 else -1,
        label = ""
    )

    val panelWidth = 190.dp

    ContentSpacerLazyColumn(modifier = Modifier
        .align(alignment)
        .offset(x = offsetXAnim)
        .width(190.dp)
        .fillMaxHeight()
        .background(White)
        .pointerInput(Unit) {}
        .then(if (!show) Modifier.clickable {
            show = true
            focusManager.clearFocus()
        } else Modifier),
        contentPadding = PaddingValues(if (show) 12.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (!show) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(36.dp)
                ) {
                    TextPlaceholder("内容简述")
                }
            }
        }

        if (!show) return@ContentSpacerLazyColumn

        item {
            InfoText(text = "面板操作")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_fullscreen),
                    contentDescription = null,
                    modifier = Modifier
                        .radius(4.dp)
                        .size(24.dp)
                        .clickable {
                            show = false
                        },
                    tint = Black
                )
                NoRippleThemeProvides {
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                alignment = Alignment.TopStart
                            })
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                alignment = Alignment.TopEnd
                            })
                }

                Icon(painter = painterResource(id = R.drawable.ic_border_outline),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {})

                Icon(painter = painterResource(id = R.drawable.ic_save_outline),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {})

            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                InfoText(text = "历史")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    viewModel.historyActionButtons.forEachIndexed { index, pair ->
                        val background = if (index == 0) {
                            if (viewModel.enableHistoryPrev) Warning else Warning_1
                        } else {
                            if (viewModel.enableHistoryNext) Warning else Warning_1
                        }

                        val tint = if (index == 0) {
                            if (viewModel.enableHistoryPrev) White else White_50
                        } else {
                            if (viewModel.enableHistoryNext) White else White_50
                        }

                        ClickableInformationItem(iconResId = pair.second,
                            text = pair.first,
                            backgroundColor = background,
                            tint = tint,
                            textColor = tint,
                            paddingValues = PaddingValues(
                                start = 8.dp, top = 4.dp, bottom = 4.dp, end = 12.dp
                            ),
                            onClick = {
                                if (index == 0 && viewModel.enableHistoryPrev) {
                                    viewModel.historyPrev()
                                }
                                if (index != 0 && viewModel.enableHistoryNext) {
                                    viewModel.historyNext()
                                }
                            })
                    }

                }
            }
        }


        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                InfoText(text = "组件操作")

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    viewModel.componentActionButtons.forEach {
                        var size = IntSize.Zero
                        var offset = Offset.Zero
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                viewModel.onClickActionButton(it.second, size, offset)
                            }
                            .onGloballyPositioned {
                                size = it.size
                                offset = it.positionInRoot()
                            }
                        ) {
                            InformationItem(
                                iconResId = it.second,
                                text = it.first,
                                backgroundColor = it.third,
                                tint = White,
                                textColor = White,
                                paddingValues = PaddingValues(
                                    start = 8.dp, top = 4.dp, bottom = 4.dp, end = 12.dp
                                )
                            )
                        }
                    }
                }
            }
        }


        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                InfoText(text = "对齐")

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    viewModel.alignButtons.forEach {
                        Icon(painter = painterResource(id = it.first),
                            contentDescription = null,
                            modifier = Modifier
                                .radius(2.dp)
                                .size(26.dp)
                                .then(if (viewModel.enableAlignButton) Modifier.clickable {
                                    viewModel.onAlignComponent(it.second)
                                } else Modifier),
                            tint = if (viewModel.enableAlignButton) Black else Black_50)
                    }
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoText(text = "变换")

                    Icon(
                        painter = painterResource(
                            id =
                            when (viewModel.currentEditData.getUnLockedComponentCount()) {
                                0 -> R.drawable.ic_unlock_outline
                                viewModel.currentEditData.components.size -> R.drawable.ic_lock_full
                                else -> R.drawable.ic_lock_outline
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(20.dp)
                            .clickable {
                                viewModel.onClickLockButton()
                            },
                        tint = Black
                    )
                }

                viewModel.transformValueConfigMap.keys.forEach {
                    AppWidgetEditTransformItem(
                        name = it,
                        getValueByName = viewModel::getTransformShowValueByName,
                        onValueChange = viewModel::onComponentTransform,
                        onDrag = viewModel::onDragEditTransformItem
                    )
                }
            }
        }


        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                InfoText(text = "文本")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    viewModel.textStyleButtons.forEach {
                        Icon(painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier
                                .radius(2.dp)
                                .size(24.dp)
                                .clickable {
                                    viewModel.onClickTextStyle(it)
                                })
                    }
                }

                if (viewModel.selectedComponents.size == 1 && viewModel.selectedComponents.first().text != null) {
                    val component = viewModel.selectedComponents.first()
                    val text = component.text!!
                    val data = text.list

                    data.forEachIndexed { index, item ->

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            InputTextFiled(
                                modifier = Modifier.weight(1f),
                                value = if(item.enabled) item.value else item.key,
                                onValueChange = { newValue ->
                                    viewModel.onChangeTextInputValue(text, newValue, index, item)
                                },
                                textStyle = TextStyle(fontSize = 12.sp),
                                inputFieldHeight = 24.dp,
                                inputFieldMaxHeight = 240.dp,
                                padding = PaddingValues(2.dp),
                                contentAlignment = Alignment.CenterStart,
                                maxLines = 10,
                                enabled = item.enabled
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_url_1),
                                contentDescription = null,
                                tint = if (item.enabled) Black else Primary,
                                modifier = Modifier
                                    .radius(2.dp)
                                    .size(24.dp)
                                    .clickable {
                                        viewModel.onBindingField(component, index)
                                    }
                                    .padding(4.dp)
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier
                                    .radius(2.dp)
                                    .size(24.dp)
                                    .clickable {
                                        viewModel.onDeleteBindingField(component, index)
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ClickableInformationItem(
                            iconResId = R.drawable.ic_add,
                            text = "添加文本",
                            backgroundColor = Primary,
                            tint = White,
                            textColor = White,
                            onClick = viewModel::onAddTextComponentValue
                        )
                    }

                }

                viewModel.textValueConfigMap.keys.forEach {
                    AppWidgetEditTransformItem(
                        name = it,
                        getValueByName = viewModel::getTransformShowValueByName,
                        onValueChange = viewModel::onComponentTransform,
                        onDrag = viewModel::onDragEditTransformItem
                    )
                }

                PrimaryText(text = "时间格式")
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InfoText(text = "99小时99分钟")
                    InfoText(text = "明天 16:00后")
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoText(text = "图片")

                Row {
                    Image(painter = painterResource(id = R.drawable.bg_default_name_card),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(4.dp)
                            .size(36.dp)
                            .clickable {

                            })

                    Column {
                        ClickableInformationItem(
                            iconResId = R.drawable.ic_image_add,
                            text = "选择图片",
                            backgroundColor = Primary,
                            tint = White,
                            textColor = White
                        ) {

                        }

                        ClickableInformationItem(
                            iconResId = R.drawable.ic_url_1,
                            text = "网络图片",
                            backgroundColor = Primary,
                            tint = White,
                            textColor = White
                        ) {

                        }
                    }
                }

                InfoText(text = "输入网络图片url")
                InputTextFiled(
                    value = "",
                    onValueChange = {},
                    textStyle = TextStyle(fontSize = 16.sp),
                    inputFieldHeight = 24.dp,
                    padding = PaddingValues(2.dp)
                )
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoText(text = "进度条")

                InfoText(text = "当前值")
                InfoText(text = "*绑定属性后,当前值依据绑定的数据的当前值与最大值相除求得(当前值=绑定值/最大值)")
                ClickableInformationItem(
                    iconResId = R.drawable.ic_url_1,
                    text = "绑定字段",
                    backgroundColor = Primary,
                    tint = White,
                    textColor = White
                ) {

                }

                InfoText(text = "最大值")
                ClickableInformationItem(
                    iconResId = R.drawable.ic_url_1,
                    text = "绑定字段",
                    backgroundColor = Primary,
                    tint = White,
                    textColor = White
                ) {

                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoText(text = "外观")

                InfoText(text = "不透明度")
                Slider(value = 100f, onValueChange = {})


                Box(
                    modifier = Modifier
                        .radius(4.dp)
                        .size(24.dp)
                        .background(Primary)
                )

                InfoText(text = "圆角")
                Slider(value = 1f, onValueChange = {})
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoText(text = "背景")

                InfoText(text = "图片")

                Row {
                    Image(painter = painterResource(id = R.drawable.bg_default_name_card),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(4.dp)
                            .size(36.dp)
                            .clickable {

                            })

                    Column {
                        ClickableInformationItem(
                            iconResId = R.drawable.ic_image_add,
                            text = "选择图片",
                            backgroundColor = Primary,
                            tint = White,
                            textColor = White
                        ) {

                        }

                        ClickableInformationItem(
                            iconResId = R.drawable.ic_url_1,
                            text = "网络图片",
                            backgroundColor = Primary,
                            tint = White,
                            textColor = White
                        ) {

                        }
                    }
                }

            }
        }
    }

    if (viewModel.showAddComponentPopupWindow) {
        AppWidgetAddableComponentPopupWindow(
            popupProvider = viewModel.popupPositionProvider,
            items = viewModel.addableComponent,
            onClickItem = viewModel::onAddComponent,
            onDismissRequest = viewModel::dismissAddComponentPopupWindow
        )
    }

    if (viewModel.showAppWidgetBindingPanel) {
        AppWidgetFieldBindingPanel(
            offsetX = offsetXAnim + panelWidth * if (alignment == Alignment.TopEnd) -1 else 1,
            alignment = alignment,
            service = viewModel.appWidgetBindingService,
            onClickField = viewModel::updateComponentBindingFiled
        )
    }

}