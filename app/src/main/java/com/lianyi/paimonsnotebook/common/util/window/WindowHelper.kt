package com.lianyi.paimonsnotebook.common.util.window

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.lianyi.paimonsnotebook.common.extension.value.toPx

object WindowHelper {

    val statusBarHeightDp: Dp
        @Composable
        get() {
            return WindowInsets.statusBars.asPaddingValues(LocalDensity.current)
                .calculateTopPadding()
        }

    val statusBarHeightPx: Float
        @Composable
        get() {
            return statusBarHeightDp.toPx()
        }

    val navigationBarHeightDp: Dp
        @Composable
        get() {
            return WindowInsets.navigationBars.asPaddingValues(LocalDensity.current)
                .calculateTopPadding()
        }

    val navigationBarHeightPx: Float
        @Composable
        get() {
            return navigationBarHeightDp.toPx()
        }


}