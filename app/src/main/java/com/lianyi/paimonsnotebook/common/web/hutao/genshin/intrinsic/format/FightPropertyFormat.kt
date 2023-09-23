package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import kotlin.math.roundToInt

/*
* 战斗属性格式化
*
* property:战斗属性id
* value:值
* */
data class FightPropertyFormat(
    val property: Int,
    val value: Float
) {
    //属性名称
    val name: String
        get() = FightProperty.getNameByProperty(property)

    //属性图标资源
    val iconResource:Int
        get() = FightProperty.getIconResourceByProperty(property)

    val formatValue: String
        get() =
            when (FormatMethod.getFormatMethod(property)) {
                FormatMethod.Method.Integer -> "${value.roundToInt()}"
                FormatMethod.Method.Percent -> "${"%.1f".format(value * 100)}%"
                else -> "$value"
            }
}
