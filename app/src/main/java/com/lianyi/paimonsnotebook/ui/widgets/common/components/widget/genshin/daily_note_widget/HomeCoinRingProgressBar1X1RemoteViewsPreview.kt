package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note_widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.theme.Primary_4
import com.lianyi.paimonsnotebook.ui.theme.Primary_9
import kotlin.math.roundToInt

@Composable
fun HomeCoinRingProgressBar1X1RemoteViewsPreview(
    previewAnimData: RemoteViewsPreviewAnimData
) {

    val lineWidth = remember {
        6.dp.toPx()
    }

    Box(
        modifier = Modifier
            .radius(previewAnimData.backgroundRadius.value.roundToInt().dp)
            .size(60.dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(6.dp)
            .drawArcBorder(
                Primary_4,
                enabledTrack = true,
                trackColor = Primary_9,
                lineWidth = lineWidth,
                sweepAngle = 210f
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_home_coin),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}