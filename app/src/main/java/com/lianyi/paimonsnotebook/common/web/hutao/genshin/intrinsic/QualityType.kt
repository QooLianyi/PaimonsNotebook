package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic

import com.lianyi.paimonsnotebook.R

/*
* 稀有度
* */
object QualityType {

    fun getQualityBgByType(type: Int) =
        when (type) {
            QUALITY_WHITE, QUALITY_NONE -> R.drawable.bg_quality_1
            QUALITY_GREEN -> R.drawable.bg_quality_2
            QUALITY_BLUE -> R.drawable.bg_quality_3
            QUALITY_PURPLE -> R.drawable.bg_quality_4
            QUALITY_ORANGE -> R.drawable.bg_quality_5
            QUALITY_ORANGE_SP -> R.drawable.bg_quality_105
            else -> R.drawable.transparent
        }

    fun getStarCountByType(type: Int) =
        when (type) {
            QUALITY_ORANGE_SP -> 5
            else -> type
        }

    const val QUALITY_NONE = 0

    //一星
    const val QUALITY_WHITE = 1

    //二星
    const val QUALITY_GREEN = 2

    //三星
    const val QUALITY_BLUE = 3

    //四星
    const val QUALITY_PURPLE = 4

    //五星
    const val QUALITY_ORANGE = 5

    //限定五星
    const val QUALITY_ORANGE_SP = 105
}