package com.lianyi.paimonsnotebook.ui.screen.items.components.item.property

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.blur_card.widget.ItemSlider
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_40
import kotlin.math.roundToInt


@Composable
internal fun ItemPropertyContent(
    iconUrl: String,
    name: String,
    compareIconUrl: String,
    propertyList: List<FightPropertyFormat>,
    compareItemPropertyList: List<FightPropertyFormat>,
    showPromotedButton: Boolean = true,
    onClickCompareAvatar: () -> Unit,
    onLevelChange: (Int) -> Unit,
    onPromotedChange: (Boolean) -> Unit,
    informationSlot: @Composable () -> Unit
) {
    var promoted by remember {
        mutableStateOf(false)
    }

    var level by remember {
        mutableIntStateOf(1)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            informationSlot.invoke()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(White_40)
                    .padding(6.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NetworkImageForMetadata(
                    url = iconUrl,
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(text = name, fontSize = 16.sp, color = Black)

                Spacer(modifier = Modifier.width(4.dp))
            }

            Box(modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onClickCompareAvatar.invoke()
                }
            ) {
                InformationItem(
                    iconUrl = compareIconUrl,
                    iconResId = if (compareIconUrl.isNotEmpty()) -1 else R.drawable.ic_select_other,
                    iconSize = 22.dp,
                    tint = null
                ) {
                    if (compareIconUrl.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dismiss),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        PropertyItemGroup(
            propertyList = propertyList,
            compareList = compareItemPropertyList
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showPromotedButton) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left_2),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .rotate(90f)
                        .background(if (promoted) Black else White_40)
                        .clickable {
                            promoted = !promoted
                            onPromotedChange.invoke(promoted)
                        }
                        .padding(8.dp),
                    tint = if (promoted) White else Black
                )
            }

            ItemSlider(
                value = level,
                range = (1f..90f),
                onValueChange = {
                    level = it.roundToInt()
                    onLevelChange.invoke(level)
                }
            )
        }
    }
}