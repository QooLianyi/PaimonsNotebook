package com.lianyi.paimonsnotebook.ui.screen.home.util

object PostHelper {

    private const val TAG = "/article/"

    const val PARAM_POST_ID = "postId"
    const val PARAM_WEB_STATIC_URL = "webstaticUrl"

    fun checkUrlType(
        url: String,
        isPostID:(Long)->Unit,
        isUrl:(String)->Unit
    ){
        if (url.contains(TAG)) {
            isPostID.invoke(getArticleIdFromUrl(url))
        } else {
            //网页时，帖子id为网页url
            isUrl.invoke(url)
        }
    }

    fun getArticleIdFromUrl(url: String): Long {
        val articlePosition = url.lastIndexOf("/") + 1
        val urlLength = url.length
        val takeCount = urlLength - articlePosition
        return url.takeLast(takeCount).split("?").first().toLongOrNull() ?: 0L
    }
}