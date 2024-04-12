package com.lianyi.paimonsnotebook.common.extension.date

import java.time.LocalDateTime

fun LocalDateTime.toFormatString() = with(this){
    "${year}-${monthValue}-${dayOfMonth} ${hour}:${minute}:${second}"
}