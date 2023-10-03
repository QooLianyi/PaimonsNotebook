package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.genshin.daily_note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import kotlin.math.roundToInt

@Composable
internal fun DailyNote2X1RemoteViewsPreview(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    val list = remember {
        listOf(
            R.drawable.icon_resin to "160/160",
            R.drawable.icon_daily_task to "4/4",
            R.drawable.icon_home_coin to "2400/2400",
            R.drawable.icon_secret_tower to "3/3"
        )
    }

    Column(
        modifier = Modifier
            .radius(previewAnimData.backgroundRadius.value.roundToInt().dp)
            .size(120.dp, 60.dp)
            .background(previewAnimData.backgroundColor.value),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            list.forEach { pair ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = pair.first),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(text = pair.second, fontSize = 10.sp, color = previewAnimData.textColor.value)
                }
            }
        }
    }
}