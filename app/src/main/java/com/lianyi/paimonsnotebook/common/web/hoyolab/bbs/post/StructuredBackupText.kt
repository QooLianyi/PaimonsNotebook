package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

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
    ){
        fun getTitle() = JSON.parse<List<StructuredText>>(title?:"[]", getParameterizedType(List::class.java,StructuredText::class.java))
        fun getContent() = JSON.parse<List<StructuredText>>(content?:"[]", getParameterizedType(List::class.java,StructuredText::class.java))
    }

    data class StructuredText(
        val insert: String?,
        val attributes: StructuredContentAttributes?
    )
}

