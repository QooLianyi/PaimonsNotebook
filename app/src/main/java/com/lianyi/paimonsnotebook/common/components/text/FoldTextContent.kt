package com.lianyi.paimonsnotebook.common.components.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.Info

@Composable
fun FoldTextContent(
    modifier: Modifier = Modifier,
    titleSlot: @Composable RowScope.() -> Unit,
    textContentSlot: @Composable ColumnScope.() -> Unit,
) {

    var isUnfold by remember {
        mutableStateOf(false)
    }

    val angle by animateFloatAsState(targetValue = if (isUnfold) 180f else 0f)

    Column(modifier = modifier
        .clip(RoundedCornerShape(4.dp))
        .border(1.dp, Color(0xfff0f0f0), RoundedCornerShape(4.dp))
        .background(Color(0xFFFBFBFB))
        .clickable {
            isUnfold = !isUnfold
        }
    ) {
        Row(modifier = Modifier.padding(10.dp, 6.dp)) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                titleSlot()
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = "unfold",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(angle),
                tint = Info
            )
        }

        AnimatedVisibility(visible = isUnfold) {
            Column {

                Spacer(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(1.dp,Color(0xfff0f0f0))
                )

                textContentSlot()
            }
        }
    }

}