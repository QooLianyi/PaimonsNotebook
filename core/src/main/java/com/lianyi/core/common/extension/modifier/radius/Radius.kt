package com.lianyi.core.common.extension.modifier.radius

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

fun Modifier.radius(size: Dp) = clip(RoundedCornerShape(size))

