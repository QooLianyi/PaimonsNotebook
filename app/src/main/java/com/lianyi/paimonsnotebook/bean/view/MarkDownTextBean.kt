package com.lianyi.paimonsnotebook.bean.view

data class MarkDownTextBean(val level:Int,val text:String,val textType:MarkDownTextType,val listStyle:MarkDownTextType) {
}

enum class MarkDownTextType{
    TEXT,
    H1,H2,H3,H4,H5,
    BR,
    ORDER_LIST,
    DISORDER_LIST,
    NORMAL
}