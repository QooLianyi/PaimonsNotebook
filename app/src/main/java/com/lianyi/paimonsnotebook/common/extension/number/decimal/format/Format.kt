package com.lianyi.paimonsnotebook.common.extension.number.decimal.format

import java.text.DecimalFormat

fun Float.format(format: String = "0.##"): String =
    DecimalFormat(format).format(this)