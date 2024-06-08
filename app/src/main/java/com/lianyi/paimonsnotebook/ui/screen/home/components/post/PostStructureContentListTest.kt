package com.lianyi.paimonsnotebook.ui.screen.home.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.DividerText
import com.lianyi.paimonsnotebook.common.components.text.FoldTextContent
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.html.RichTextParser
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeStampType
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostStructuredContentData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.StructuredBackupText
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Gray_Dark
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.LinkColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PostStructureContentListTest(
    postFull: PostFullData,
    fontSize: TextUnit = 12.sp,
    onClickLink: (String) -> Unit,
    onClickLinkCard: (String) -> Unit,
    onClickImage: (String) -> Unit,
    onClickVideo: (PostFullData.Post.Vod) -> Unit
) {
    //处理结构内容,将文字与链接整合到一起
    val structuredContent = remember(postFull.post.post.post_id) {
        RichTextParser.parsePostStructuredContent(postFull.post.post.structured_content)
    }

    ContentSpacerLazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp, 6.dp)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                PrimaryText(text = postFull.post.post.subject, fontSize = 18.sp)

                DividerText(
                    text = "文章发表:${
                        TimeHelper.getTime(
                            postFull.post.post.created_at.toLong() * 1000L,
                            TimeStampType.MM_DD
                        ).replace(" ", "-")
                    }", modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .fillMaxWidth()
                )
            }
        }

        itemsIndexed(structuredContent) { i, item ->
            if (item.isEmpty()) return@itemsIndexed

            if (item.size > 1) {
                TextBuildAnnotatedSpannableString(data = item, fontSize = fontSize)
                return@itemsIndexed
            }

            val data = item.first()

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                data.insert.apply {
                    if (insert != null) {
                        TextBuildAnnotatedSpannableString(data = item, fontSize = fontSize)
                    }
                    if (lottery != null) {

                    }
                    if (vod != null) {

                    }
                    if (image != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = data.contentAlignAttribute,
                        ) {
//                                val modifier = remember(image) {
//                                    println("url = ${image}")
//                                    Modifier
//                                        .radius(4.dp)
//                                        .apply {
//                                            if (data.widthDp > maxWidth) {
//                                                println("this.fillMaxWidth()")
//                                                this.fillMaxWidth()
//                                            } else {
//                                                println("this.size(data.widthDp)")
//                                                this.size(data.widthDp)
//                                            }
//                                        }
//                                        .clickable {
//                                            onClickImage.invoke(image)
//                                        }
//                                }
                            NetworkImage(
                                url = image,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                                diskCache = DiskCache(
                                    url = image,
                                    name = "文章详情图片",
                                    description = "阅读文章时加载的配图"
                                )
                            )
                        }
                    }
                    if (link_card != null) {
//                        PostLinkCard(item = link_card, onClick = {
//                            println(it)
//                        })
                    }
                    if (divider != null) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(.5.dp)
                                .background(Black_10)
                        )
                    }
                    if (fold != null) {
                        FoldTextContent(modifier = Modifier.padding(0.dp, 6.dp),
                            titleSlot = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fold_content_icon),
                                    contentDescription = null,
                                    tint = Black,
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
//                                    TextBuildAnnotatedSpannableString(fold.title) {
//                                        Text(
//                                            text = it,
//                                            fontSize = fontSize,
//                                            maxLines = 1,
//                                            overflow = TextOverflow.Ellipsis
//                                        )
//                                    }
                            }
                        ) {
                            Box(modifier = Modifier.padding(8.dp, 0.dp)) {
//                                    TextBuildAnnotatedSpannableString(fold.content) {
//                                        Text(
//                                            text = it,
//                                            fontSize = fontSize,
//                                            lineHeight = fontSize * 1.4444f
//                                        )
//                                    }
                            }
                        }
                    }
                    if (vote != null) {

                    }
                }
            }
        }

//        items(structuredContent) { item ->
//            when (item.first) {
//                StructuredContentType.BackupText -> {
//                    val lottery = item.second.first().insert.backupText?.lottery
//                    if (lottery != null) {
//                        PostBackupTextLottery(
//                            lottery.toast ?: ""
//                        )
//                    }
//                }
//
//                StructuredContentType.Text -> {
//                    TextBuildAnnotatedSpannableString(
//                        data = item.second,
//                        fontSize = fontSize,
//                        block = onClickLink
//                    )
//                }
//
//                StructuredContentType.Vod -> {
//                    val data = item.second.first()
//
//                    if (data.insert.vod != null) {
//                        VideoPlayerPlaceholder(
//                            cover = data.insert.url ?: postFull.post.post.cover,
//                        ) {
//                            onClickVideo.invoke(data.insert.vod)
//                        }
//                    } else {
//                        TextPlaceholder("帖子:${postFull.post.post.post_id}视频内容资源错误")
//                    }
//                }
//
//                StructuredContentType.Image -> {
//                    val data = item.second.first()
//
//                    val imageUrl by lazy {
//                        data.insert.url ?: ""
//                    }
//                    NetworkImage(
//                        url = imageUrl,
//                        modifier = Modifier
//                            .padding(0.dp, 8.dp)
//                            .apply {
//                                val height = data.attributes?.height
//                                val width = data.attributes?.width
//                                with(LocalDensity.current) {
//                                    this@apply.width(width?.toDp() ?: 0.dp)
//                                    this@apply.height(height?.toDp() ?: 0.dp)
//                                }
//                            }
//                            .clip(RoundedCornerShape(4.dp))
//                            .clickable {
//                                onClickImage.invoke(imageUrl)
//                            },
//                        contentScale = ContentScale.FillWidth,
//                        diskCache = DiskCache(
//                            url = item.second.first().insert.url ?: "",
//                            name = "文章详情图片",
//                            description = "阅读文章时加载的配图"
//                        )
//                    )
//                }
//
//                StructuredContentType.LinkCard -> {
//                    val linkCard = item.second.first().insert.linkCard
//                    if (linkCard != null) {
//                        PostLinkCard(item = linkCard) {
//                            onClickLinkCard.invoke(it.origin_url)
//                        }
//                    }
//                }
//
//                StructuredContentType.Fold -> {
//                    val fold = item.second.first().insert.backupText?.fold
//                    if (fold != null) {
//                        FoldTextContent(modifier = Modifier.padding(0.dp, 6.dp),
//                            titleSlot = {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_fold_content_icon),
//                                    contentDescription = null,
//                                    tint = Black,
//                                    modifier = Modifier
//                                        .size(18.dp)
//                                        .graphicsLayer {
//                                            rotationY = 180f
//                                        }
//                                )
//                                Spacer(modifier = Modifier.width(6.dp))
//                                TextBuildAnnotatedSpannableString(fold.getTitle()) {
//                                    Text(
//                                        text = it,
//                                        fontSize = fontSize,
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//                                }
//                            }
//                        ) {
//                            Box(modifier = Modifier.padding(8.dp, 0.dp)) {
//                                TextBuildAnnotatedSpannableString(fold.getContent()) {
//                                    Text(
//                                        text = it,
//                                        fontSize = fontSize,
//                                        lineHeight = fontSize * 1.4444f
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                else -> {}
//            }
//        }

        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                postFull.post.topics.forEach {
                    BoxWithConstraints {
                        Text(
                            text = it.name, fontSize = 10.sp, color = Font_Normal,
                            modifier = Modifier
                                .radius(2.dp)
                                .background(CardBackGroundColor_Gray_Dark)
                                .padding(4.dp, 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.heightIn(6.dp))
        }

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

/*
* 富文本构造组件
* */
@Composable
private fun TextBuildAnnotatedSpannableString(
    data: List<PostStructuredContentData>,
    fontSize: TextUnit,
    block: (String) -> Unit = {},
) {
    val text = buildAnnotatedString {
        data.forEach { item ->
            val textColor =
                if (item.attributes?.color != null) {
                    Color((item.attributes.color).toColorInt())
                } else if (item.attributes?.link != null) {
                    LinkColor
                } else {
                    Black
                }
            val content = item.insert.insert ?: ""

            withStyle(
                style = SpanStyle(
                    fontSize = fontSize,
                    color = textColor,
                    fontWeight = if (item.attributes?.bold == true) FontWeight.SemiBold else FontWeight.Normal,
                    textDecoration = if (item.attributes?.link != null) TextDecoration.Underline else TextDecoration.None
                )
            ) {

                if (item.attributes?.link != null) {
                    val link = item.attributes.link
                    val end = this.length - 1 + (content.length)
                    addStringAnnotation("Link", link, this.length, end)
                }

                append(content)
            }
        }
    }

    val textAlign = remember(data.first().attributes?.align) {
        data.first().textAlignAttribute
    }

    ClickableText(
        text = text,
        onClick = { index ->
            val annotatedString = text.getStringAnnotations("Link", 0, text.length)

            annotatedString.forEach {
                if (index in (it.start..it.end)) {
                    block.invoke(it.item)
                    return@forEach
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            lineHeight = fontSize * 1.4444f,
            textAlign = textAlign
        )
    )
}

@Composable
private fun TextBuildAnnotatedSpannableString(
    data: List<StructuredBackupText.StructuredText>,
    textSlot: @Composable (text: AnnotatedString) -> Unit,
) {
    if (data.isEmpty()) return

    val text = buildAnnotatedString {
        data.forEachIndexed { index, item ->
            val color = if (item.attributes?.color != null) {
                Color((item.attributes.color).toColorInt())
            } else {
                Black
            }
            withStyle(
                style = SpanStyle(
                    color = color
                ),
            ) {
                append(if (index != data.lastIndex) item.insert else item.insert?.removeSuffix("\n"))
            }
        }
    }
    textSlot.invoke(text)
}
