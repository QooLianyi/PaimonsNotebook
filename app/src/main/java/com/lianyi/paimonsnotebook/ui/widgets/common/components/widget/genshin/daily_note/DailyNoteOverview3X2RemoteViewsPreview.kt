package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import kotlin.math.roundToInt

@Composable
fun DailyNoteOverview3X2RemoteViewsPreview(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    val icons = remember(Unit) {
        listOf(
            R.drawable.icon_home_coin to "2400",
            R.drawable.icon_daily_task to "4/4",
            R.drawable.icon_adventurers to "5/5",
            R.drawable.icon_quality_convert to "7天",
        )
    }

    Row(
        modifier = Modifier
            .radius(previewAnimData.backgroundRadius.value.roundToInt().dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(240.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_paimon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "角色昵称", fontSize = 14.sp, color = previewAnimData.textColor.value)
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "180",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = previewAnimData.textColor.value,
                )

                Image(
                    painter = painterResource(id = R.drawable.icon_resin),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock_outline),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "99小时99分钟\n明天23:59回满",
                    color = previewAnimData.textColor.value,
                    fontSize = 12.sp
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            icons.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = it.first),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = it.second,
                        color = previewAnimData.textColor.value,
                        fontSize = 14.sp
                    )
                }
            }
        }

    }
}