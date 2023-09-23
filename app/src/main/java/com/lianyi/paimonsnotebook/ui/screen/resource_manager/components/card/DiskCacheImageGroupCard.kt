package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.card

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingBottom
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.DiskCacheGroupImage
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData
import com.lianyi.paimonsnotebook.ui.theme.Font_Primary
import com.lianyi.paimonsnotebook.ui.theme.Info_1
import kotlin.math.ceil

@Composable
fun DiskCacheImageGroupCard(
    list: List<DiskCacheGroupData.GroupItem>,
    groupName: String,
    rows: Int = 4,
    isMultipleSelect: Boolean,
    isAllSelect: Boolean,
    imageSize: Dp,
    onClickImageGroupActionButton: () -> Unit,
    onClickImage: (DiskCacheGroupData.GroupItem) -> Unit,
    onEnabledMultipleSelect: () -> Unit,
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .height(40.dp)
                .padding(10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = groupName,
                fontSize = 14.sp,
                color = Font_Primary,
                modifier = Modifier.weight(1f)
            )

            Crossfade(
                targetState = isMultipleSelect
            ) {
                if (it) {
                    Text(
                        text = if (isAllSelect) "全不选" else "全选",
                        fontSize = 14.sp,
                        color = Font_Primary,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onClickImageGroupActionButton()
                            }
                            .background(Info_1)
                            .padding(10.dp, 4.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.height(imageSize * (ceil(list.size / rows.toFloat()))),
            userScrollEnabled = false
        ) {

            items(list.split(rows)) { split ->

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .paddingBottom(1.dp)
                ) {
                    split.forEachIndexed { index, item ->

                        Box {

                            DiskCacheGroupImage(
                                item = item,
                                index = index,
                                imageSize = imageSize,
                                onClickImage = onClickImage,
                                onEnabledMultipleSelect = onEnabledMultipleSelect
                            )

                            Crossfade(
                                targetState = isMultipleSelect,
                                modifier = Modifier
                                    .size(35.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(5.dp)
                            ) {
                                if (it) {
                                    Checkbox(
                                        checked = item.select,
                                        onCheckedChange = {
                                            onClickImage(item)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
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