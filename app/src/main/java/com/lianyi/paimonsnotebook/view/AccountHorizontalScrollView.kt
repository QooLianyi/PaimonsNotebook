package com.lianyi.paimonsnotebook.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import com.lianyi.paimonsnotebook.util.dp

class AccountHorizontalScrollView:HorizontalScrollView {

    //自动回滚分界(dp)
    private val scrollEdge = 120

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        //取消滚动条和顶部波纹
        this.overScrollMode = OVER_SCROLL_NEVER
        this.isHorizontalScrollBarEnabled = false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action){
            MotionEvent.ACTION_UP -> {
                if (scrollX <= scrollEdge.dp) {
                    this.smoothScrollTo(0, 0)
                } else {
                    this.smoothScrollTo(width, 0)
                }
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

}