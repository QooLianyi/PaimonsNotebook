package com.lianyi.paimonsnotebook.ui.screen.account.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountItemGameRole(
    role: UserGameRoleData.Role,
    onChangeAccountPlayerUid: (UserGameRoleData.Role) -> Unit,
) {
    //player uid 指示器动画
    val playerUidIndicatorHeightAnim by animateDpAsState(targetValue = if (role.is_chosen) 20.dp else 0.dp)
    val playerUidIndicatorAlphaAnim by animateFloatAsState(targetValue = if (role.is_chosen) 1f else 0f)

    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(6.dp))
        .padding(0.dp, 2.dp)
        .combinedClickable(
            onClick = {},
            onLongClick ={
                onChangeAccountPlayerUid(role)
            }
        )
        .background(CardBackGroundColor_Light_1)
        .padding(0.dp, 3.dp)
    ) {

        Spacer(
            modifier = Modifier
                .alpha(playerUidIndicatorAlphaAnim)
                .width(4.dp)
                .border(4.dp, Primary_1, CircleShape)
                .height(playerUidIndicatorHeightAnim)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PrimaryText(text = role.nickname, fontSize = 14.sp, color = Black)
            Spacer(modifier = Modifier.height(2.dp))
            PrimaryText(
                text = "${role.game_uid} | ${role.region_name}",
                fontSize = 12.sp,
                color = Info
            )
        }
    }
}