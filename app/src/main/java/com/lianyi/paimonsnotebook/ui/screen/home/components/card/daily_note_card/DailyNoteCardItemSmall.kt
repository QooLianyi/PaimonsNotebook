package com.lianyi.paimonsnotebook.ui.screen.home.components.card.daily_note_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun DailyNoteCardItemSmall(
    icon:Int,
    text:String
) {
    Column(
        modifier = Modifier
            .radius(8.dp)
            .size(71.dp)
            .background(White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = text, fontSize = 16.sp)

    }
}