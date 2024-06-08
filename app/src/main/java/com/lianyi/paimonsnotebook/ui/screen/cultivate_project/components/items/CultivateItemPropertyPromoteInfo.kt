package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.Black_90
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun CultivateItemPropertyPromoteInfo(
    iconUrl: String,
    name: String,
    fromLevel: Int,
    toLevel: Int,
    alignBothSide: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(
            6.dp
        )
    ) {

        InformationItem(
            iconUrl = iconUrl,
            text = name,
            tint = Black,
            backgroundColor = White
        )

        if (alignBothSide) {
            Spacer(modifier = Modifier.weight(1f))
        }

        InformationItem(
            text = "Lv.${fromLevel}",
            backgroundColor = White
        )

        Row(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(
                1.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_triangle_round),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = Black_30
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_triangle_round),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = Black_60
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_triangle_round),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = Black_90
            )
        }

        InformationItem(
            text = "Lv.${toLevel}",
            backgroundColor = White
        )

    }
}