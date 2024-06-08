package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyVerticalGrid
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.group.CultivateMaterialGroupHeader
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.group.CultivateMaterialGroupItem
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data.EntityBaseInfo
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data.MaterialBaseInfo
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CultivateProjectOverallPage(
    showGridLayout: Boolean,
    overallMaterialBaseInfoGroupList: List<List<MaterialBaseInfo>>,
    overallMaterialBaseInfoGroupListFlatten: List<MaterialBaseInfo>,
    getOverallEntityBaseInfoListByMaterialId: (Int) -> List<EntityBaseInfo>,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit,
    onShowEntityInfoPopupDialog: (Int, InformationPopupPositionProvider) -> Unit
) {
    Crossfade(targetState = showGridLayout, label = "") {
        if (it) {
            ContentSpacerLazyVerticalGrid(
                columns = GridCells.Adaptive(60.dp),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                statusBarPaddingEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) {

                items(overallMaterialBaseInfoGroupListFlatten) { baseInfo ->
                    CultivateMaterialGroupHeader(
                        materialBaseInfo = baseInfo,
                        imageSize = 55.dp,
                        onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog
                    )
                }
            }
        } else {
            ContentSpacerLazyVerticalGrid(
                columns = GridCells.Adaptive(40.dp),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                statusBarPaddingEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) {

                items(overallMaterialBaseInfoGroupList, span = {
                    GridItemSpan(this.maxLineSpan)
                }) { materialBaseInfoList ->
                    var showEntityList by remember {
                        mutableStateOf(false)
                    }

                    Column(
                        modifier = Modifier
                            .radius(8.dp)
                            .fillMaxSize()
                            .background(CardBackGroundColor_Light_1)
                            .clickable {
                                showEntityList = !showEntityList
                            }
                            .padding(8.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            materialBaseInfoList.forEach { baseInfo ->

                                CultivateMaterialGroupHeader(
                                    materialBaseInfo = baseInfo,
                                    onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog
                                )
                            }
                        }

                        AnimatedVisibility(visible = showEntityList) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    getOverallEntityBaseInfoListByMaterialId(materialBaseInfoList.first().material.Id)
                                        .forEach { entityBaseInfo ->
                                            CultivateMaterialGroupItem(
                                                entityBaseInfo = entityBaseInfo,
                                                onShowEntityInfoPopupDialog = onShowEntityInfoPopupDialog
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}