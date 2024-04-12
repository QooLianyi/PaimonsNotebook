package com.lianyi.paimonsnotebook.common.components.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    paddingValues: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
    borderColor: Color = Color(0xfff0f0f0),
    backgroundColor: Color = Color(0xFFFBFBFB),
    titleSlot: @Composable RowScope.() -> Unit,
    textContentSlot: @Composable ColumnScope.() -> Unit,
) {

    var isUnfold by remember {
        mutableStateOf(false)
    }

    val angle by animateFloatAsState(targetValue = if (isUnfold) 180f else 0f, label = "")

    Column(modifier = modifier
        .clip(RoundedCornerShape(4.dp))
        .border(1.dp, borderColor, RoundedCornerShape(4.dp))
        .background(backgroundColor)
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
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {

                Spacer(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(1.dp, borderColor)
                )

                textContentSlot()
            }
        }
    }

}