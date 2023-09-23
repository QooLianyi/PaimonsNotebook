package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun ErrorRemoteViewsPreview(
    content:String = ""
) {
    Row(
        modifier = Modifier
            .radius(8.dp)
            .background(White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.emotion_icon_ambor_failure),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "错误的组件:${content}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Black
        )
    }
}