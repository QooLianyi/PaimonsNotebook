package com.lianyi.paimonsnotebook.lib.html

import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import org.xml.sax.XMLReader

class TagHandler:Html.TagHandler {

    private var startIndex = 0
    private var endIndex = 0

    override fun handleTag(p0: Boolean, tag: String?, output: Editable?, p3: XMLReader?) {
        if(output==null) return
        if(tag?.toLowerCase()=="t"){
            if(p0){
                startT(tag,output,p3!!)
            }else{
                endT(tag,output,p3!!)
            }
        }
    }

    private fun startT(tag:String, output: Editable, xmlReader: XMLReader){
        startIndex = output.length
    }

    private fun endT(tag:String, output: Editable, xmlReader: XMLReader){
        endIndex = output.length
        output.setSpan(TSpan(),startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    inner class TSpan():CharacterStyle(){
        override fun updateDrawState(p0: TextPaint?) {

        }
    }

}