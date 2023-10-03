package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.theme.Black_40
import com.lianyi.paimonsnotebook.ui.theme.Black_5
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import kotlin.math.roundToInt

@Composable
internal fun DailyMaterial3X2Preview(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    Column(
        Modifier
            .radius(previewAnimData.backgroundRadius.value.roundToInt().dp)
            .width(240.dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.emotion_icon_nahida_drink),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "今天是[周一]",
                    fontSize = 10.sp,
                    color = previewAnimData.textColor.value,
                    modifier = Modifier.padding(4.dp, 0.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            val (actionButtonTintColor,actionButtonBackgroundColor) = remember(previewAnimData.backgroundColor) {
                if(previewAnimData.backgroundColor.value == Transparent){
                    previewAnimData.textColor.value to Transparent
                }else{
                    Black_40 to Black_5
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(actionButtonBackgroundColor)
                        .padding(2.dp),
                    tint = actionButtonTintColor
                )

                Text(
                    text = "3/10",
                    fontSize = 10.sp,
                    color = previewAnimData.textColor.value,
                    modifier = Modifier.padding(6.dp, 0.dp),
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(actionButtonBackgroundColor)
                        .padding(2.dp),
                    tint = actionButtonTintColor
                )
            }
        }

        val icons = remember {
            listOf(
                R.drawable.emotion_icon_ambor_failure to "材料名称",
                R.drawable.emotion_icon_paimon_error to "材料名称",
                R.drawable.emotion_icon_nahida_thinking to "材料名称",
                R.drawable.emotion_icon_keqing_sleeping to "材料名称",
                R.drawable.emotion_icon_nahida_drink to "材料名称"
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            icons.forEach { pair ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = pair.first),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = pair.second,
                        fontSize = 10.sp,
                        color = previewAnimData.textColor.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            icons.forEach { pair ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = pair.first),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = pair.second,
                        fontSize = 10.sp,
                        color = previewAnimData.textColor.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}