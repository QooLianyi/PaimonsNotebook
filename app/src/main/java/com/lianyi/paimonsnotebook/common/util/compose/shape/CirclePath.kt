package com.lianyi.paimonsnotebook.common.util.compose.shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.sqrt

class CirclePath(private val progress: Float, private val start: CirclePathStartPoint,private val offset: Offset = Offset.Zero) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        val origin = when (start) {
            CirclePathStartPoint.TopStart -> Offset(0f, 0f)
            CirclePathStartPoint.TopCenter -> Offset(size.center.x, 0f)
            CirclePathStartPoint.TopEnd -> Offset(size.width, 0f)
            CirclePathStartPoint.CenterStart -> Offset(0f, size.center.y)
            CirclePathStartPoint.Center -> Offset(size.center.x, size.center.y)
            CirclePathStartPoint.CenterEnd -> Offset(size.width, size.center.y)
            CirclePathStartPoint.BottomStart -> Offset(0f, size.height)
            CirclePathStartPoint.BottomCenter -> Offset(size.center.x, size.height)
            CirclePathStartPoint.BottomEnd -> Offset(size.width, size.height)
        } + offset


        val radius = (sqrt(
            size.height * size.height + size.width * size.width
        ) * 1f) * progress

        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        center = origin,
                        radius = radius,
                    )
                )
            }
        )
    }
}

enum class CirclePathStartPoint {
    TopStart,
    TopCenter,
    TopEnd,
    CenterStart,
    Center,
    CenterEnd,
    BottomStart,
    BottomCenter,
    BottomEnd,
}
