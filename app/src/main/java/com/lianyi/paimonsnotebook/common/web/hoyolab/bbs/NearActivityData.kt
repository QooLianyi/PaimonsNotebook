package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

data class NearActivityData(
    val list: List<Hots>
) {
    data class Hots(
        val ch_ext: String,
        val children: List<Group2>,
        val depth: Int,
        val id: Int,
        val list: List<Any>,
        val name: String,
        val parent_id: Int
    ) {
        data class Group2(
            val ch_ext: String,
            val children: List<Children>,
            val depth: Int,
            val id: Int,
            val list: List<Any>,
            val name: String,
            val parent_id: Int
        ) {
            data class Children(
                val ch_ext: String,
                val children: List<Any>,
                val depth: Int,
                val id: Int,
                val list: List<NearActivity>,
                val name: String,
                val parent_id: Int
            ) {
                data class NearActivity(
                    val `abstract`: String,
                    val article_time: String,
                    val article_user_name: String,
                    val avatar_url: String,
                    val content_id: Int,
                    val create_time: String,
                    val end_time: String,
                    val ext: String,
                    val icon: String,
                    val recommend_id: Int,
                    val title: String,
                    val type: Int,
                    val url: String
                )
            }
        }
    }
}