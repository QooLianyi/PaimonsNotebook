package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.data.popup.PopupWindowPositionProvider
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateEntity
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.material.CultivateMaterialGroup
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data.CultivateItemInfoData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun CultivateAvatarCard(
    avatarData: AvatarData,
    entity: CultivateEntity,
    cultivateItems: List<CultivateItems>,
    avatarCultivateMap: Map<Int, CultivateItemInfoData>,
    cultivateItemsMap: Map<Int, CultivateItems>,
    getCultivateMaterialsListByCultivateItemId: (Int) -> List<CultivateItemMaterials>,
    getMaterialInfo: (Int) -> Material,
    onClickDelete: (CultivateEntity, String) -> Unit,
    onEmitMaterialItemUpdateQueue: (CultivateItems, List<CultivateItemMaterials>) -> Unit,
    onShowMaterialInfoPopupDialog: (Material, PopupWindowPositionProvider) -> Unit,
) {
    var showMaterialGroup by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .radius(8.dp)
            .fillMaxWidth()
            .background(CardBackGroundColor_Light_1)
            .clickable {
                showMaterialGroup = !showMaterialGroup
            }
            .padding(8.dp)
    ) {

        //养成信息与角色基本信息布局 开始

        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            NetworkImageForMetadata(
                url = avatarData.gachaAvatarIcon,
                modifier = Modifier
                    .radius(4.dp)
                    .width(70.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillWidth,
                alignment = BiasAlignment(0f, -0.4f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    8.dp
                )
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp
                    )
                ) {
                    Text(
                        text = avatarData.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black
                    )

                    InformationItem(
                        text = "Lv.${cultivateItemsMap[avatarData.id]?.toLevel}",
                        backgroundColor = White,
                        textSize = 13.sp,
                        paddingValues = PaddingValues(
                            6.dp,
                            2.5.dp
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(22.dp)
                            .clickable {
                                onClickDelete.invoke(entity, avatarData.name)
                            }
                    )

                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp
                    )
                ) {

                    InformationItem(
                        iconResId = avatarData.fetterInfo.elementIconResId,
                        text = avatarData.fetterInfo.VisionBefore,
                        textColor = White,
                        backgroundColor = avatarData.fetterInfo.elementColor,
                        textSize = 12.sp,
                        paddingValues = PaddingValues(
                            6.dp,
                            2.5.dp
                        )
                    )

                    StarGroup(
                        starCount = avatarData.starCount,
                        starSize = 14.dp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(White)
                            .padding(6.dp, 4.dp)
                    )
                }


                cultivateItemsMap.forEach { (key, value) ->
                    //如果为角色等级养成信息或总览数据就跳过此处渲染
                    if (key == avatarData.id || key == -avatarData.id) return@forEach

                    val data =
                        avatarCultivateMap[value.itemId]
                            ?: return@forEach
                    CultivateItemPropertyPromoteInfo(
                        iconUrl = data.icon,
                        name = data.name,
                        fromLevel = value.fromLevel,
                        toLevel = value.toLevel
                    )
                }
            }
        }

        //养成信息与角色基本信息布局 结束

        AnimatedVisibility(visible = showMaterialGroup) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))

                CultivateMaterialGroup(
                    entity = entity,
                    cultivateItems = cultivateItems,
                    cultivateItemsMap = cultivateItemsMap,
                    getMaterialsByCultivateItemId = getCultivateMaterialsListByCultivateItemId,
                    getMaterialInfo = getMaterialInfo,
                    avatarData = avatarData,
                    onEmitMaterialItemUpdateQueue = onEmitMaterialItemUpdateQueue,
                    onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog
                )
            }
        }
    }
}