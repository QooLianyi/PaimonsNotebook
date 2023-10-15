package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/*
* compose sp转dp
* */
@Composable
fun TextUnit.toDp(): Dp = with(LocalDensity.current) {
    this@toDp.toDp()
}