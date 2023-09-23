package com.lianyi.paimonsnotebook.common.util.html

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import com.lianyi.paimonsnotebook.common.extension.string.show

//自定义链接点击事件
object CustomLinkMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(
        widget: TextView?,
        buffer: Spannable?,
        event: MotionEvent?,
    ): Boolean {
        val action = event?.action

        if (action == MotionEvent.ACTION_UP) {
            val x = event.x - (widget?.totalPaddingLeft ?: 0) + (widget?.scrollX ?: 0)
            val y = event.y - (widget?.totalPaddingTop ?: 0) + (widget?.scrollY ?: 0)
            val layout = widget?.layout
            val line = layout?.getLineForVertical(y.toInt()) ?: 0
            val off = layout?.getOffsetForHorizontal(line, x) ?: 0
            val spans = buffer?.getSpans(off, off, URLSpan::class.java)
            if (!spans.isNullOrEmpty()) {
                "点击:${spans.first().url}".show()
                return true
            }
        }

        return super.onTouchEvent(widget, buffer, event)
    }
}