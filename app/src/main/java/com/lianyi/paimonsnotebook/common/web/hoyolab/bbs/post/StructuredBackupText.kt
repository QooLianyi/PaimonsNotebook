package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

import com.lianyi.paimonsnotebook.common.util.html.RichTextParser
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType

/*
* 文本集合
* */
data class StructuredBackupText(
    val backup_text: String?,
    val lottery: Lottery?,
    val fold: Fold?,
) {
    data class Lottery(
        val id: String?,
        val toast: String?,
    )

    data class Fold(
        val content: String?,
        val id: String?,
        val size: String?,
        val title: String?,
    ) {
        fun getTitle(): List<PostStructuredContent> =
            JSON.parse<List<StructuredText>>(
                title ?: JSON.EMPTY_LIST,
                getParameterizedType(List::class.java, StructuredText::class.java)
            ).map {
                PostStructuredContent.getTextItem(it.insert, it.attributes)
            }

        /*
        * 此处的insert可能是string或object
        *
        * 返回boolean,list
        * true表示为结构体列表,false表示文本列表
        * */
        fun getContent(): Pair<Boolean, List<Pair<StructuredContentType, List<PostStructuredContent>>>> {
            val list = RichTextParser.parse(this.content ?: JSON.EMPTY_LIST)

            val insert = list.first().insert
            /*
            * 如果转换成功就使用帖子结构体
            * 失败就使用文本
            * */
            return if (
                insert.backupText != null ||
                insert.content != null ||
                insert.url != null ||
                insert.vod != null ||
                insert.linkCard != null
            ) {
                true to RichTextParser.parseGroup(list)
            } else {
                //先获取之前类型的内容
                val textList = JSON.parse<List<StructuredText>>(
                    content ?: "[]",
                    getParameterizedType(List::class.java, StructuredText::class.java)
                )

                false to listOf(
                    StructuredContentType.Text to textList.map {
                        PostStructuredContent.getTextItem(it.insert, it.attributes)
                    }
                )
            }.apply {
                println("content = ${content}")
                println("size = ${this.second.size}")
                println("list = ${this.second}")
            }
        }
    }

    data class StructuredText(
        val insert: String?,
        val attributes: StructuredContentAttributes?
    )
}

