package com.lianyi.paimonsnotebook.common.components.text

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/*
* 富文本文字
*
* 适用于<color=#99FFFFFF>TEXT</color>格式的文字
* 将其转换为Android能够显示的html文本
* */
@Composable
fun RichText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp
) {
    val str = text.replace("<color=#", "<b><font color=\"#")
        .replace("FF>","\">")
        .replace("/color>", "/font></b>")
        .replace("\n","<br>")
    Text(
        text = Html.fromHtml(
            str,
            Html.FROM_HTML_MODE_COMPACT
        ).toAnnotatedString(), modifier = modifier, fontSize = fontSize
    )
}

private fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)

        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ), start, end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start,
                end
            )

            is ForegroundColorSpan -> addStyle(
                SpanStyle(color = Color(span.foregroundColor)),
                start,
                end
            )
        }
    }
}