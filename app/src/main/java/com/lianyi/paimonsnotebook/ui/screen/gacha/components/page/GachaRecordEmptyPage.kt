package com.lianyi.paimonsnotebook.ui.screen.gacha.components.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.widget.IconTextHintSlotItem
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1

@Composable
fun GachaRecordEmptyPage(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.emotion_icon_nahida_thinking),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "当前还没有祈愿记录", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .fillMaxWidth()
        ) {
            IconTextHintSlotItem(
                title = "前往祈愿记录设置页",
                description = "前往祈愿记录设置界面选择获取祈愿记录的方式",
                backGroundColor = CardBackGroundColor_Light_1,
                primaryTextSize = 16.sp,
                secondTextSize = 14.sp,
                onClick = {
                    onClick.invoke()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Black_30
                )
            }
        }
    }
}