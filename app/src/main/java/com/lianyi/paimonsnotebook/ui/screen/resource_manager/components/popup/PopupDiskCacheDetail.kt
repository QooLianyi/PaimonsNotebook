package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.popup

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.extension.modifier.action.doubleClick
import com.lianyi.paimonsnotebook.common.extension.modifier.action.pointerInputDetectTransformGestures
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun PopupDiskCacheDetail(
    showImage: Boolean,
    showImageData: DiskCacheGroupData.GroupItem?,
    showImageDetailDropMenu: Boolean,
    imageDetailDropMenuAction: (Int) -> Unit,
    onDisableImage: () -> Unit,
    onShowImageDetailDropMenu: () -> Unit,
    onDisableImageDetailDropMenu: () -> Unit,
    imageDetailDropMenuOptions: Array<String>,
) {
    val scope = rememberCoroutineScope()

    AnimatedVisibility(visible = showImage,
        enter = fadeIn() + expandHorizontally { it },
        exit = fadeOut() + shrinkHorizontally { it }
    ) {

        val scale = remember {
            Animatable(1f, Float.VectorConverter)
        }

        var isTouching by remember {
            mutableStateOf(false)
        }

        val imageBackgroundAlpha by remember {
            derivedStateOf {
                scale.value.coerceAtMost(1f)
            }
        }

        var offset by remember {
            mutableStateOf(Offset.Zero)
        }

        LaunchedEffect(isTouching) {
            if (!isTouching) {
                if (scale.value == .5f) {
                    onDisableImage()
                } else if (scale.value < 1f) {
                    scale.animateTo(1f)
                }
            }
        }

        Box {
            Box(
                modifier = Modifier
                    .alpha(imageBackgroundAlpha)
                    .fillMaxSize()
                    .background(White)
            ) {

            }

            Column {
                StatusBarPaddingSpacer()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .pointerInput(Unit) {
                        }
                        .shadow(1.dp)
                        .background(White)
                        .padding(8.dp, 4.dp)
                        .zIndex(2f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = "stop_show_image",
                        tint = Black,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .clickable {
                                onDisableImage()
                            }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${showImageData?.data?.name}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (showImageData?.data?.description?.isNotBlank() == true) {

                            Spacer(modifier = Modifier.height(1.dp))

                            Text(
                                text = showImageData.data.description,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Font_Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_vertical),
                            contentDescription = "image_detail_drop_menu",
                            tint = Black,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .clickable {
                                    onShowImageDetailDropMenu()
                                }
                        )

                        DropdownMenu(
                            expanded = showImageDetailDropMenu,
                            onDismissRequest = { onDisableImageDetailDropMenu() }) {
                            imageDetailDropMenuOptions.forEachIndexed { index, s ->
                                DropdownMenuItem(onClick = {
                                    imageDetailDropMenuAction(index)
                                }) {
                                    Text(text = s, fontSize = 16.sp)
                                }
                            }
                        }

                    }

                }

                Box(modifier = Modifier
                    .zIndex(1f)
                    .doubleClick(clickBlock = {
                        onDisableImage()
                    }, doubleClickBlock = {
                        offset = Offset.Zero
                        scope.launch {
                            scale.snapTo(1f)
                        }
                    })
                ) {

//                    Image(
//                        painter = rememberAsyncImagePainter(model = showImageData?.file),
//                        contentDescription = showImageData?.data?.name,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .pointerInputDetectTransformGestures(
//                                isTransformInProgressChanged = {
//                                    isTouching = it
//                                }) { _, pan, zoom, _ ->
//                                offset += pan
//                                scope.launch {
//                                    scale.snapTo(
//                                        (zoom * scale.value).coerceAtLeast(
//                                            .5f
//                                        )
//                                    )
//                                }
//                            }
//                            .scale(scale = scale.value)
//                            .graphicsLayer(
//                                translationX = offset.x,
//                                translationY = offset.y
//                            )
//                    )

                }

            }

        }

    }
}