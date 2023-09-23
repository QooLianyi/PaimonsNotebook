package com.lianyi.paimonsnotebook.common.components.loading

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun LoadingAnimationPlaceholder(
    height: Dp = 140.dp,
    shadowDp: Dp = 10.dp,
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .border(10.dp, White, RoundedCornerShape(6.dp))
            .padding(5.dp)
            .shadow(shadowDp, RoundedCornerShape(6.dp))
            .padding(5.dp)
    ) {

        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color(0xff1B65A5))
        ) {

            if(maxWidth < 160.dp){
                "加载占位符加载失败:屏幕尺寸未到160dp".warnNotify(false)
                return@BoxWithConstraints
            }

            var back by remember {
                mutableStateOf(true)
            }

            val rotate by animateFloatAsState(
                targetValue = if (back) 180f else 0f,
                animationSpec = tween(500), label = ""
            )

            val offsetX by animateDpAsState(
                targetValue = if (back) 0.dp else (maxWidth - 80.dp),
                animationSpec = tween(2000), label = ""
            ) {
                if (it >= (maxWidth - 80.dp)) {
                    back = true
                }
                if (it <= 0.dp) {
                    back = false
                }
            }

            LaunchedEffect(Unit) {
                back = false
            }

            Image(
                painter = painterResource(id = R.drawable.icon_summer_land_mini),
                contentDescription = null, modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = (-31).dp)
            )
            Image(
                painter = painterResource(id = R.drawable.icon_area_summer_land),
                contentDescription = null, Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .offset(y = (-25).dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                    .background(Color(0xff13ABDC))
                    .align(Alignment.BottomStart)
            )

            Image(painter = painterResource(id = R.drawable.icon_klee),
                contentDescription = null, modifier = Modifier
                    .size(80.dp, 100.dp)
                    .align(Alignment.BottomStart)
                    .offset(offsetX, (-10).dp)
                    .graphicsLayer {
                        rotationY = rotate
                    })

        }
    }

}