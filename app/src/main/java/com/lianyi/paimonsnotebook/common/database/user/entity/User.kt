package com.lianyi.paimonsnotebook.common.database.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lianyi.paimonsnotebook.common.database.user.type_converter.CookieConverter
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie

/*
* 用户表
* aid 米游社id
* */

@Entity("users")
@TypeConverters(CookieConverter::class)
data class User(
    val aid: String,
    @PrimaryKey
    val mid: String,
    @ColumnInfo("cookie_token")
    val cookieToken: Cookie,
    val ltoken: Cookie,
    val stoken: Cookie,
    @ColumnInfo("is_selected")
    var isSelected: Boolean
) {
    val cookies:String
        get() = "${cookieToken};${ltoken};${stoken};"
}