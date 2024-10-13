package com.lianyi.paimonsnotebook.ui.screen.home.components.card.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingStart
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.Info_1
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_10
import com.lianyi.paimonsnotebook.ui.theme.White_20
import com.lianyi.paimonsnotebook.ui.theme.White_80

@Composable
internal fun AccountInfoCard(
    user: User?,
    onAddAccount: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(145.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_default_name_card),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            StatusBarPaddingSpacer()

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier
                    .drawBehind {
                        drawCircle(White_20, 86f, style = Stroke(50f))
                        drawCircle(White_10, 128f, style = Stroke(4f))
                    }
                ) {

                    if (user != null) {
                        NetworkImage(
                            url = user.userInfo.avatar_url,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(3.dp)
                                .clip(CircleShape)
                                .background(White)
                                .border(3.dp, White, CircleShape),
                            contentScale = ContentScale.Crop,
                            diskCache = DiskCache(
                                url = user.userInfo.avatar_url,
                                name = "默认账户头像",
                                createFrom = "首页",
                                description = "默认账户头像",
                                type = DiskCacheDataType.Stable,
                                lastUseFrom = "首页"
                            )
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.icon_offline_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(3.dp)
                                .clip(CircleShape)
                                .background(White)
                                .border(3.dp, White, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    com.lianyi.core.ui.components.text.PrimaryText(
                        text = user?.userInfo?.nickname
                            ?: "旅行者",
                        textSize = 16.sp,
                        color = White,
                        modifier = Modifier
                            .background(Black_10)
                            .paddingStart(5.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    com.lianyi.core.ui.components.text.PrimaryText(
                        text = user?.getSelectedGameRole()?.game_uid ?: "当前未选择角色",
                        textSize = 13.sp,
                        color = Info_1,
                        modifier = Modifier
                            .background(Black_10)
                            .paddingStart(6.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Row(modifier = Modifier
                .background(Black_30, CircleShape)
                .border(1.dp, White_80, CircleShape)
                .clip(CircleShape)
                .clickable {
                    onAddAccount.invoke()
                }
                .padding(6.dp, 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.function_icon_hutao_database),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp), tint = White
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "账号管理", fontSize = 12.sp, color = White)
            }
        }
    }
}