package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

data class PostStructuredContent(
    val type: StructuredContentType,
    val attributes: StructuredContentAttributes?,
    val insert: Item,
) {
    data class Item(
        val vod: StructuredVod? = null,
        val linkCard: StructuredLinkCard? = null,
        val backupText: StructuredBackupText? = null,
        val url: String? = null,
        val content:String? = null
    )
}
