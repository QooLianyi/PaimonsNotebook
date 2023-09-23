package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note_widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.theme.Primary_4
import com.lianyi.paimonsnotebook.ui.theme.Primary_9

@Composable
fun ResinProgressBarRecoverTime3X2RemoteViewsPreview(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    Column(
        modifier = Modifier
            .radius(8.dp)
            .size(270.dp, 80.dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_resin),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "原粹树脂",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = previewAnimData.textColor.value
            )
        }

        Row(
            modifier = Modifier
                .padding(0.dp, 6.dp)
                .clip(CircleShape)
                .fillMaxWidth()
                .height(5.dp)
                .background(Primary_9)
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Primary_4)
            ) {

            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "剩余恢复时间",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp,
                    color = previewAnimData.textColor.value
                )
                Text(
                    text = "99小时60分钟",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp,
                    color = previewAnimData.textColor.value,
                    modifier = Modifier.alpha(.8f)
                )
            }

            Text(
                text = "160/160",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = previewAnimData.textColor.value
            )
        }

    }
}