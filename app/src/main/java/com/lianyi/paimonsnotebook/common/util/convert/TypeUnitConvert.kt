package com.lianyi.paimonsnotebook.common.util.convert

import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue

object TypeUnitConvert {

    fun dimensionToPx(value: Number, type: Int, metrics: DisplayMetrics): Float {
        val fValue = value.toFloat()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            TypedValue.convertDimensionToPixels(
                type,
                fValue,
                metrics
            )
        } else {
            fValue * when (type) {
                TypedValue.COMPLEX_UNIT_SP -> metrics.scaledDensity
                else -> metrics.density
            }
        }
    }

    fun spToPx(value: Number, metrics: DisplayMetrics) =
        dimensionToPx(value = value, type = TypedValue.COMPLEX_UNIT_SP, metrics)

    fun dpToPx(value: Number, metrics: DisplayMetrics) =
        dimensionToPx(value = value, type = TypedValue.COMPLEX_UNIT_DIP, metrics)

}