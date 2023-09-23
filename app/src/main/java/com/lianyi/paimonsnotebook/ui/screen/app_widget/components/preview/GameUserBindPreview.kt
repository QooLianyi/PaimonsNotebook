package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Info

@Composable
internal fun GameUserBindPreview(
    user: User?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .radius(4.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (user == null) {
            Image(
                painter = painterResource(id = R.drawable.emotion_icon_nahida_thinking),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
        } else {
            NetworkImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                diskCache = DiskCache(
                    url = user.userInfo.avatar_url,
                    lastUseFrom = "桌面组件绑定"
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        val text = remember(user) {
            if (user == null) {
                "你需要选择一个绑定的用户"
            } else {
                "${user.userInfo.nickname} | ${user.userInfo.uid}"
            }
        }
        Text(
            text = text,
            fontSize = 14.sp, color = Info
        )
    }
}