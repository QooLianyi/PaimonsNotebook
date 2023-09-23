package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

data class OfficialRecommendedPostsData(
    val list: List<OfficialRecommendedPost>
){
    data class OfficialRecommendedPost(
        val banner: String,
        val official_type: Int,
        val post_id: String,
        val subject: String
    )
}