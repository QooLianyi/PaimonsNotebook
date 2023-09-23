package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Success

@Composable
fun Expedition3X1RemoteViewsPreview(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    val icons = remember {
        listOf(
            R.drawable.emotion_icon_nahida_drink,
            R.drawable.emotion_icon_nahida_thinking,
            R.drawable.emotion_icon_keqing_sleeping,
            R.drawable.emotion_icon_ambor_failure,
            R.drawable.emotion_icon_paimon_error
        )
    }
    Row(
        modifier = Modifier
            .radius(8.dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        icons.forEachIndexed { index, i ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = i),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(30.dp)
                        .drawArcBorder(if (index % 2 == 0) Success else Black)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "999分钟",
                    fontSize = 10.sp,
                    color = previewAnimData.textColor.value
                )
            }
        }

    }
}