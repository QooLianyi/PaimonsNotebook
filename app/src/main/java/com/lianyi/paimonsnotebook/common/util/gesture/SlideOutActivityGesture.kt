package com.lianyi.paimonsnotebook.common.util.gesture

import android.animation.ValueAnimator
import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.ui.graphics.toArgb
import androidx.core.animation.doOnEnd
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.get
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import kotlin.math.abs

class SlideOutActivityGesture(private val activity: Activity) : GestureDetector.OnGestureListener {
    private val xMax by lazy {
        activity.resources.displayMetrics.widthPixels.toFloat()
    }
    private val xMinLimit = 20f
    private val xMin = 0f
    private var currentX = xMin
    private var currentY = xMin

    private var isDragging = false
        private set

    private val decorView by lazy {
        (activity.window.decorView as ViewGroup).apply {
            setBackgroundColor(Black_60.toArgb())
        }
    }
    private val oldScreen by lazy {
        decorView[0]
    }

    private val detector by lazy {
        GestureDetectorCompat(activity, this)
    }

    fun onTouchEvent(motionEvent: MotionEvent?) {
        if (motionEvent != null) {
            detector.onTouchEvent(motionEvent)
        }

        if (motionEvent?.action == MotionEvent.ACTION_UP) {
            onActionUp()
        }
    }

    private fun updateDecorViewState() {
        val alpha = (.6f - currentX / xMax).coerceAtLeast(0f).coerceAtMost(.6f)

        decorView.setBackgroundColor(Black.copy(alpha = alpha).toArgb())
        oldScreen.x = currentX
        isDragging = true
    }

    //离开屏幕
    private fun onActionUp() {
        val anim = ValueAnimator.ofFloat(currentX, if (currentX >= 400) xMax else xMin)
        anim.duration = 300
        anim.addUpdateListener {
            currentX = (it.animatedValue as Float)
            updateDecorViewState()

            if (currentX == xMax) {
                activity.finish()
                activity.overridePendingTransition(0, 0)
            }
        }
        anim.doOnEnd {
            currentY = xMin
        }
        anim.start()
        isDragging = false
    }

    override fun onDown(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y
        repeat(decorView.childCount) {
            val view = decorView.getChildAt(it)

            if (x >= view.left && x < view.right && y >= view.top && y < view.bottom){

            }
        }

        return true
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val absDistanceX = abs(distanceX)
        currentX = if (distanceX > 0) {
            (currentX - distanceX).takeIf { it > xMin } ?: xMin
        } else {
            (currentX + absDistanceX).takeIf { it < xMax } ?: xMax
        }

        updateDecorViewState()
        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return true
    }
}