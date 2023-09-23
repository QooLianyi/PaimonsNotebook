package com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api

data class GetExtListData(
    val code: Int,
    val ext_list: List<String>,
    val msg: String,
    val pkg_list: List<String>,
    val pkg_str: String
)