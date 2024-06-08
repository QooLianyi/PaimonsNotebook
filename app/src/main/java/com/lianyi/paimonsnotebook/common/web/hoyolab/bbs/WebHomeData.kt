package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

/*
* 网页首页数据
* 此类只使用到了轮播图,其它未用到的数据全部被移除了
* */
data class WebHomeData(
    val carousels: List<Carousel>
) {
    data class Carousel(
        val cover: String,
        val path: String
    )
}