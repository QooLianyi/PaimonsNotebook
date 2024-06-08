package com.lianyi.paimonsnotebook.ui.screen.home.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.text.DividerText
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.widget.RoundedTag
import com.lianyi.paimonsnotebook.common.util.html.RichTextParser
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.ui.theme.Info

/*
* 文章结构内容列表
* 结构解析还是有一些小问题,不修了,凑合用
* */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PostStructureContentList(
    postFull: PostFullData,
    fontSize: TextUnit = 12.sp,
    onClickLink: (String) -> Unit,
    onClickLinkCard: (String) -> Unit,
    onClickImage: (String) -> Unit,
    onClickVideo: (PostFullData.Post.Vod) -> Unit,
    onClickTag: (PostFullData.Post.Topic) -> Unit
) {
    //处理结构内容,将文字与链接整合到一起
    val structuredContent = remember(postFull.post.post.post_id) {
        RichTextParser.parseGroup(postFull.post)
    }

    ContentSpacerLazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp, 6.dp)
    ) {

        //标题
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                PrimaryText(text = postFull.post.post.subject, fontSize = 18.sp)

                DividerText(
                    text = "文章发表:${TimeHelper.getDifferenceTimeText(postFull.post.post.created_at)}",
                    modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .fillMaxWidth()
                )
            }
        }

        //正文内容
        items(structuredContent) { item ->
            PostStructureContentItem(
                fontSize = fontSize,
                item = item,
                onClickLink = onClickLink,
                onClickLinkCard = onClickLinkCard,
                onClickImage = onClickImage,
                onClickVideo = onClickVideo
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        //tags
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                postFull.post.topics.forEach { topic ->
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

        item {
            Spacer(modifier = Modifier.heightIn(8.dp))
        }

        //阅读量
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_page_view),
                        contentDescription = null,
                        tint = Info,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "阅读量${postFull.post.stat.view_num}",
                        fontSize = 12.sp,
                        color = Info
                    )
                }
            }
        }
    }
}
