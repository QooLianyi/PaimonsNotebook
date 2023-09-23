package com.lianyi.paimonsnotebook.ui.screen.daily_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.ui.screen.daily_note.data.DailyNoteCardItemData
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.Primary_7

@Composable
fun DailyNoteCardInfoItem(
    data: DailyNoteCardItemData
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = data.icon),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .padding(1.dp)
                .drawArcBorder(
                    color = Primary_1,
                    sweepAngle = data.sweepAngle,
                    enabledTrack = true,
                    trackColor = Primary_7
                )
                .padding(2.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = data.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(2.dp))

            Text(text = data.description, color = Info, fontSize = 14.sp)
        }

        Text(text = data.state, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}