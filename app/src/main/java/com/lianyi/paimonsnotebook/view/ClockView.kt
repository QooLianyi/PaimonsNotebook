package com.lianyi.paimonsnotebook.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.lianyi.paimonsnotebook.R
import kotlin.math.*

class ClockView: FrameLayout {

    private val iconDusk = ImageView(context)
    private val iconNoon = ImageView(context)
    private val iconNight = ImageView(context)
    private val iconDawn = ImageView(context)
    private val pointerLong = ImageView(context)
    private val pointerShort = ImageView(context)
    private val clockBody = ImageView(context)
    private val clockBackground = ImageView(context)

    private val gearCenter = ImageView(context)
    private val gearS = ImageView(context)
    private val gearM = ImageView(context)
    private val gearL = ImageView(context)
    private val gearXL = ImageView(context)

    private var back = false
    private var preQuadrant = 1
    private var gearSpeed = 1L

    private val gearAnimator = mutableListOf<ObjectAnimator>()

    private val processOuter = ProgressBar(context,null,0,android.R.attr.progressBarStyleHorizontal)
    private val processInner = ProgressBar(context,null,0,android.R.attr.progressBarStyleHorizontal)

    private lateinit var gestureDetector:GestureDetector

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initView(context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initView(context,attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        initView(context,attrs)
    }

    private fun initView(context: Context,attrs: AttributeSet?){
        gestureDetector = object :GestureDetector(context, object : OnGestureListener {
            override fun onDown(p0: MotionEvent?): Boolean {
                return true
            }

            override fun onShowPress(p0: MotionEvent?) {
            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }

            override fun onScroll(
                p0: MotionEvent?,
                p1: MotionEvent?,
                offsetX: Float,
                offsetY: Float
            ): Boolean {
                calcAngle(p0, p1)
                return true
            }

            override fun onLongPress(p0: MotionEvent?) {
            }

            override fun onFling(
                p0: MotionEvent?,
                p1: MotionEvent?,
                offsetX: Float,
                offsetY: Float
            ): Boolean {
//                if (abs(offsetX) > abs(offsetY)) {
//                    if (offsetX < -SLIDE_DISTANCE) {
//                        println("向右")
//                    } else {
//                        println("向左")
//                    }
//                } else {
//                    if (offsetY < -SLIDE_DISTANCE) {
//                        println("向下")
//                    } else {
//                        println("向上")
//                    }
//                }
                return true
            }
        }){}

        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(iconDusk)
        addView(iconNoon)
        addView(iconNight)
        addView(iconDawn)
        addView(pointerLong)
        addView(pointerShort)
        addView(clockBody)
        addView(processOuter)
        addView(processInner)

        //添加内部背景与齿轮
        val clockBackgroundSpan = CardView(context)
        addView(clockBackgroundSpan)
        clockBackgroundSpan.apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            val cardSpan = FrameLayout(context).apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                addView(clockBackground)
                val gearSpan = FrameLayout(context)
                addView(gearSpan)
                gearSpan.apply {
                    layoutParams = LayoutParams(getDp(180f).toInt(),getDp(180f).toInt())
                    alpha = 0.6f
                    addView(gearCenter)
                    addView(gearS)
                    addView(gearL)
                    addView(gearM)
                    addView(gearXL)
                }
            }
            addView(cardSpan)
            radius = getDp(90f)
            cardElevation = 0f
            setCardBackgroundColor(Color.TRANSPARENT)
        }

        //主体
        setImageViewAttrs(iconDusk,49.8f,R.drawable.img_clock_dusk,6f,141f,-1f,Gravity.CENTER)
        setImageViewAttrs(iconNoon,49f,R.drawable.img_clock_noon,6f,0f,-140.5f,Gravity.CENTER)
        setImageViewAttrs(iconNight,52.5f,R.drawable.img_clock_night,6f,0f,141.5f,Gravity.CENTER)
        setImageViewAttrs(iconDawn,49.8f,R.drawable.img_clock_dawn,6f,-140f,-2f,Gravity.CENTER)
        setImageViewAttrs(pointerLong,100f,R.drawable.img_clock_pointer_long,6f,0f,-30f,Gravity.CENTER)
        setImageViewAttrs(pointerShort,70f,R.drawable.img_clock_pointer_short,4f,0f,35f,Gravity.CENTER)
        setImageViewAttrs(clockBody,450f,R.drawable.img_clock_body,5f,0f,0f,Gravity.CENTER)
        setImageViewAttrs(clockBackground,180f,R.drawable.img_clock_background,0f,0f,0f)

        //齿轮
        setImageViewAttrs(gearCenter,50f,R.drawable.img_clock_gear_s,3f,0f,0f,Gravity.CENTER)
        setImageViewAttrs(gearS,50f,R.drawable.img_clock_gear_s,3f,44f,-88f,Gravity.CENTER)
        setImageViewAttrs(gearM,103f,R.drawable.img_clock_gear_m,3f,95f,54f,Gravity.CENTER)
        setImageViewAttrs(gearL,156f,R.drawable.img_clock_gear_l,2f,-28f,18f,Gravity.CENTER)
        setImageViewAttrs(gearXL,180f,R.drawable.img_clock_gear_xl,1f,15f,-15f,Gravity.CENTER)


        //设置指针锚点
        setImagePivot(pointerLong,50.0f,80.29f)
        setImagePivot(pointerShort,35f,0f)

        processInner.apply {
            val lp = layoutParams as LayoutParams
            lp.apply {
                gravity = Gravity.CENTER
                width = getDp(199f).toInt()
                height = getDp(199f).toInt()
            }
            elevation = 7f
            max = 360
            layoutParams = lp
//            progressDrawable = gradient
            progressDrawable = ContextCompat.getDrawable(context,R.drawable.round_clock_progress_bar)
        }

        processOuter.apply {
            val lp = layoutParams as LayoutParams
            lp.apply {
                gravity = Gravity.CENTER
                width = getDp(210f).toInt()
                height = getDp(210f).toInt()
            }
            elevation = 7f
            max = 360
            layoutParams = lp
//            progressDrawable = gradient
            progressDrawable = ContextCompat.getDrawable(context,R.drawable.round_clock_progress_bar)
        }
        setAnimation()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val b = sqrt(a*a+c*c)
//        println("atan = ${atan(a/c)} deg = ${Math.toDegrees(atan(a/c).toDouble())}")
        event?.apply {
            when(action){
                MotionEvent.ACTION_DOWN->{
                    calcAngle(null,event)
                }
                MotionEvent.ACTION_MOVE->{
                    gestureDetector.onTouchEvent(event)
                    if(gearSpeed==1L){
                        gearSpeed = 4L
                        clearAnimation()
                        setAnimation()
                    }
                }
                MotionEvent.ACTION_UP->{
                    gearSpeed = 1L
                    clearAnimation()
                    setAnimation()
                }
            }
        }
        performClick()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun calcAngle( p0: MotionEvent?, p1: MotionEvent?){
        val oX = left + measuredWidth / 2
        val oY = top + measuredHeight / 2

        val oldX = (p0?.x ?: oX).toFloat()
        val oldY = (p0?.y ?: oY).toFloat()

        val currentX = (p1?.x ?: oX).toFloat()
        val currentY = (p1?.y ?: oY).toFloat()

        val oldA = oldX - oX
        val oldC = oldY - oY

        val a = currentX - oX
        val c = (currentY - oY) * -1

        val oldDeg = Math.toDegrees(atan(oldA / oldC).toDouble()).toFloat()
        val currentDeg = Math.toDegrees(atan(a / c).toDouble()).toFloat()

        val oldQuadrant = getQuadrant(oldX, oldC)
        val currentQuadrant = getQuadrant(a, c)

//        println("currentX = $currentX currentY = $currentY")
//        println("a = $a c = $c")
//        println("currentDeg = ${currentDeg} currentQuadrant = $currentQuadrant")
//        val b = sqrt(a*a+c*c)
//        println("atan = ${atan(a/c)} deg = ${Math.toDegrees(atan(a/c).toDouble())} acos=${Math.toDegrees(acos((a*a-c*c-b*b)/(-2*b*c).toDouble()))}")
        setPointerLongRotate(currentQuadrant,currentDeg)
//                if(currentQuadrant>=oldQuadrant){
//                    setPointerLongRotate(currentDeg)
//                }else{
//
//                }

//                if (abs(offsetX) > abs(offsetY)) {
//                    setPointerLongRotate(offsetX)
//                    if (offsetX>0) {
//                        println("S向左")
//                    } else {
//                        println("S向右")
//                    }
//                } else {
//                    if (offsetY>0) {
//                        println("S向上")
//                    } else {
//                        println("S向下")
//                    }
//                }
    }

    private fun setPointerLongRotate(currentQuadrant:Int,currentDeg:Float){
        val degOffset = when (currentQuadrant) {
            2 -> 180
            3 -> 180
            4 -> 360
            else -> 0
        }

        val value = degOffset + currentDeg
//        println("offset = ${degOffset} deg = ${currentDeg} value = ${value} rotation=${pointerLong.rotation} currentQuadrant = ${currentQuadrant}")
//        println("currentQuadrant = ${currentQuadrant} preQuadrant = ${preQuadrant} gearSpeed = ${gearSpeed}")

        if((currentQuadrant==4&&preQuadrant==1||currentQuadrant==3&&preQuadrant==1)&&pointerLong.rotation<=360&&!back){//从0往回转时
            pointerLong.rotation = 0f
            processOuter.progress = 0
        }else if(currentQuadrant==1&&preQuadrant==4&&pointerLong.rotation<360){//进入第二圈
            pointerLong.rotation = 360+value
            processOuter.progress = 360
            processInner.progress = pointerLong.rotation.toInt()%360
            preQuadrant = currentQuadrant
        }else if(currentQuadrant==4&&preQuadrant==1&&pointerLong.rotation>=360){ //从第二圈回第一圈
            pointerLong.rotation = value
            processInner.progress = 0
            processOuter.progress = pointerLong.rotation.toInt()%360
            back = true
        }else if(processOuter.progress==360&&preQuadrant==4&&(currentQuadrant==1||currentQuadrant==2)){//两圈转满
            processOuter.progress = 360
            processInner.progress = 360
            pointerLong.rotation = 720f
        }else if(pointerLong.rotation>=360){//转第二圈
            pointerLong.rotation = 360+value
            processInner.progress = pointerLong.rotation.toInt()%360
            preQuadrant = currentQuadrant
        }else{//转第一圈
            pointerLong.rotation = value
            pointerShort.rotation = pointerLong.rotation/360
            processOuter.progress = pointerLong.rotation.toInt()%360
            preQuadrant = currentQuadrant
            back = false
        }
    }

    private fun setAnimation(){
        arrayOf(gearS,gearM,gearL,gearXL,gearCenter).forEach {imageView ->
            val (start,end,time) = if(imageView==gearM){
                Triple(imageView.rotation,imageView.rotation-360f,13460L/gearSpeed)
            }else{
                Triple(imageView.rotation,imageView.rotation+360f,20000L/gearSpeed)
            }
            val anim = ObjectAnimator.ofFloat(imageView,"rotation",start,end).apply {
                duration = time
                interpolator = object :LinearInterpolator(){
                    override fun getInterpolation(input: Float): Float {
                        return input
                    }
                }
            }
            anim.addUpdateListener {
                imageView.rotation = it.animatedValue as Float * gearSpeed
            }
            anim.repeatCount = ObjectAnimator.INFINITE
            AnimatorSet().apply {
                play(anim)
                start()
            }
            gearAnimator += anim
        }
        pointerShort.setOnClickListener {
            removeAllAnimation()
        }
    }

    private fun removeAllAnimation(){
        gearAnimator.forEach {
            it.cancel()
        }
        gearAnimator.clear()
    }

    private fun setImageViewAttrs(img:ImageView,sideLength:Float,resource:Int,zIndex:Float,transformX:Float = 0f,transformY:Float = 0f,targetGravity:Int = Gravity.NO_GRAVITY){
        img.apply {
            setImageResource(resource)
            elevation = zIndex
            val lp = layoutParams as LayoutParams
            lp.apply {
                gravity = targetGravity
                width = getDp(sideLength).toInt()
                height = getDp(sideLength).toInt()
                translationX = getDp(transformX)
                translationY = getDp(transformY)
            }
            layoutParams = lp
        }
    }

    private fun setImagePivot(img: ImageView,x:Float,y:Float){
        img.apply {
            pivotX = getDp(x)
            pivotY = getDp(y)
        }
    }
    //判断象限 分别为1,2,3,4 (顺时针)
    private fun getQuadrant(x:Float,y:Float)=
        if(x>=0&&y>=0){
            1
        }else if(x>=0&&y<=0){
            2
        }else if(x<=0&&y<=0){
            3
        }else if(x<=0&&y>=0){
            4
        }else{//原点
            0
        }

    private fun getDp(value:Float) = context.resources.displayMetrics.density * value +0.5f
}