package com.lianyi.paimonsnotebook.ui.screen.items.components.item.property

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
internal fun PropertyItemGroup(
    propertyList: List<FightPropertyFormat>,
    compareList: List<FightPropertyFormat> = listOf()
) {
    Column(
        modifier = Modifier
            .radius(4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White_40)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        propertyList.forEachIndexed { index, cardPropertyData ->
            PropertyItem(
                data = cardPropertyData,
                compareData =  if (index >= compareList.size) null else compareList[index]
            )
        }

    }
}