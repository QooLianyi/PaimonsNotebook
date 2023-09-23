package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.common

/*
* 祈愿活动
* */
data class GachaPoolData(
    val list: List<Pool>
){
    data class Pool(
        val activity_url: String,
        val content_before_act: String,
        val end_time: String,
        val id: Int,
        val pool: List<PoolInfo>,
        val start_time: String,
        val title: String,
        val voice_icon: String,
        val voice_status: Int,
        val voice_url: String
    ){
        data class PoolInfo(
            val icon: String,
            val url: String
        )
    }
}

