package com.lianyi.paimonsnotebook.ui.screen.items.components.item.property

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Error
import com.lianyi.paimonsnotebook.ui.theme.Success
import com.lianyi.paimonsnotebook.ui.theme.White_40
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun PropertyItem(
    data: FightPropertyFormat,
    compareData: FightPropertyFormat? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = data.iconResource),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .background(White_40)
                .padding(6.dp),
            tint = Black
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = data.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        Text(
            text = data.formatValue,
            fontSize = 14.sp
        )

        //对比数据不为空并且属性id相同
        if (compareData != null && data.property == compareData.property) {

            var showDecimal = false
            var compareValue = 0f

            val (iconResId, color) = remember(compareData) {
                (data.value - compareData.value).let {
                    compareValue = abs(
                        if (data.value < 1) {
                            showDecimal = true
                            it * 100
                        } else {
                            it
                        }
                    )

                    if (it < 0) {
                        R.drawable.ic_chevron_circle_down_full to Error
                    } else if (it > 0) {
                        R.drawable.ic_chevron_circle_up_full to Success
                    } else {
                        R.drawable.ic_ring to Black
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = iconResId), contentDescription = null, tint = color,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "${
                    when (showDecimal) {
                        true -> {
                            "${"%.1f".format(compareValue)}%"
                        }

                        false -> {
                            compareValue.roundToInt()
                        }
                    }
                }",
                fontSize = 10.sp,
                color = color
            )

        }

    }
}