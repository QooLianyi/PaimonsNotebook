package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.media.FullScreenImage
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.placeholder.TextPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.VideoPlayerPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.FoldTextContent
import com.lianyi.paimonsnotebook.common.data.html.HtmlSpanData
import com.lianyi.paimonsnotebook.common.data.html.HtmlTextData
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.toAnnotatedString
import com.lianyi.paimonsnotebook.common.util.html.HtmlSpanType
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.LinkColor
import okhttp3.internal.toHexString
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
* 此组件仅用于转换文章详情页的html文本
* 显示其他html需要特定的转换规则
*
* htmlText:解析的html文本,仅支持解析米游社文章详情接口获取的html文本,其他文本需要设置特定的解析规则
* horizontalPadding:item的水平外边距
* verticalPadding:item的垂直外边距
* onHyperlinkClick:当超链接被点击时
* content
*
* */
@Composable
fun HtmlTextLazyColumn(
    htmlText: String,
    diskCacheTemplate: DiskCache,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 5.dp,
    fontSize: TextUnit = 16.sp,
    videoCover: String = "",
    onHyperlinkClick: (url: String) -> Unit = {},
    onVideoClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val htmlSpanData = getHtmlSpanDataList(htmlText)

    var imageFullScreen by remember {
        mutableStateOf(false)
    }
    var imageUrl by remember {
        mutableStateOf("")
    }

    ContentSpacerLazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        item {
            Column(
                modifier = Modifier.padding(
                    horizontalPadding,
                    verticalPadding,
                    horizontalPadding,
                    0.dp
                )
            ) {
                content()
            }
        }

        if (videoCover.isNotBlank()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontalPadding,
                            verticalPadding / 2
                        )
                ) {
                    VideoPlayerPlaceholder(
                        cover = videoCover
                    ) {
                        onVideoClick()
                    }
                }
            }
        }

        items(htmlSpanData) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontalPadding,
                        if (item.type == HtmlSpanType.Img) verticalPadding else verticalPadding / 2
                    ),
                horizontalAlignment = item.alignment
            ) {
                when (item.type) {
                    HtmlSpanType.Img -> {

                        NetworkImage(
                            url = item.data,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    imageUrl = item.data
                                    imageFullScreen = true
                                },
                            contentScale = ContentScale.FillWidth,
                            diskCache = diskCacheTemplate.copy(
                                url = item.data,
                                name = "文章详情图片",
                                description = "阅读文章时加载的配图"
                            )
                        )
                    }

                    HtmlSpanType.A, HtmlSpanType.SP -> {

                        TextBuildAnnotatedSpannableString(
                            data = item.textList,
                            fontSize = fontSize
                        ) {
                            onHyperlinkClick(it)
                        }
                    }

                    HtmlSpanType.P -> {
                        TextBuildAnnotatedString(data = item.textList, fontSize = fontSize)
                    }

                    HtmlSpanType.Video -> {
                        VideoPlayerPlaceholder(
                            cover = videoCover
                        ) {
                            onVideoClick()
                        }
                    }

                    HtmlSpanType.Fold -> {

                        FoldTextContent(titleSlot = {
                            TextBuildAnnotatedString(data = item.titleList, fontSize = fontSize)
//                            TextBuildAnnotatedSpannableString(data = item.titleList, fontSize = fontSize)
                        }, textContentSlot = {
                            Column(modifier = Modifier.padding(10.dp)) {
                                TextBuildAnnotatedString(data = item.textList, fontSize = fontSize)
//                                TextBuildAnnotatedSpannableString(data = item.textList, fontSize = fontSize)
                            }
                        })

                    }

                    HtmlSpanType.LinkCard -> {

                    }

                    else -> {
                        TextPlaceholder("此处使用了一个预料外的标签:[${item.data}]。\n向开发者反馈以解决此问题")
                    }
                }
            }
        }
    }

    if (imageFullScreen) {
        Dialog(
            onDismissRequest = { imageFullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                FullScreenImage(url = imageUrl, onClick = {
                    imageFullScreen = false
                })
            }
        }
    }
}

@Composable
private fun TextBuildAnnotatedSpannableString(
    data: List<HtmlTextData>,
    fontSize: TextUnit,
    block: (String) -> Unit = {},
) {

    val text = buildAnnotatedString {
        data.forEach { item ->
            withStyle(style = SpanStyle(fontSize = fontSize, color = item.color)) {
                append(item.spannableString.toSpannable().toAnnotatedString(LinkColor))
            }
        }
    }
    ClickableText(text = text, onClick = { index ->
        val annotatedString = text.getStringAnnotations("URL", 0, text.length)

        annotatedString.forEach {
            if (index in (it.start..it.end)) {
                block(it.item)
                return@forEach
            }
        }
    })
}


@Composable
private fun TextBuildAnnotatedString(
    data: List<HtmlTextData>,
    fontSize: TextUnit,
) {
    val text = buildAnnotatedString {
        data.forEach { item ->
            withStyle(style = SpanStyle(color = item.color, fontSize = fontSize)) {
                append(item.text)
            }
        }
    }
    Text(text = text)
}

//解析html内标签,包括颜色、样式等属性。
private fun getHtmlSpanDataList(htmlText: String): List<HtmlSpanData> {
    val document = Jsoup.parse(htmlText)
    //获取顶层层级
    val body = document.body().children()

    val htmlSpanData = mutableListOf<HtmlSpanData>()

    //此处根据获取到的标签名(it.tagName)来生成对应的组件
    if (body.size > 0) {

        body.forEach { parent ->

            var alignment = Alignment.Start
            var spanType = HtmlSpanType.P
            var data = ""
            val textList = mutableListOf<HtmlTextData>()
            val titleList = mutableListOf<HtmlTextData>()

            when (parent.tagName()) {
                //文字类型
                "p" -> {

                    alignment = getAlign(parent)

                    val parentHtmlText = parent.html()

                    val text =
                        HtmlCompat.fromHtml(parentHtmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    textList += HtmlTextData(spannableString = text)
                    spanType = HtmlSpanType.SP
                }

                //块类型 一般用于装填媒体内容,可折叠文本
                "div" -> {

                    spanType =
                        if (parent.classNames().contains("ql-fold")) {
                            //折叠文本
                            parent.getElementsByClass("ql-fold-title-content").first()
                                ?.getElementsByTag("span")
                                ?.forEach { span ->
                                    titleList += HtmlTextData(
                                        text = span.text(),
                                        color = getTextColorInStyle(span)
                                    )
                                }

                            parent.getElementsByClass("ql-fold-content").first()
                                ?.getElementsByTag("p")?.forEach { p ->
                                    p?.getElementsByTag("span")?.forEach { text ->
                                        textList += HtmlTextData(
                                            text = text.text(),
                                            color = getTextColorInStyle(text)
                                        )
                                    }
                                    textList += HtmlTextData(
                                        text = "\n",
                                        color = Color.Transparent
                                    )
                                }

                            HtmlSpanType.Fold
                        } else if (parent.classNames().contains("ql-link-card")) {
                            //外部链接
                            val coverElement = parent.getElementsByClass("card-cover")
                            if (coverElement.size > 0) {
                                val cover = coverElement.first()
                                val style = cover?.attr("style")
                                style?.apply {
                                    this.split(";").forEach { kv ->
                                        val index = kv.indexOfFirst { it == ':' }

                                        if (index != -1) {
                                            val k = kv.slice((0 until index)).trim()
                                            val v = kv.slice((index + 1 until kv.length)).trim()

                                            if (k == "background-image") {
                                                val imageUrl =
                                                    v.removePrefix("url(\"").removeSuffix("\")")
                                                data = imageUrl
                                            }

                                        }
                                    }
                                }
                            }

                            titleList += HtmlTextData(
                                text = parent.getElementsByClass("card-title").text()
                            )

                            HtmlSpanType.LinkCard
                        } else {
                            if (parent.childrenSize() == 0) {
                                HtmlSpanType.Video
                            } else if (parent.children().firstOrNull()?.tagName() == "iframe") {
                                data = parent.children().attr("video")
                                HtmlSpanType.Video
                            } else {
                                data = parent.getElementsByTag("img").first()?.attr("src")
                                    ?: "https://img-static.mihoyo.com/communityweb/upload/417976a3dacde790f947f8769d85d55c.png"
                                HtmlSpanType.Img
                            }
                        }

                }
                //默认处理
                else -> {
                    data = parent.tagName()
                }
            }

            htmlSpanData += HtmlSpanData(
                type = spanType,
                data = data,
                textList = textList,
                alignment = alignment,
                clickable = false,
                titleList = titleList
            )
        }
    } else {
        htmlSpanData += HtmlSpanData(
            textList = listOf(
                HtmlTextData(
                    text = htmlText,
                    color = Color.Black
                )
            )
        )
    }

    return htmlSpanData
}

//获取元素中的对齐方式class,无返回TopStart
private fun getAlign(element: Element): Alignment.Horizontal {
    val elementClass = element.classNames()
    return if ("ql-align-center" in elementClass) {
        Alignment.CenterHorizontally
    } else {
        Alignment.Start
    }
}

private fun getColorFromRgbColorText(colorString: String): Color {
    val (r, g, b) = getRGBFromRgbColorText(colorString)

    return Color(r, g, b)
}

private fun getRGBFromRgbColorText(colorString: String): Triple<Int, Int, Int> {
    val rgbColor = colorString.replace("rgb(", "").replace(")", "").split(",")
    val r = rgbColor.first().trim().toInt()
    val g = rgbColor[1].trim().toInt()
    val b = rgbColor.last().trim().toInt()

    return Triple(r, g, b)
}

private fun getColorHexFromRgbColorText(colorString: String): String {
    val (r, g, b) = getRGBFromRgbColorText(colorString)
    return android.graphics.Color.rgb(r, g, b).toHexString()
}

//获取元素中style内color的值
private fun getTextColorInStyle(element: Element): Color {
    var color = Black

    val style = element.attr("style")
    val attrs = style.split(";")
    attrs.forEach {
        if (it.startsWith("color")) {
            val colorString = it.split(":").last().trim()
            color = if (colorString.startsWith("rgb")) {
                getColorFromRgbColorText(colorString)
            } else {
                //当使用的不是常见的颜色单词时,颜色转换可能会发生异常
                try {
                    Color(colorString.toColorInt())
                } catch (e: Exception) {
                    "异常的颜色转换:${colorString}\n请向开发者反馈".show()
                    Color.Black
                }
            }
        }
    }
    return color
}
