package com.lianyi.paimonsnotebook.ui.screen.items.components.item.material

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.popup.IconTitleInformationPopupWindow
import com.lianyi.paimonsnotebook.common.data.popup.IconTitleInformationPopupWindowData
import com.lianyi.paimonsnotebook.common.data.popup.PopupWindowPositionProvider
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_40

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ItemMaterialContent(
    list: List<Material>
) {
    var showPopup by remember {
        mutableStateOf(false)
    }

    var popupWindowInfo by remember(Unit) {
        mutableStateOf(IconTitleInformationPopupWindowData())
    }

    var provider by remember {
        mutableStateOf<PopupWindowPositionProvider?>(null)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .radius(4.dp)
                .background(White_40)
                .padding(8.dp, 0.dp, 8.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            list.forEach { material ->

                //当前组件在窗口上的位置
                var currentPosition = Offset.Zero
                var currentSize = IntSize.Zero
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .radius(4.dp)
                        .onGloballyPositioned {
                            currentPosition = it.positionInRoot()
                            currentSize = it.size
                        }
                        .size(60.dp, 80.dp)
                        .border(1.dp, Black_10, RoundedCornerShape(4.dp))
                        .clickable {
                            provider = PopupWindowPositionProvider(
                                contentOffset = Offset(
                                    currentPosition.x,
                                    currentPosition.y
                                ),
                                itemSize = currentSize,
                                itemSpace = 8.dp
                            )

                            popupWindowInfo = material.getShowPopupWindowInfo()
                            showPopup = true
                        }
                ) {

                    Image(
                        painter = painterResource(id = material.qualityResId),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .size(62.dp, 82.dp)
                            .align(Alignment.Center)
                    )

                    Column(modifier = Modifier.fillMaxSize()) {
                        NetworkImageForMetadata(
                            url = material.iconUrl,
                            modifier = Modifier
                                .size(60.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .background(White)
                                .padding(2.dp, 0.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            com.lianyi.core.ui.components.text.AutoSizeText(
                                text = material.Name,
                                targetTextSize = 10.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                minTextSize = 10.sp
                            )
                        }
                    }

                }
            }
        }

        if (showPopup && provider != null) {
            IconTitleInformationPopupWindow(
                data = popupWindowInfo,
                popupProvider = provider!!,
                onDismissRequest = {
                    showPopup = false
                }
            )
        }
    }
}