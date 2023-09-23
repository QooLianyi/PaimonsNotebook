package com.lianyi.paimonsnotebook.common.components.helper_text.data

import androidx.compose.runtime.MutableState
import com.lianyi.paimonsnotebook.common.util.enums.HelperTextStatus

/*
* 用于HelperText组件提示
* validate返回为true时,说明该值可用
*
* */
data class HelperTextData(
    val text:String,
    val state: MutableState<HelperTextStatus>,
    val validate:()->Boolean
)
