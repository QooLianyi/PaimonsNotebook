package com.lianyi.paimonsnotebook.ui.screen.items.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.data.SearchOptionData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun SearchOption(
    data: SearchOptionData,
    selected: Boolean,
    onClick: (SearchOptionData) -> Unit
) {
    val name = remember {
        when (data.sortBy) {
            ItemFilterType.Star -> ""
            else -> {
                data.name.takeIf { it.isNotEmpty() }
                    ?: ItemContentFilterHelper.getSortTypeNameByType(data.sortBy)
            }
        }
    }

    val (backgroundColor, textColor) = remember(selected) {
        if (selected) {
            Black to White
        } else {
            if (data.sortBy == ItemFilterType.Element) ElementType.getElementColorByName(data.name) to White else CardBackGroundColor to Black
        }
    }

    Box(modifier = Modifier
        .clip(CircleShape)
        .clickable {
            onClick.invoke(data)
        })
    {
        InformationItem(
            iconResId = data.iconResId,
            iconUrl = data.iconUrl,
            text = name,
            textColor = textColor,
            backgroundColor = backgroundColor
        ) {
            if (selected && data.orderBy != SortOrderBy.None) {
                Icon(
                    painter = painterResource(id = if (data.orderBy == SortOrderBy.Ascend) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = textColor
                )
            }

            if (data.contentSlot != null) {
                data.contentSlot.invoke()
            }
        }
    }
}