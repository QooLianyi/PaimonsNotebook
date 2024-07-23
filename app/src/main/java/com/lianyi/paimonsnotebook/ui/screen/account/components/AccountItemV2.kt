package com.lianyi.paimonsnotebook.ui.screen.account.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Primary_1

@Composable
fun AccountItemV2(
    modifier: Modifier = Modifier,
    user: User,
    onDelete: (User) -> Unit = {},
    onCopyCookie: (User) -> Unit = {},
    onSetDefault: (User) -> Unit = {},
    onRefreshCookie: (User) -> Unit,
    onChangeAccountPlayerUid: (User, UserGameRoleData.Role) -> Unit = { _, _ -> },
    diskCache: DiskCache
) {
    Column(modifier = modifier) {
        //默认账号指示器动画
        val defaultAccountIndicatorHeightAnim by animateDpAsState(
            targetValue = if (user.isSelected) 30.dp else 0.dp,
            label = ""
        )
        val defaultAccountIndicatorAlphaAnim by animateFloatAsState(
            targetValue = if (user.isSelected) 1f else 0f,
            label = ""
        )

        //展开账号角色
        var showGameRoles by remember {
            mutableStateOf(false)
        }

        //信息展示
        Column {
            //信息布局
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        showGameRoles = !showGameRoles
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                //指示器
                Spacer(
                    modifier = Modifier
                        .alpha(defaultAccountIndicatorAlphaAnim)
                        .padding(5.dp, 0.dp)
                        .width(6.dp)
                        .border(6.dp, Primary_1, CircleShape)
                        .height(defaultAccountIndicatorHeightAnim)
                        .align(Alignment.CenterVertically)
                )

                NetworkImage(
                    url = user.userInfo.avatar_url,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .border(1.dp, Primary_1, CircleShape),
                    contentScale = ContentScale.Crop,
                    diskCache = diskCache
                )

                Spacer(modifier = Modifier.width(5.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    PrimaryText(text = user.userInfo.nickname)

                    Spacer(modifier = Modifier.height(3.dp))

                    PrimaryText(
                        text = user.userInfo.uid,
                        fontSize = 14.sp,
                        bold = false
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {

                    if (!user.isSelected) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user_circle_outline),
                            contentDescription = null,
                            modifier = Modifier
                                .radius(2.dp)
                                .size(30.dp)
                                .clickable {
                                    onSetDefault.invoke(user)
                                }, tint = Black
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_cookie),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(30.dp)
                            .clickable {
                                onCopyCookie.invoke(user)
                            }, tint = Black
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_sync_circle),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(30.dp)
                            .clickable {
                                onRefreshCookie.invoke(user)
                            }, tint = Black
                    )


                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(30.dp)
                            .clickable {
                                onDelete.invoke(user)
                            }, tint = Black
                    )
                }
            }

            //角色信息
            AnimatedVisibility(visible = showGameRoles) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp, 2.dp)
                ) {

                    user.userGameRoles.forEach { role ->
                        AccountItemGameRole(role = role, onChangeAccountPlayerUid = {
                            onChangeAccountPlayerUid(user, it)
                        })
                    }
                }
            }
        }
    }

}