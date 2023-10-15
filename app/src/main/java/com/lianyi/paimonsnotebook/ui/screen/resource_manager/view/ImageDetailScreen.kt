package com.lianyi.paimonsnotebook.ui.screen.resource_manager.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.dialog.PropertiesDialog
import com.lianyi.paimonsnotebook.common.components.media.FullScreenImage
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.action.doubleClick
import com.lianyi.paimonsnotebook.common.extension.modifier.action.pointerInputDetectTransformGestures
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget.ImageDetailFooter
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget.ImageDetailHeader
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel.ImageDetailScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White
import kotlinx.coroutines.launch

class ImageDetailScreen : BaseActivity() {

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_REMOVE_PLAN_DELETE = "extra_remove_plan_delete"
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[ImageDetailScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.checkStoragePermission = this::checkStoragePermission

        viewModel.setImageCacheFileFromUrl(intent) {
            finish()
        }

        setContent {
            PaimonsNotebookTheme(this) {
                val scope = rememberCoroutineScope()

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

                Box {
                    Box(
                        modifier = Modifier
                            .alpha(imageBackgroundAlpha)
                            .fillMaxSize()
                            .background(White)
                    )

                    Box(
                        modifier = Modifier
                            .doubleClick(clickBlock = {
                                finish()
                            }, doubleClickBlock = {
                                offset = Offset.Zero
                                scope.launch {
                                    scale.snapTo(1f)
                                }
                            })
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(model = viewModel.cacheFile),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInputDetectTransformGestures({ _, pan, zoom, _ ->
                                    scope.launch {
                                        scale.snapTo(
                                            (zoom * scale.value).coerceAtLeast(
                                                .5f
                                            )
                                        )
                                    }

                                    offset += pan * (1 / scale.value).coerceAtMost(1f)

                                    isTouching = true
                                }, onGestureEnd = {
                                    isTouching = false

                                    scope.launch {
                                        if (scale.value == .5f) {
                                            finish()
                                        } else if (scale.value < 1f) {
                                            scale.animateTo(1f)
                                        }
                                    }
                                })
                                .scale(scale = scale.value)
                                .graphicsLayer(
                                    translationX = offset.x,
                                    translationY = offset.y
                                )
                        )
                    }

                    Crossfade(targetState = isTouching, label = "") {
                        if (!it) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                ImageDetailHeader(diskCache = viewModel.diskCacheData) {
                                    finish()
                                }

                                ImageDetailFooter(
                                    deleteImage = {
                                        viewModel.deleteImage {
                                            finish()
                                        }
                                    },
                                    saveImage = viewModel::saveImage,
                                    showImageProperty = viewModel::showPropertyDialog
                                )
                            }
                        }
                    }
                }


                if (viewModel.showPropertyDialog) {
                    PropertiesDialog(
                        title = "属性",
                        properties = viewModel.propertyList,
                        onDismissRequest = viewModel::dismissPropertyDialog,
                        onCopyPropertiesValue = viewModel::copyProperty,
                        onButtonClick = {
                            viewModel.dismissPropertyDialog()
                        }
                    )
                }

                if (viewModel.showFullScreen) {
                    FullScreenImage(url = viewModel.imageUrl)
                }


                if (viewModel.showRequestPermissionDialog) {
                    ConfirmDialog(title = "缺少权限",
                        content = "使用相应功能需要存储权限,点击确定前往申请",
                        onConfirm = {
                            viewModel.showRequestPermissionDialog = false
                            requestStoragePermission()
                        },
                        onCancel = {
                            viewModel.showRequestPermissionDialog = false
                        })
                }
            }
        }
    }
}