package com.lianyi.paimonsnotebook.common.util.html

import android.content.Context
import android.content.Intent
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import coil.imageLoader
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.view.WebViewScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun setTextLinkOpenByWebView(
    context: Context,
    htmlText: String,
    useWebView: Boolean = false,
    block: (SpannableStringBuilder) -> Unit,
) {
    thread {
        if (htmlText.isNotBlank()) {
            val density = context.resources.displayMetrics.density
            val textContent = HtmlCompat.fromHtml(htmlText,
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                {
                    runBlocking(Dispatchers.IO) {
                        val request =
                            ImageRequest.Builder(context)
                                .data(it)
                                .build()
                        var drawable = request.context.imageLoader.execute(request).drawable
                        drawable = drawable ?: ContextCompat.getDrawable(context, R.drawable.icon_klee)!!.current

                        val screenWidth = context.resources.displayMetrics.widthPixels - (30 * density).toInt()
                        //计算缩放比例
                        val scaleValue =
                            screenWidth / drawable.intrinsicWidth.toFloat()

                        val drawableHeight =
                            scaleValue * drawable.intrinsicHeight

                        drawable.setBounds(0,
                            0,
                            screenWidth,
                            drawableHeight.toInt())
                        drawable
                    }
                }, null)
            if (textContent is SpannableStringBuilder) {
                if (useWebView) {
                    textContent.apply {
                        val a = getSpans(0, length, URLSpan::class.java)
                        if (a.isNotEmpty()) {
                            a.forEach { urlSpan ->
                                val startIndex = getSpanStart(urlSpan)
                                val endIndex = getSpanEnd(urlSpan)
                                if (urlSpan is URLSpan) {
                                    val url = urlSpan.url
                                    removeSpan(urlSpan)
                                    setSpan(object : ClickableSpan() {
                                        override fun updateDrawState(ds: TextPaint) {
                                            ds.color = Color.Gray.toArgb()
                                            super.updateDrawState(ds)
                                        }

                                        override fun onClick(widget: View) {
                                            context.startActivity(Intent(context,
                                                WebViewScreen::class.java).apply {
                                                putExtra("url", url)
                                            })
                                        }
                                    }, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                                }
                            }
                        }
                    }
                }

                block(textContent)
            }
        } else {
            block(SpannableStringBuilder(htmlText))
        }
    }
}