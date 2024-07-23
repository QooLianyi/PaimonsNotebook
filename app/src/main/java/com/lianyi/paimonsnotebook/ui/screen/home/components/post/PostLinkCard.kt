package com.lianyi.paimonsnotebook.ui.screen.home.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostLinkCardType
import com.lianyi.paimonsnotebook.ui.theme.LinkColor
import com.lianyi.paimonsnotebook.ui.theme.PriceColor
import com.lianyi.paimonsnotebook.ui.theme.White_50

@Composable
internal fun PostLinkCard(
    item: PostFullData.Post.LinkCard,
    onClick: (PostFullData.Post.LinkCard) -> Unit,
) {
    val (size, textContentAlign) = remember(item.link_type) {
        when (item.link_type) {
            PostLinkCardType.LINK_TYPE_UNIVERSAL -> {
                48.dp to Arrangement.Center
            }

            PostLinkCardType.LINK_TYPE_MIHOYO_SHOP -> {
                72.dp to Arrangement.SpaceBetween
            }

            else -> {
                48.dp to Arrangement.Center
            }
        }
    }

    val diskCache = remember(item.cover) {
        DiskCache(
            url = item.cover,
            createFrom = "帖子详情",
            lastUseFrom = "帖子详情",
            name = "帖子卡片图标",
            description = "帖子详情中卡片的图标"
        )
    }

    Row(
        modifier = Modifier
            .radius(4.dp)
            .background(White_50)
            .clickable {
                onClick.invoke(item)
            }
            .padding(6.dp)
            .fillMaxWidth()
    ) {
        NetworkImage(
            diskCache = diskCache,
            modifier = Modifier
                .radius(2.dp)
                .size(size),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .height(size)
                .fillMaxWidth(),
            verticalArrangement = textContentAlign
        ) {
            Text(text = item.title, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)

            if (item.link_type == PostLinkCardType.LINK_TYPE_MIHOYO_SHOP) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.price,
                        fontSize = 10.sp,
                        color = PriceColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = "${item.button_text}>", fontSize = 10.sp, color = LinkColor)
                }
            }
        }
    }
}