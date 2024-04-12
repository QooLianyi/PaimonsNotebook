package com.lianyi.paimonsnotebook.common.components.placeholder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 视频播放器占位符
*
* */
@Composable
fun VideoPlayerPlaceholder(
    cover: String,
    duration: Int = 0,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .clickable {
            onClick()
        }
    ) {

        NetworkImage(
            url = cover,
            modifier = Modifier
                .radius(4.dp)
                .fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )

        //覆盖封面的遮罩,防止封面过亮导致播放按钮看不起
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Black_10)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = "play",
            modifier = Modifier
                .align(Alignment.Center)
                .size(65.dp)
        )

        if (duration > 0) {
            Text(
                text = TimeHelper.timeParseUnix(duration * 1000), modifier = Modifier
                    .padding(0.dp, 6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .align(Alignment.BottomEnd)
                    .background(Black_30)
                    .padding(4.dp, 2.dp), color = White, fontSize = 13.sp
            )
        }
    }
}