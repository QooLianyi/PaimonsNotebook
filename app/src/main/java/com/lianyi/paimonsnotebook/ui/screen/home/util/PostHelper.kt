package com.lianyi.paimonsnotebook.ui.screen.home.util

object PostHelper {

    //文章tag
    private const val POST_TAG = "/article/"

    //主题tag
    private const val TOPIC_TAG = "/topicDetail/"

    //文章id参数
    const val PARAM_POST_ID = "postId"

    //主题id参数
    const val PARAM_TOPIC_ID = "topicId"

    fun checkUrlType(
        url: String,
        isPostID: (Long) -> Unit,
        isUrl: (String) -> Unit,
        isTopic: (Long) -> Unit
    ) {
        if (url.contains(POST_TAG)) {
            isPostID.invoke(getArticleIdFromUrl(url))
            return
        }

        if (url.contains(TOPIC_TAG)) {
            isTopic.invoke(getTopicIdFromUrl(url))
            return
        }

        /*
        * 都不满足以上条件时,认为是网页url
        *
        * 是网页时,帖子id为网页url
        * */
        isUrl.invoke(url)
    }

    fun getTopicIdFromUrl(url: String): Long {
        val index = url.lastIndexOf(TOPIC_TAG)
        val urlLength = url.length
        val takeCount = urlLength - (index + TOPIC_TAG.length)
        return url.takeLast(takeCount).toLongOrNull() ?: 0L
    }

    private fun getArticleIdFromUrl(url: String): Long {
        val articlePosition = url.lastIndexOf("/") + 1
        val urlLength = url.length
        val takeCount = urlLength - articlePosition
        return url.takeLast(takeCount).split("?").first().toLongOrNull() ?: 0L
    }
}