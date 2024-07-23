package com.lianyi.core.ui.components.notify

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.R
import com.lianyi.core.common.extension.modifier.radius.radius
import com.lianyi.core.common.notify.NotifyData
import com.lianyi.core.common.notify.NotifyHelper
import com.lianyi.core.common.notify.NotifyType
import com.lianyi.core.ui.theme.Black
import com.lianyi.core.ui.theme.Error
import com.lianyi.core.ui.theme.Error_1
import com.lianyi.core.ui.theme.Primary_9
import com.lianyi.core.ui.theme.Warning
import com.lianyi.core.ui.theme.Warning_1


@Composable
fun NotifyCard(
    data: NotifyData,
) {
    val (primaryColor, icon, backgroundColor) = when (data.type) {
        NotifyType.Error -> Triple(
            Error,
            R.drawable.ic_dismiss_circle_full,
            Error_1
        )

        NotifyType.Warning -> Triple(
            Warning,
            R.drawable.ic_warning_1,
            Warning_1
        )

        else -> Triple(Black, R.drawable.ic_info_1, Primary_9)
    }

    Crossfade(targetState = data.isShowing.value, animationSpec = tween(490), label = "") {
        if (it) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(backgroundColor)
                    .padding(16.dp, 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon), contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp).align(Alignment.CenterStart),
                )

                Text(
                    text = data.content, fontSize = 14.sp, color = primaryColor,
                    modifier = Modifier.padding(
                        26.dp,
                        0.dp,
                        if (data.closeable) 26.dp else 0.dp,
                        0.dp
                    )
                )

                if (data.closeable) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dismiss),
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(20.dp)
                            .clickable {
                                NotifyHelper.remove(data.notificationId)
                            }.align(Alignment.TopEnd),
                    )
                }
            }

        }
    }


}