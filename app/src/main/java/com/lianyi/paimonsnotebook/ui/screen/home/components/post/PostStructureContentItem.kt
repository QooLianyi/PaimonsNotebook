package com.lianyi.paimonsnotebook.ui.screen.home.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.placeholder.TextPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.VideoPlayerPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.FoldTextContent
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostStructuredContent
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.StructuredContentType
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.LinkColor

/*
* 帖子结构内容列表项
*
* TODO 待优化 如item的类型可以通过list.first获取
* */
@Composable
fun PostStructureContentItem(
    fontSize: TextUnit,
    item: Pair<StructuredContentType, List<PostStructuredContent>>,
    onClickLink: (String) -> Unit,
    onClickLinkCard: (String) -> Unit,
    onClickImage: (String) -> Unit,
    onClickVideo: (PostFullData.Post.Vod) -> Unit
) {
    when (item.first) {
        StructuredContentType.BackupText -> {
            val lottery = item.second.first().insert.backupText?.lottery
            if (lottery != null) {
                PostBackupTextLottery(
                    lottery.toast ?: ""
                )
            }
        }

        StructuredContentType.Text -> {
            TextBuildAnnotatedSpannableString(
                data = item.second,
                fontSize = fontSize,
                onClickLink = onClickLink
            )
        }

        StructuredContentType.Vod -> {
            val insert = item.second.first().insert

            if (insert.vod != null) {
                Column {
                    VideoPlayerPlaceholder(
                        cover = insert.url ?: insert.vod.cover,
                    ) {
                        onClickVideo.invoke(insert.vod)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                TextPlaceholder("帖子视频内容资源错误")
            }
        }

        StructuredContentType.Image -> {
            val data = item.second.first()

            val imageUrl by lazy {
                data.insert.url ?: ""
            }
            NetworkImage(
                url = imageUrl,
                modifier = Modifier
                    .padding(0.dp, 8.dp)
                    .apply {
                        val height = data.attributes?.height
                        val width = data.attributes?.width
                        with(LocalDensity.current) {
                            this@apply.width(width?.toDp() ?: 0.dp)
                            this@apply.height(height?.toDp() ?: 0.dp)
                        }
                    }
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        onClickImage.invoke(imageUrl)
                    },
                contentScale = ContentScale.FillWidth,
                diskCache = DiskCache(
                    url = item.second.first().insert.url ?: "",
                    name = "文章详情图片",
                    description = "阅读文章时加载的配图"
                )
            )
        }

        StructuredContentType.LinkCard -> {
            val linkCard = item.second.first().insert.linkCard
            if (linkCard != null) {
                PostLinkCard(item = linkCard) {
                    onClickLinkCard.invoke(it.origin_url)
                }
            }
        }

        StructuredContentType.Fold -> {
            val fold = item.second.first().insert.backupText?.fold
            if (fold != null) {
                FoldTextContent(modifier = Modifier.padding(0.dp, 6.dp),
                    backgroundColor = CardBackGroundColor_Light_1,
                    borderColor = Black_10,
                    titleSlot = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_fold_content_icon),
                            contentDescription = null,
                            tint = Black,
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer {
                                    rotationY = 180f
                                }
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        TextBuildAnnotatedSpannableString(fold.getTitle()) {
                            Text(
                                text = it,
                                fontSize = fontSize,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                ) {

                    val contentPair = remember(item.first) {
                        fold.getContent()
                    }

                    /*
                    * 此处的fold不止包含文字,也可能包含图片
                    *
                    * 需要递归调用自身组件来完成渲染
                    * */
                    if (contentPair.first) {
                        contentPair.second.forEach { foldItem ->
                            PostStructureContentItem(
                                fontSize = fontSize,
                                item = foldItem,
                                onClickLink = onClickLink,
                                onClickLinkCard = onClickLinkCard,
                                onClickImage = onClickImage,
                                onClickVideo = onClickVideo
                            )
                        }
                    } else {
                        val list = contentPair.second.first().second
                        TextBuildAnnotatedSpannableString(list, fontSize, onClickLink)
                    }
                }
            }
        }

        else -> {}
    }
}


/*
* 富文本构造组件
* */
@Composable
private fun TextBuildAnnotatedSpannableString(
    data: List<PostStructuredContent>,
    fontSize: TextUnit,
    onClickLink: (String) -> Unit = {},
) {
    //防止空集合
    if (data.isEmpty()) return
    //防止无意义的换行
    if (data.size == 1 && (data.first().insert.content == null || data.first().insert.content == " \n" || data.first().insert.content == "\n")) return

    val text = buildAnnotatedString {
        data.forEach { item ->
            val textColor =
                if (item.attributes?.color != null) {
                    Color((item.attributes.color).toColorInt())
                } else if (item.type == StructuredContentType.Link) {
                    LinkColor
                } else {
                    Black
                }
            val content = item.insert.content

            withStyle(
                style = SpanStyle(
                    fontSize = fontSize,
                    color = textColor,
                    fontWeight = if (item.attributes?.bold == true) FontWeight.SemiBold else FontWeight.Normal
                ),
            ) {

                if (item.type == StructuredContentType.Link) {
                    val link = item.attributes?.link ?: ""
                    val end = this.length - 1 + (content?.length ?: 0)
                    addStringAnnotation("Link", link, this.length, end)
                }

                append(content)
            }
        }
    }

    //根据连续文本的最后一个的对齐属性设置对齐方式
    val attrAlign = data.last().attributes?.align
    val textAlign = remember(attrAlign) {
        if (attrAlign == "center") {
            TextAlign.Center
        } else {
            TextAlign.Start
        }
    }

    ClickableText(
        text = text,
        onClick = { index ->
            val annotatedString = text.getStringAnnotations("Link", 0, text.length)

            annotatedString.forEach {
                if (index in (it.start..it.end)) {
                    onClickLink.invoke(it.item)
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
    data: List<PostStructuredContent>,
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
                append(
                    if (index != data.lastIndex) item.insert.content
                    else
                        item.insert.content?.removeSuffix("\n")
                )
            }
        }
    }
    textSlot.invoke(text)
}
