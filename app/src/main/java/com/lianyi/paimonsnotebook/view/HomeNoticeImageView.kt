package com.lianyi.paimonsnotebook.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.ClipDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import com.lianyi.paimonsnotebook.util.dp

class HomeNoticeImageView: androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(drawable!=null){
            if(drawable.intrinsicHeight>240.dp){
                val map = drawable.toBitmap()
                val bitMap = Bitmap.createBitmap(map,0,0,map.width,240.dp.toInt())
                this.setImageBitmap(bitMap)
            }
            if(drawable.intrinsicHeight==240.dp.toInt()){
                this.scaleType = ScaleType.CENTER_CROP
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}