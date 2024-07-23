package com.lianyi.paimonsnotebook.ui.screen.account.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.Primary_1

@Composable
fun AccountItemGameRole(
    role: UserGameRoleData.Role,
    onChangeAccountPlayerUid: (UserGameRoleData.Role) -> Unit,
) {
    //player uid 指示器动画
    val playerUidIndicatorHeightAnim by animateDpAsState(
        targetValue = if (role.is_chosen) 20.dp else 0.dp,
        label = ""
    )
    val playerUidIndicatorAlphaAnim by animateFloatAsState(
        targetValue = if (role.is_chosen) 1f else 0f,
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .padding(0.dp, 2.dp)
            .background(CardBackGroundColor_Light_1)
            .padding(0.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically
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
                .weight(1f)
        ) {
            PrimaryText(
                text = role.nickname,
                fontSize = 14.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            PrimaryText(
                text = "${role.game_uid} | ${role.region_name}",
                fontSize = 12.sp,
                color = Info
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!role.is_chosen) {
            Icon(
                painter = painterResource(id = R.drawable.ic_user_outline),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .radius(2.dp)
                    .size(24.dp)
                    .clickable {
                        onChangeAccountPlayerUid.invoke(role)
                    }, tint = Black
            )
        }

    }
}