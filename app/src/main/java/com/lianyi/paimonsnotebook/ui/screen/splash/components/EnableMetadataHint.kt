package com.lianyi.paimonsnotebook.ui.screen.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White_60
import kotlinx.coroutines.delay

@Composable
fun EnableMetadataHint(
    onCountDownEnd: () -> Unit,
    skipDownloadMetadata: () -> Unit,
    downloadMetadata: () -> Unit
) {
    var countDown by remember {
        mutableIntStateOf(20)
    }

    LaunchedEffect(Unit) {
        while (countDown > 0) {
            delay(1000)
            countDown -= 1
        }

        onCountDownEnd.invoke()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.emotion_icon_nahida_thinking),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "是否要下载元数据?\n下载元数据后即可使用程序的全部功能,包含祈愿记录、角色资料、武器资料等。\n缺少元数据时仅可使用基础的功能,如账号cookie获取、扫码登录等。\n进入程序后也可在设置中下载元数据。",
            color = Black,
            modifier = Modifier.padding(vertical = 32.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                text = "暂不下载",
                onClick = skipDownloadMetadata,
                backgroundColor = White_60,
                textColor = Black,
                modifier = Modifier.weight(1f),
                textSize = 14.sp,
                bold = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(
                text = "立即下载(${countDown})",
                onClick = downloadMetadata,
                modifier = Modifier.weight(1f),
                textSize = 14.sp,
                bold = true
            )
        }
    }

}