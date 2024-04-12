package com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.painter_topic

import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData


/*
* 此类经过了精简,将不显示的内容剔除了
* */
data class PainterTopicListData(
    val is_last: Boolean,
    val is_origin: Boolean,
    val list: List<TopicPost>,
    val next_offset: String
) {
    data class TopicPost(
        val entity_id: String,
        val entity_type: Int,
        val game_id: String,
        val id: String,
        /*
        * instant与post只会出现其中一个
        *
        * TODO 完成instant类型的显示
        * instant暂时不处理
        * */
        val instant: Instant?,
        val post: PostFullData.Post,
        val publish_at: String,
        val uid: String
    )

    class Instant

}