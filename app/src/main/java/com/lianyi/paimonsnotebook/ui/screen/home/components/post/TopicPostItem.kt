package com.lianyi.paimonsnotebook.ui.screen.home.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.widget.RoundedTag
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.painter_topic.PainterTopicListData
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun TopicPostItem(
    item: PainterTopicListData.TopicPost,
    onClickPostListItem: (PainterTopicListData.TopicPost) -> Unit,
    onClickTag: (PostFullData.Post.Topic) -> Unit
) {
    //如果此项不为空,表示post一定为空,直接返回
    if (item.instant != null) return

    val imageList = item.post.image_list
    val videoList = item.post.vod_list

    val cover = if (imageList.isNotEmpty()) {
        imageList.first().url
    } else if (videoList.isNotEmpty()) {
        videoList.first().cover
    } else {
        ""
    }

    Column(
        modifier = Modifier
            .zIndex(0f)
            .radius(8.dp)
            .fillMaxWidth()
            .background(CardBackGroundColor_Light_1)
            .clickable {
                onClickPostListItem.invoke(item)
            }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            NetworkImage(
                url = item.post.user.avatar_url,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(38.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                com.lianyi.core.ui.components.text.PrimaryText(
                    text = item.post.user.nickname,
                    textSize = 13.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                com.lianyi.core.ui.components.text.InfoText(text = "uid:${item.post.user.uid}")
            }

            Spacer(modifier = Modifier.width(8.dp))

            com.lianyi.core.ui.components.text.InfoText(text = TimeHelper.getDifferenceTimeText(item.post.post.created_at))
        }

        if (cover.isNotEmpty()) {
            NetworkImage(
                url = cover,
                modifier = Modifier
                    .radius(4.dp)
                    .fillMaxWidth()
                    .heightIn(1.dp, 360.dp),
                contentScale = ContentScale.FillWidth
            )
        }

        //帖子数据
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_thumb_up_outline),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            com.lianyi.core.ui.components.text.InfoText(text = "${item.post.stat.like_num}")

            Icon(
                painter = painterResource(id = R.drawable.ic_comment_outline),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            com.lianyi.core.ui.components.text.InfoText(text = "${item.post.stat.reply_num}")
        }

        com.lianyi.core.ui.components.text.PrimaryText(text = item.post.post.subject)

        com.lianyi.core.ui.components.text.InfoText(text = item.post.post.summary)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item.post.topics.forEach { topic ->
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClickTag.invoke(topic)
                    }
                ) {
                    RoundedTag(topic.name)
                }
            }
        }
    }
}