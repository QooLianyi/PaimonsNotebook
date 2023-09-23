package com.lianyi.paimonsnotebook.common.data.html

import android.text.SpannableString
import android.text.Spanned
import androidx.compose.ui.graphics.Color
import com.lianyi.paimonsnotebook.ui.theme.Black

/*
* HTML文本数据
* 用于记录解析的文字内容、颜色、超链接
*
* text:内容
* spannableString:用于设置可点击的文本
* color:颜色
* start:字符在整个文本中开始的位置
* end:字符在整个文本中结束的位置
* annotation:注释，一般用于记录A标签的href
* */
data class HtmlTextData(
    val text: String = "",
    val spannableString: Spanned = SpannableString(""),
    val color: Color = Black,
    val start: Int = 0,
    val end: Int = 0,
    val annotation: String = "",
)
