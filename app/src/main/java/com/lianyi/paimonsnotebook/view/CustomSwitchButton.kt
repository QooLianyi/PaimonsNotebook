package com.lianyi.paimonsnotebook.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.lianyi.paimonsnotebook.R

class CustomSwitchButton: FrameLayout {
    private var textON = "开启"
    private var textOFF = "关闭"

    private var density = 0f

    private val showTextColor = Color.parseColor("#C5C9CC")
    private val showTextSize = 14f

    private val buttonShape = GradientDrawable().apply {
        setColor(Color.argb(255,211,189,142))
    }

    private val switchButton = SwitchCompat(this.context)
    private val onTextView = TextView(this.context)
    private val offTextView = TextView(this.context)
    var callback:((Boolean)->Unit)?=null

    init {
        density = context.resources.displayMetrics.density
    }

    constructor(context: Context) : super(context){
    }
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
        val a = context.obtainStyledAttributes(attrs,R.styleable.CustomSwitchButton)

        val mTextOn = a.getString(R.styleable.CustomSwitchButton_textOn)
        val mTextOff = a.getString(R.styleable.CustomSwitchButton_textOff)
        val mChecked = a.getBoolean(R.styleable.CustomSwitchButton_checked,false)
        val mPaddingHor = a.getInteger(R.styleable.CustomSwitchButton_padding_hor,getDp(5).toInt())
        val mPaddingVer = a.getInteger(R.styleable.CustomSwitchButton_padding_ver,getDp(3).toInt())
        val mButtonHeight = a.getInteger(R.styleable.CustomSwitchButton_button_height,35)
        val mButtonWidth = a.getInteger(R.styleable.CustomSwitchButton_button_width,50)
        val mButtonRadius = a.getInteger(R.styleable.CustomSwitchButton_button_radius,6)

        if(!mTextOn.isNullOrEmpty()){
            textON = mTextOn
        }

        if(!mTextOff.isNullOrEmpty()){
            textOFF = mTextOff
        }

        val switchLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,50)
        val onTextViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        val offTextViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)

        background = GradientDrawable().apply {
            setColor(Color.argb(255,240,241,245))
            cornerRadius = getDp(8)
        }
        setPadding(mPaddingHor,mPaddingVer,mPaddingHor,mPaddingVer)

        onTextViewLayoutParams.apply {
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
            marginEnd = getDp(10).toInt()
        }
        offTextViewLayoutParams.apply {
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            marginStart = getDp(10).toInt()
        }

        onTextView.apply {
            layoutParams = onTextViewLayoutParams
            setTextColor(showTextColor)
            textSize = showTextSize
            text = textON
        }
        offTextView.apply {
            layoutParams = offTextViewLayoutParams
            setTextColor(showTextColor)
            textSize = showTextSize
            text = textOFF
        }

        switchButton.apply {
            layoutParams = switchLayoutParams
            splitTrack = false
            showText = true
            isChecked = mChecked
            textOn = textON
            textOff = textOFF
            thumbDrawable = buttonShape.apply {
                setSize(getDp(mButtonWidth).toInt(),getDp(mButtonHeight).toInt())
                cornerRadius = getDp(mButtonRadius)
            }
            setSwitchTextAppearance(context,R.style.CustomBottomStyle)
            setTrackResource(R.color.transparent)
            highlightColor = ContextCompat.getColor(context,R.color.transparent)
            setOnCheckedChangeListener { compoundButton, b ->
                changeTextViewVisibility(b)
                callback?.invoke(b)
            }
        }

        addView(onTextView)
        addView(offTextView)
        addView(switchButton)
        changeTextViewVisibility(switchButton.isChecked)
        a.recycle()
    }

    fun setOffText(string: String){
        switchButton.textOff = string
        offTextView.text = string
    }
    fun setOnText(string: String){
        switchButton.textOn = string
        onTextView.text = string
    }

    private fun getDp(px:Int):Float = px * density +0.5f

    private fun changeTextViewVisibility(b:Boolean){
//        if(b){
//            offTextView.visibility = View.VISIBLE
//            onTextView.visibility = View.GONE
//        }else{
//            offTextView.visibility = View.GONE
//            onTextView.visibility = View.VISIBLE
//        }
    }
}