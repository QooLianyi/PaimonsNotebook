package com.lianyi.paimonsnotebook.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.view.MarkDownTextBean
import com.lianyi.paimonsnotebook.bean.view.MarkDownTextType

//此组件用于显示读取从github获取的日志(md格式)

class MarkDownView:LinearLayout {

    companion object{
        const val H1_TEXT_SIZE = 28f
        const val H2_TEXT_SIZE = 24f
        const val H3_TEXT_SIZE = 20f
        const val H4_TEXT_SIZE = 18f
        const val H5_TEXT_SIZE = 16f
        const val TEXT_SIZE = 16f
        const val BR_HEIGHT = 2f
        const val TEXT_PADDING_START = 10f
        private val regex = Regex("[0-9]+.")

        var isEnableLog = false
    }
    private var density = 0f
    private var orderIndex = 1
    private lateinit var mContainer:LinearLayout
    private val mChilds = mutableListOf<LinearLayout>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initView(context!!,attrs!!)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initView(context!!,attrs!!)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        initView(context!!,attrs!!)
    }

    private fun initView(context: Context,attrs: AttributeSet){
        density = context.resources.displayMetrics.density
        val a = context.obtainStyledAttributes(attrs, R.styleable.MarkDownView)
        val text = a.getString(R.styleable.MarkDownView_text)
        createNewContainer()
        setText(text)

        this.orientation = VERTICAL
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)

        a.recycle()
    }

    fun setText(text:String?){
        removeAllContainer()

        val textLines = text?.split("\r\n")
        val markTextList = mutableListOf<MarkDownTextBean>()
        textLines?.forEach {
            markTextList += getMarkDownBean(it)
        }

        markTextList.forEach {
            val textView = TextView(context)
            if(it.level==0){
                this.addView(mContainer)
                createNewContainer()
            }
            mContainer.addView(textView)

            val (textSizeValue,textStyleValue) = when(it.textType){
                MarkDownTextType.H1->{
                    H1_TEXT_SIZE to Typeface.BOLD
                }
                MarkDownTextType.H2->{
                    H2_TEXT_SIZE to Typeface.BOLD
                }
                MarkDownTextType.H3->{
                    H3_TEXT_SIZE to Typeface.BOLD
                }
                MarkDownTextType.H4->{
                    H4_TEXT_SIZE to Typeface.BOLD
                }
                MarkDownTextType.H5->{
                    H5_TEXT_SIZE to Typeface.BOLD
                }
                MarkDownTextType.DISORDER_LIST->{
                    TEXT_SIZE to Typeface.NORMAL
                }
                MarkDownTextType.ORDER_LIST->{
                    TEXT_SIZE to Typeface.NORMAL
                }
                MarkDownTextType.BR->{
                    BR_HEIGHT to Typeface.NORMAL
                }
                MarkDownTextType.TEXT->{
                    TEXT_SIZE to Typeface.NORMAL
                }
                else ->{
                    TEXT_SIZE to Typeface.NORMAL
                }
            }
            when(it.textType){
                MarkDownTextType.BR->{
                    textView.apply {
                        val lp = layoutParams as MarginLayoutParams
                        lp.apply {
                            height = getDp(BR_HEIGHT).toInt()
                            width = context.resources.displayMetrics.widthPixels
                        }
                        layoutParams = lp
                        setBackgroundColor(Color.argb(20,0,0,0))
                    }
                }
                else->{
                    val paddingStart = getDp(it.level* TEXT_PADDING_START)

                    textView.apply {
                        setText(it.text)
                        setTextColor(context.getColor(R.color.black))
                        setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeValue)
                        setTypeface(null,textStyleValue)
                        setPadding(paddingStart.toInt(),5,0,5)
                    }
                }
            }
        }
        this.addView(mContainer)
    }

    private fun getMarkDownBean(text: String):MarkDownTextBean{
        val line = text.split(" ")
        var level = 0
        val textBuffer = StringBuffer()
        if(isEnableLog)
            println(line.toString())

        line.forEach {
            when(it){
                "" -> {
                    level++
                }
                else->{
                    textBuffer.append("${it} ")
                }
            }
        }

        val linePart = textBuffer.toString().trim().split(" ")

        if(isEnableLog)
            println("linePart = ${linePart}")

        val listStyle = when(linePart[0]){
            //无序列表
            "-"->{
                MarkDownTextType.DISORDER_LIST
            }
            //有序列表
            if(regex.matches(line.first())) line.first() else "-990009" ->{
                level++
                MarkDownTextType.ORDER_LIST
            }
            else ->{
                MarkDownTextType.NORMAL
            }
        }

        val flag = if(linePart.size>2&&linePart[1].contains("#")){
            if(linePart[0]=="-"||regex.matches(linePart[0])){
                linePart[1]
            }else{
                linePart[0]
            }
        }else{
            linePart[0]
        }

        if(isEnableLog)
            println("flag change after = ${flag}")

        val type = when(flag){
            "#"->{
                MarkDownTextType.H1
            }
            "##"->{
                MarkDownTextType.H2
            }
            "###"->{
                MarkDownTextType.H3
            }
            "####"->{
                MarkDownTextType.H4
            }
            "#####"->{
                MarkDownTextType.H5
            }
            "***"->{
                MarkDownTextType.BR
            }
            else ->{
                MarkDownTextType.TEXT
            }
        }

        val showTextBuffer = StringBuffer()
        (line.indexOf(flag)+1 until line.size).forEach {
            showTextBuffer.append(line[it])
        }

        var showText = showTextBuffer.toString()

        if(listStyle!=MarkDownTextType.NORMAL){
            val symbol = when(level){
                0,1->{
                    "•"
                }
                2->{
                    "⚬"
                }
                else->{
                    "▪"
                }
            }
            showText = when(listStyle){
                MarkDownTextType.DISORDER_LIST->{
                    "$symbol $showTextBuffer"
                }
                MarkDownTextType.ORDER_LIST->{
                    "${orderIndex++}. $showTextBuffer"
                }
                else->{
                    showTextBuffer.toString()
                }
            }
        }

        val bean = MarkDownTextBean(level,showText,type,listStyle)
        if(isEnableLog)
            println(bean.toString())

        return bean
    }

    private fun createNewContainer(){
        orderIndex = 1
        mContainer = LinearLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            orientation = VERTICAL
        }
        mChilds += mContainer
    }

    private fun removeAllContainer(){
        mChilds.forEach {
            it.removeAllViews()
            this.removeView(it)
        }
    }

    private fun getDp(px:Float):Float = px * density +0.5f
}