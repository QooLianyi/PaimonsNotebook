package com.lianyi.paimonsnotebook.common.util.compose.provider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoOverscrollEffectThemeProvides(content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalOverscrollConfiguration provides null, content = content)