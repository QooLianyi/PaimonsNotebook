package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

/*
* 结构对象链接卡片
* */
data class StructuredLinkCard(
    val button_text: String,
    val card_id: String,
    val card_status: Int,
    val cover: String,
    val landing_url: String,
    val landing_url_type: Int,
    val link_type: Int,
    val market_price: String,
    val origin_url: String,
    val origin_user_avatar:String,
    val origin_user_nickname:String,
    val price: String,
    val title: String,
){
    companion object{
        //商品链接
        val LINK_TYPE_MIHOYO_SHOP = 2
        //内部链接
        val LINK_TYPE_UNIVERSAL = 1
        //状态
        val STATUS_DEFAULT = 1
        val STATUS_DISABLE = 2
        //URL类型
        val URL_TYPE_MIHOYO = 1
        val URL_TYPE_OTHER = 2
    }
}