package com.lianyi.paimonsnotebook.common.extension.modifier.padding

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.paddingBottom(value: Dp) = this.padding(0.dp,0.dp,0.dp,value)

fun Modifier.paddingTop(value: Dp) = this.padding(0.dp,value,0.dp,0.dp)

fun Modifier.paddingStart(value: Dp) = this.padding(value,0.dp,0.dp,0.dp)

fun Modifier.paddingEnd(value: Dp) = this.padding(0.dp,0.dp,value,0.dp)

