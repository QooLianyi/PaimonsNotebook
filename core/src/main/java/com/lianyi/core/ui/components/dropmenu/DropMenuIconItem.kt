package com.lianyi.core.ui.components.dropmenu

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.core.ui.components.text.PrimaryText

@Composable
fun DropMenuIconItem(
    iconResId: Int,
    text: String,
    onClick: () -> Unit
) = DropMenuIconItem(painter = painterResource(id = iconResId), text = text, onClick = onClick)

@Composable
fun DropMenuIconItem(
    painter: Painter,
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .padding(2.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            PrimaryText(text = text)
        }
    }
}
