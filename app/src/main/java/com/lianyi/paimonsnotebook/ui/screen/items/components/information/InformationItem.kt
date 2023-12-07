package com.lianyi.paimonsnotebook.ui.screen.items.components.information

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White_40

/*
* 展示简单信息
* */
@Composable
internal fun InformationItem(
    iconResId: Int = -1,
    iconUrl: String = "",
    text: String = "",
    iconSize: Dp = 16.dp,
    textSize: TextUnit = 12.sp,
    textColor: Color = Black,
    tint: Color? = textColor,
    backgroundColor: Color = White_40,
    paddingValues: PaddingValues = PaddingValues(8.dp, 4.dp),
    slot: @Composable () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(paddingValues),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconUrl.isNotEmpty()) {
            NetworkImageForMetadata(
                url = iconUrl, tint = tint,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconSize)
            )
        }

        if (iconResId != -1) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize),
                colorFilter = if (tint != null) ColorFilter.tint(tint) else null
            )
        }

        if (text.isNotEmpty()) {
            Text(
                text = text,
                fontSize = textSize,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }

        slot.invoke()
    }
}