package com.lianyi.paimonsnotebook.common.util.compose.provider

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.lianyi.paimonsnotebook.common.util.compose.theme.NoRippleTheme

@Composable
fun NoRippleThemeProvides(
    content: @Composable () -> Unit,
) = CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme, content = content)