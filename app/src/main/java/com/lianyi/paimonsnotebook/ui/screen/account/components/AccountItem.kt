package com.lianyi.paimonsnotebook.ui.screen.account.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.action.pressureMonitor
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.shake
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingEnd
import com.lianyi.paimonsnotebook.common.util.compose.shape.CirclePath
import com.lianyi.paimonsnotebook.common.util.compose.shape.CirclePathStartPoint
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    user: User,
    onDelete: (User) -> Unit = {},
    onCopyCookie: (User) -> Unit = {},
    onSetDefault: (User) -> Unit = {},
    onRefreshCookie: (User) -> Unit,
    diskCache: DiskCache = DiskCache(user.userInfo.avatar_url),
    onChangeAccountPlayerUid: (User, UserGameRoleData.Role) -> Unit = { _, _ -> },
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

        var deleteIsPressed by remember {
            mutableStateOf(false)
        }
        val deleteAccountAnim = remember {
            Animatable(initialValue = 0f, Float.VectorConverter)
        }

        //动画完成重置
        LaunchedEffect(deleteAccountAnim.value) {
            if (deleteAccountAnim.value >= 1f) {
                deleteAccountAnim.snapTo(0f)
                showGameRoles = false
                deleteIsPressed = false
                onDelete.invoke(user)
            }
        }

        //判断是否持续触摸
        LaunchedEffect(deleteIsPressed) {
            if (deleteIsPressed) {
                deleteAccountAnim.animateTo(1f, tween(2000))
            } else if (deleteAccountAnim.value > 0f) {
                deleteAccountAnim.animateTo(0f, tween(1500))
            }
        }

        //信息展示
        Column {
            //账号信息
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
            ) {

                Box(
                    Modifier
                        .clip(
                            CirclePath(
                                deleteAccountAnim.value,
                                CirclePathStartPoint.CenterEnd,
                                offset = Offset(-72f, 0f)
                            )
                        )
                        .fillMaxSize()
                        .background(Error)
                ) {
                }

                //信息布局
                Row(modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        onClick = {
                            showGameRoles = !showGameRoles
                        },
                        onLongClick = {
                            onSetDefault.invoke(user)
                        }
                    ),
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
                        modifier = Modifier
                            .weight(1f),
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

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_sync_circle),
                        contentDescription = null,
                        modifier = Modifier
                            .paddingEnd(10.dp)
                            .size(30.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .clickable {
                                onRefreshCookie.invoke(user)
                            }, tint = Black
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_cookie),
                        contentDescription = null,
                        modifier = Modifier
                            .paddingEnd(10.dp)
                            .size(30.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .clickable {
                                onCopyCookie.invoke(user)
                            }, tint = Black
                    )

                    Icon(painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .paddingEnd(10.dp)
                            .size(30.dp)
                            .padding(0.dp)
                            .shake(deleteIsPressed)
                            .pressureMonitor(
                                onBegin = {
                                    deleteIsPressed = true
                                }, onEnd = {
                                    deleteIsPressed = false
                                }), tint = Black
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
