package com.lianyi.paimonsnotebook.ui.screen.items.data

data class CardPropertyData(
    val icon:Int,
    val propertyName:String,
    val propertyValue:Float,
    val compareValue:Float = -1f,
    val suffix:String = ""
)
