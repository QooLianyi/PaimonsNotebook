package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor

@Composable
internal fun ItemActionButton(
    iconResId: Int,
    text: String = "",
    spacer: Dp = 10.dp,
    backgroundColor: Color = BlurCardBackgroundColor,
    iconTint: Color = Black,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .radius(4.dp)
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(23.dp),
            tint = iconTint
        )

        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(spacer))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}