package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic

/*
* 地区
* */
object AssociationType {

    fun getAssociationNameByType(type: Int) =
        when (type) {
            ASSOC_TYPE_MONDSTADT -> "蒙德"
            ASSOC_TYPE_LIYUE -> "璃月"
            ASSOC_TYPE_INAZUMA -> "稻妻"
            ASSOC_TYPE_SUMERU -> "须弥"
            ASSOC_TYPE_FONTAINE -> "枫丹"
            ASSOC_TYPE_NATLAN -> "纳塔"
            ASSOC_TYPE_SNEZHNAYA -> "至冬"
            ASSOC_TYPE_FATUI-> "愚人众"
            else -> ""
        }

    //无
    const val ASSOC_TYPE_NONE = 0

    //蒙德
    const val ASSOC_TYPE_MONDSTADT = 1

    //璃月
    const val ASSOC_TYPE_LIYUE = 2

    //主角
    const val ASSOC_TYPE_MAINACTOR = 3

    //愚人众
    const val ASSOC_TYPE_FATUI = 4

    //稻妻
    const val ASSOC_TYPE_INAZUMA = 5

    //游侠
    const val ASSOC_TYPE_RANGER = 6

    //须弥
    const val ASSOC_TYPE_SUMERU = 7

    //枫丹
    const val ASSOC_TYPE_FONTAINE = 8

    //纳塔
    const val ASSOC_TYPE_NATLAN = 9

    //至冬
    const val ASSOC_TYPE_SNEZHNAYA = 10
}