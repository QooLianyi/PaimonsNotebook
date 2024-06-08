package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lianyi.paimonsnotebook.common.extension.modifier.state.isScrollToEnd
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_40
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 描述对话框
*
* */
@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun LazyColumnDialog(
    title: String,
    titleSpacer: Dp = 20.dp,
    titleTextSize: TextUnit = 20.sp,
    verticalSpacedBy: Dp = 0.dp,
    buttonTextSize: TextUnit = 16.sp,
    buttonGroupAlign: Arrangement.Horizontal = Arrangement.End,
    buttons: Array<String> = arrayOf("关闭"),
    onDismissRequest: () -> Unit,
    onClickButton: (Int) -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {

        val state = rememberLazyListState()

        val offset by remember { derivedStateOf { state.firstVisibleItemScrollOffset } }

        val topDividerAnimation by animateFloatAsState(
            targetValue = if (offset == 0) 0f else 1f,
            animationSpec = tween(300), label = ""
        )

        val bottomDividerAnimation by animateFloatAsState(
            targetValue = if (state.isScrollToEnd && state.canScrollBackward) 1f else 0f,
            animationSpec = tween(300), label = ""
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            val maxHeight = maxHeight

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .background(White)
                    .padding(20.dp)
            ) {

                Text(
                    text = title,
                    fontSize = titleTextSize,
                    color = Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(titleSpacer))

                Spacer(
                    modifier = Modifier
                        .alpha(topDividerAnimation)
                        .height(.5.dp)
                        .fillMaxWidth()
                        .background(Black_40)
                )

                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyColumn(
                        state = state,
                        modifier = Modifier.requiredHeightIn(1.dp, maxHeight * .7f),
                        verticalArrangement = Arrangement.spacedBy(verticalSpacedBy)
                    ) {
                        content()

                        if (buttons.isNotEmpty()) {
                            item {
                                Column {
                                    Spacer(
                                        modifier = Modifier
                                            .alpha(bottomDividerAnimation)
                                            .height(.5.dp)
                                            .fillMaxWidth()
                                            .background(Black_40)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }

                if (buttons.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = buttonGroupAlign,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        buttons.forEachIndexed { index, s ->
                            TextButton(onClick = { onClickButton(index) }) {
                                Text(text = s, fontSize = buttonTextSize, color = Primary_1)
                            }
                            if (index != buttons.size - 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}