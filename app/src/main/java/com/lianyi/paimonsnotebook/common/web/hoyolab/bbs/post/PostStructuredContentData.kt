package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.lianyi.paimonsnotebook.common.extension.value.pxToDp

data class PostStructuredContentData(
    val attributes: Attributes?,
    val insert: Insert
) {
    data class Attributes(
        //图片
        val height: Int?,
        val width: Int?,
        val size: Int?,
        val ext: String?,
        //文字
        val align: String?,
        val color: String?,
        val bold: Boolean?,
        //链接
        val link: String?,
    )

    val widthDp: Dp by lazy {
        (attributes?.width ?: 0).pxToDp()
    }

    val heightDp: Dp by lazy {
        (attributes?.height ?: 0).pxToDp()
    }

    //获取文字对齐方向
    val textAlignAttribute
        get() = when (attributes?.align) {
            "center" -> TextAlign.Center
            "justify" -> TextAlign.Justify
            "start", "left" -> TextAlign.Start
            "end", "right" -> TextAlign.End
            else -> TextAlign.Start
        }

    //获取布局对齐方向
    val contentAlignAttribute: Arrangement.Horizontal
        get() = when (attributes?.align) {
            "center" -> Arrangement.Center
            "start", "left" -> Arrangement.Start
            "end", "right" -> Arrangement.End
            else -> Arrangement.Start
        }

    data class Insert(
        //原本insert占位
        val insert: String?,
        //抽奖
        val backup_text: String?,
        val lottery: Lottery?,
        //图片
        val image: String?,
        //链接卡片
        val link_card: LinkCard?,
        //分割线
        val divider: String?,
        //折叠内容
        val fold: Fold?,
        //视频
        val vod: Vod?,
        //投票
        val vote: Vote?
    ) {

        data class Vote(
            val id: String,
            val uid: String
        )

        data class Vod(
            val cover: String,
            val duration: Int,
            val id: String,
            val resolutions: List<Resolution>,
            val review_status: Int,
            val transcoding_status: Int,
            val view_num: Int
        ) {
            data class Resolution(
                val bitrate: Int,
                val definition: String,
                val format: String,
                val height: Int,
                val label: String,
                val size: Int,
                val url: String,
                val width: Int
            )
        }


        data class Fold(
            val content: List<Content>,
            val id: String,
            val size: String,
            val title: List<Title>
        ) {
            data class Content(
                val insert: String,
                val attributes: Attributes?
            )

            data class Title(
                val insert: String
            )
        }

        data class Lottery(
            val id: String,
            val toast: String
        )

        data class LinkCard(
            val button_text: String,
            val card_id: String,
            val card_status: Int,
            val cover: String,
            val landing_url: String,
            val landing_url_type: Int,
            val link_type: Int,
            val market_price: String,
            val origin_url: String,
            val price: String,
            val title: String
        )
    }
}