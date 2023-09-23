package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

/*
* 结构内容属性
* */
data class StructuredContentAttributes(
    //对齐方式 center
    val align: String?,
    //字体加粗
    val bold: Boolean?,
    //链接
    val link: String?,
    //字体颜色 hex
    val color:String?,
    //宽高
    val height: Int?,
    val width: Int?,
)
