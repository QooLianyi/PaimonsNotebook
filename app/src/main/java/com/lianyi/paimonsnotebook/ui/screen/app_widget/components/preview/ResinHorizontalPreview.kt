package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun ResinHorizontalWidgetPreview() {
    Row(
        modifier = Modifier
            .background(White, RoundedCornerShape(6.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_resin), contentDescription = "",
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "160/160", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}