package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.ElementElectricColor
import com.lianyi.paimonsnotebook.ui.theme.ElementFireColor
import com.lianyi.paimonsnotebook.ui.theme.ElementGrassColor
import com.lianyi.paimonsnotebook.ui.theme.ElementIceColor
import com.lianyi.paimonsnotebook.ui.theme.ElementRockColor
import com.lianyi.paimonsnotebook.ui.theme.ElementWaterColor
import com.lianyi.paimonsnotebook.ui.theme.ElementWindColor
import com.lianyi.paimonsnotebook.ui.theme.Error
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import com.lianyi.paimonsnotebook.ui.theme.Primary_4
import com.lianyi.paimonsnotebook.ui.theme.Primary_5
import com.lianyi.paimonsnotebook.ui.theme.Success
import com.lianyi.paimonsnotebook.ui.theme.Warning


class TypographyScreen : BaseActivity() {

    class InformationPopupWindowOffset {
        var overTop = false
        var overSide = false
        var windowOffset = IntOffset.Zero

        var indicatorOffsetPx = 0.dp

        //代表哪个方向超出边界
        var end = false
        var start = false


        fun getIndicatorCurrentOffset() = if (end) {
            Alignment.TopEnd
        } else if (start) {
            Alignment.TopStart
        } else {
            Alignment.TopCenter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var showPopup by mutableStateOf(false)

        val colors = listOf(
            Primary_4,
            Warning,
            Error,
            Success,
            Primary_2,
            Info,
            Primary_5,
            ElementGrassColor,
            ElementElectricColor,
            ElementIceColor,
            ElementRockColor,
            ElementFireColor,
            ElementWaterColor,
            ElementWindColor
        )

        var offset by mutableStateOf(InformationPopupWindowOffset())

        var popupProvider = InformationPopupPositionProvider()

        setContent {

            PaimonsNotebookTheme {
//                Content()

                val state = rememberLazyGridState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ) {


//                    Box {
//                        TextButton(text = "popup window测试") {
//                            showPopup = !showPopup
//                        }
//
//                        ContentSpacerLazyVerticalGrid(
//                            columns = GridCells.Adaptive(80.dp),
//                            state = state,
//                            verticalArrangement = Arrangement.spacedBy(10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            contentPadding = PaddingValues(horizontal = 16.dp)
//                        ) {
//
//                            repeat(100) { count ->
//                                colors.forEach {
//                                    item(key = "${count}_${it.toArgb()}") {
//                                        var offset = Offset.Zero
//                                        var size = IntSize.Zero
//                                        Box(
//                                            modifier = Modifier
//                                                .onGloballyPositioned {
//                                                    offset = it.positionInRoot()
//                                                    size = it.size
//                                                }
//                                                .fillMaxSize(),
//                                            contentAlignment = Alignment.Center
//                                        ) {
//                                            Box(
//                                                modifier = Modifier
//                                                    .size(70.dp)
//                                                    .background(it)
//                                                    .clickable {
//                                                        val currentKey = "${count}_${it.toArgb()}"
//                                                        val itemInfo =
//                                                            state.layoutInfo.visibleItemsInfo.takeFirstIf { it.key == currentKey }
//                                                                ?: return@clickable
//
//                                                        println("itemInfo.size = ${itemInfo.size}")
//                                                        println("itemInfo.offset = ${itemInfo.offset}")
//
//                                                        popupProvider =
//                                                            InformationPopupPositionProvider(
//                                                                contentOffset = offset,
//                                                                itemSize = size,
//                                                                itemSpace = 10.dp,
//                                                            )
//
//                                                        showPopup = true
//                                                    },
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Spacer(
//                                                    modifier = Modifier
//                                                        .width(.5.dp)
//                                                        .background(White)
//                                                        .height(70.dp)
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (showPopup) {
//                            Popup(
//                                properties = PopupProperties(),
//                                onDismissRequest = {
//                                    showPopup = false
//                                },
//                                popupPositionProvider = popupProvider
//                            ) {
//
//                                Box {
//
//                                    Column(modifier = Modifier
//                                        .width(180.dp)
//                                        .border(1.dp, Error)
//                                        .drawWithCache {
//                                            val indicatorWidth = popupProvider.indicatorWidthPx
//                                            val cornerRadius = 20.dp.toPx()
//
//                                            val path = Path().apply {
//                                                reset()
//
//                                                arcTo(
//                                                    rect = Rect(
//                                                        offset = Offset(0f, 0f),
//                                                        size = Size(cornerRadius, cornerRadius),
//                                                    ),
//                                                    startAngleDegrees = -90f,
//                                                    sweepAngleDegrees = -90f,
//                                                    forceMoveTo = false
//                                                )
//
//                                                arcTo(
//                                                    rect = Rect(
//                                                        offset = Offset(
//                                                            0f,
//                                                            size.height - cornerRadius
//                                                        ),
//                                                        size = Size(cornerRadius, cornerRadius),
//                                                    ),
//                                                    startAngleDegrees = -180f,
//                                                    sweepAngleDegrees = -90f,
//                                                    forceMoveTo = false
//                                                )
//
//                                                arcTo(
//                                                    rect = Rect(
//                                                        offset = Offset(
//                                                            size.width - cornerRadius,
//                                                            size.height - cornerRadius
//                                                        ),
//                                                        size = Size(cornerRadius, cornerRadius),
//                                                    ),
//                                                    startAngleDegrees = 90f,
//                                                    sweepAngleDegrees = -90f,
//                                                    forceMoveTo = false
//                                                )
//
//                                                arcTo(
//                                                    rect = Rect(
//                                                        offset = Offset(
//                                                            size.width - cornerRadius,
//                                                            0f
//                                                        ),
//                                                        size = Size(cornerRadius, cornerRadius),
//                                                    ),
//                                                    startAngleDegrees = 0f,
//                                                    sweepAngleDegrees = -90f,
//                                                    forceMoveTo = false
//                                                )
//
//                                                close()
//                                            }
//
//                                            val indicatorPath = Path().apply {
//                                                reset()
//
//                                                moveTo(
//                                                    popupProvider.indicatorHorizontalDrawStartPosition,
//                                                    size.height
//                                                )
//                                                lineTo(
//                                                    popupProvider.indicatorHorizontalDrawStartPosition - indicatorWidth / 2,
//                                                    size.height
//                                                )
//                                                lineTo(
//                                                    popupProvider.indicatorHorizontalDrawStartPosition,
//                                                    size.height + indicatorWidth / 2
//                                                )
//                                                lineTo(
//                                                    popupProvider.indicatorHorizontalDrawStartPosition + indicatorWidth / 2,
//                                                    size.height
//                                                )
//
//                                                close()
//                                            }
//
//                                            onDrawBehind {
//                                                drawPath(
//                                                    path = path,
//                                                    color = Black
//                                                )
//
//                                                //如果弹窗超出顶部,并且有一边超出屏幕就将绘制的指示器路径旋转
//                                                rotate(if (popupProvider.overWindowTop) 180f else 0f) {
//                                                    println("rotate draw")
//                                                    drawPath(
//                                                        path = indicatorPath,
//                                                        color = Black
//                                                    )
//                                                }
//                                            }
//                                        }
//                                        .padding(8.dp)
//                                    ) {
//                                        Row(modifier = Modifier.fillMaxWidth()) {
//                                            Image(
//                                                painter = painterResource(id = R.drawable.icon_area_summer_land),
//                                                contentDescription = null,
//                                                modifier = Modifier.size(36.dp),
//                                            )
//
//                                            Spacer(modifier = Modifier.width(6.dp))
//
//                                            Column {
//                                                PrimaryText(
//                                                    text = "测试物品名称",
//                                                    fontSize = 14.sp,
//                                                    color = White
//                                                )
//                                                Spacer(modifier = Modifier.height(2.dp))
//                                                InfoText(text = "测试物品类型", fontSize = 10.sp)
//                                            }
//                                        }
//
//                                        Spacer(modifier = Modifier.height(6.dp))
//
//                                        InfoText(
//                                            text = "测试物品描述测试物品描述测试物品描述测试物品描述测试物品描述测试物品描述测试物品描述测试物品描述测试物品描述",
//                                            maxLines = 4
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun Content() {
        var pointerOffset: Offset by remember {
            mutableStateOf(Offset(0f, 0f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor)
//                .pointerInput("dragging") {
//                    detectDragGestures { change, dragAmount ->
//                        pointerOffset += dragAmount
//                    }
//                }
//                .onSizeChanged {
//                    pointerOffset = Offset(it.width / 2f, it.height / 2f)
//                }
//                .drawWithContent {
//                    drawContent()
//                    // draws a fully black area with a small keyhole at pointerOffset that’ll show part of the UI.
//                    drawRect(
//                        Brush.radialGradient(
//                            listOf(Transparent, Black),
//                            center = pointerOffset,
//                            radius = 100.dp.toPx(),
//                        )
//                    )
//                }
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Column(modifier = Modifier
                .width(180.dp)
                .height(120.dp)
                .drawWithCache {
                    val indicatorWidth = 10.dp.toPx()
                    val cornerRadius = 20.dp.toPx()

                    val path = Path().apply {
                        reset()

                        arcTo(
                            rect = Rect(
                                offset = Offset(0f, 0f),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = -90f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(0f, size.height - cornerRadius),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = -180f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(
                                    size.width - cornerRadius,
                                    size.height - cornerRadius
                                ),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(size.width - cornerRadius, 0f),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = 0f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        close()
                    }

                    val indicatorPath = Path().apply {
                        reset()

                        moveTo(size.width / 2 - indicatorWidth / 2, size.height)
                        lineTo(size.width / 2 - indicatorWidth / 2, size.height)
                        lineTo(size.width / 2, size.height + indicatorWidth / 2)
                        lineTo(size.width / 2 + indicatorWidth / 2, size.height)

                        close()
                    }

                    onDrawBehind {
                        drawPath(
                            path = path,
                            color = Black
                        )

                        drawPath(
                            path = indicatorPath,
                            color = Black
                        )
                    }
                }
            ) {
            }

            Canvas(
                modifier = Modifier
                    .width(180.dp)
                    .height(120.dp)
                    .zIndex(0f)
            ) {
                val indicatorWidth = 10.dp.toPx()
                val cornerRadius = 6.dp

                val path = Path().apply {
                    reset()

                    lineTo(0f, size.height)

                    lineTo(size.width, size.height)

                    lineTo(size.width, 0f)

                    close()
                }

                val indicatorPath = Path().apply {
                    reset()

                    moveTo(size.width / 2 - indicatorWidth / 2, size.height)
                    lineTo(size.width / 2 - indicatorWidth / 2, size.height)
                    lineTo(size.width / 2, size.height + indicatorWidth / 2)
                    lineTo(size.width / 2 + indicatorWidth / 2, size.height)

                    close()
                }

                drawIntoCanvas { canvas ->
                    canvas.drawPath(
                        path = path,
                        paint = Paint().apply {
                            color = Black
                            pathEffect = PathEffect.cornerPathEffect(cornerRadius.toPx())
                            strokeWidth = 5.dp.toPx()
                        }
                    )

                    canvas.drawPath(indicatorPath, Paint().apply {
                        color = Black
                        strokeWidth = 5.dp.toPx()
                    })
                }
            }

//            Column(modifier = Modifier
//                .size(180.dp, 100.dp)
//                .graphicsLayer {
//                    shadowElevation = 8.dp.toPx()
//                    shape = CutCornerShape(32.dp)
//                    clip = true
//                }
//                .background(Black)
//            ) {
//            }
//
//            Column(
//                modifier = Modifier
//                    .size(180.dp, 100.dp)
//                    .clip(TicketShape(50f))
//                    .background(Black)
//            ) {
//            }
//
//            Box(modifier = Modifier
//                .size(180.dp)
//                .border(1.dp, Error)
//                .drawBehind {
//
//                    drawRect(Black_10)
//
//                    inset(20f, 20f, 0f, 0f) {
//                        rotate(-45f) {
//                            scale(.5f) {
//                                drawLine(
//                                    color = Success,
//                                    start = Offset(size.width, size.height),
//                                    end = Offset.Zero,
//                                    strokeWidth = 32.dp.toPx(),
//                                    cap = StrokeCap.Round
//                                )
//                            }
//                        }
//                    }
//
////                    val points = mutableListOf<Offset>()
////
////                    val range = (0..1000)
////                    repeat(10) {
////                        val offset = Offset(
////                            range
////                                .random()
////                                .toFloat(),
////                            range
////                                .random()
////                                .toFloat()
////                        )
////                        points += offset
////                    }
////
////                    drawPoints(
////                        points,
////                        pointMode = PointMode.Points,
////                        Primary_2,
////                        strokeWidth = 30.dp.toPx(),
////                        cap = StrokeCap.Round
////                    )
//                }
//            )
//
//            Box(
//                modifier = Modifier
//                    .size(180.dp, 100.dp)
//                    .drawWithCache {
//                        val path = Path()
//                        path.moveTo(0f, 0f)
//                        path.lineTo(size.width / 2f, size.height / 2f)
//                        path.lineTo(size.width, 0f)
//                        path.close()
//                        onDrawBehind {
//                            drawPath(
//                                path,
//                                Success,
//                                style = Stroke(width = 10f, cap = StrokeCap.Square)
//                            )
//                        }
//                    }
//            )
//
//            Text("测试文本",
//                modifier = Modifier
//                    .size(180.dp, 100.dp)
//                    .drawWithCache {
//                        val brush = Brush.linearGradient(
//                            listOf(
//                                Error,
//                                Success
//                            )
//                        )
//
//                        onDrawBehind {
//                            drawRoundRect(
//                                brush,
//                                cornerRadius = CornerRadius(20.dp.toPx())
//                            )
//                        }
//                    }
//            )
//            Image(painter = painterResource(id = R.drawable.bg_home_background), "",
//                modifier = Modifier
//                    .size(200.dp)
//                    .background(
//                        Brush.linearGradient(
//                            listOf(
//                                Color(0xFFC5E1A5),
//                                Color(0xFF80DEEA)
//                            )
//                        )
//                    )
//                    .padding(16.dp)
//                    .graphicsLayer {
//                        compositingStrategy = CompositingStrategy.Offscreen
//                    }
//                    .drawWithCache {
//                        val path = Path()
//                        path.addRect(
//                            Rect(
//                                topLeft = Offset.Zero,
//                                bottomRight = Offset(size.width, size.height)
//                            )
//                        )
//                        onDrawWithContent {
//                            clipPath(path) {
//                                this@onDrawWithContent.drawContent()
//                            }
//
//                            val dotSize = size.width / 8f
//                            // Clip a white border for the content
//                            drawCircle(
//                                Color.Black,
//                                radius = dotSize,
//                                center = Offset(
//                                    x = size.width - dotSize,
//                                    y = size.height - dotSize
//                                ),
//                                blendMode = BlendMode.Clear
//                            )
//                            // draw the red circle indication
//                            drawCircle(
//                                Color(0xFFEF5350), radius = dotSize * 0.8f,
//                                center = Offset(
//                                    x = size.width - dotSize,
//                                    y = size.height - dotSize
//                                )
//                            )
//                        }
//                    }
//            )


//            val brush =
//                ShaderBrush(ImageShader(ImageBitmap.imageResource(R.drawable.bg_home_background)))
//
//            Text(
//                text = "测试文本内容!",
//                style = TextStyle(
//                    brush = brush,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 48.sp
//                )
//            )
//
//            Canvas(modifier = Modifier.size(100.dp)) {
//                drawCircle(brush = brush)
//            }


//            val colors = listOf(
//                Color(0xFFca63af),
//                Color(0xFFc05dba),
//                Color(0xFFb05ac7),
//                Color(0xFF9a59d5),
//                Color(0xFF7d6fe9),
//                Color(0xFF5682f7),
//                Color(0xFF0092ff),
//                Color(0xFF00cdff),
//                Color(0xFF00e5fd),
//                Color(0xFF00b1ff),
//                Color(0xFF5ffbf1)
//            )
//
//            val fontSize = 24.sp.toPx()
//            val doubleFontSize = fontSize * 2f
//
//            val infiniteTransition = rememberInfiniteTransition(label = "")
//            val offset by
//            infiniteTransition.animateFloat(
//                initialValue = 0f,
//                targetValue = doubleFontSize,
//                animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
//                label = ""
//            )
//
//
//            val colorfulBrush = Brush.linearGradient(
//                colors,
//                start = Offset(offset, offset),
//                end = Offset(offset + fontSize, offset + fontSize),
//                tileMode = TileMode.Mirror
//            )
//
//            Text(
//                text = "测试文本内容!测试文本内容!测试文本内容!测试文本内容!测试文本内容!测试文本内容!",
//                style = TextStyle(
//                    brush = colorfulBrush,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 24.sp
//                ),
//                modifier = Modifier.width(200.dp)
//            )


        }
    }

    private class TicketShape(
        private val cornerRadius: Float
    ) : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            return Outline.Generic(
                path = drawTicketPath(size, cornerRadius)
            )
        }

        fun drawTicketPath(size: Size, cornerRadius: Float): Path {
            return Path().apply {
                reset()
                // Top left arc
                arcTo(
                    rect = Rect(
                        left = -cornerRadius,
                        top = -cornerRadius,
                        right = cornerRadius,
                        bottom = cornerRadius
                    ),
                    startAngleDegrees = 90.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
                lineTo(x = size.width - cornerRadius, y = 0f)
                // Top right arc
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius,
                        top = -cornerRadius,
                        right = size.width + cornerRadius,
                        bottom = cornerRadius
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
                lineTo(x = size.width, y = size.height - cornerRadius)
                // Bottom right arc
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius,
                        top = size.height - cornerRadius,
                        right = size.width + cornerRadius,
                        bottom = size.height + cornerRadius
                    ),
                    startAngleDegrees = 270.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
                lineTo(x = cornerRadius, y = size.height)
                // Bottom left arc
                arcTo(
                    rect = Rect(
                        left = -cornerRadius,
                        top = size.height - cornerRadius,
                        right = cornerRadius,
                        bottom = size.height + cornerRadius
                    ),
                    startAngleDegrees = 0.0f,
                    sweepAngleDegrees = -90.0f,
                    forceMoveTo = false
                )
                lineTo(x = 0f, y = cornerRadius)
                close()
            }
        }
    }


//            PaimonsNotebookTheme(this) {
//                val state = rememberLazyListState()
//                val firstIndex by remember { derivedStateOf { state.firstVisibleItemIndex } }
//                val firstOffset by remember {
//                    derivedStateOf { state.firstVisibleItemScrollOffset }
//                }
//
//                val itemWidth = 260.dp
//
//                val itemWidthPx = remember {
//                    itemWidth.toPx()
//                }
//
//                val halfWidthPx = remember {
//                    -(itemWidthPx * .2f).toInt()
//                }
//
//                val screenWidthPx = remember {
//                    resources.displayMetrics.widthPixels
//                }
//
//                val startToEndLimit = remember {
//                    (screenWidthPx - itemWidthPx / 2 + -30.dp.toPx()).toInt()
//                }
//
//                val endToStartLimit = remember {
//                    abs(screenWidthPx - itemWidthPx - itemWidthPx / 2).toInt()
//                }
//
//                var isDragged by remember {
//                    mutableStateOf(false)
//                }
//
//                var offset by remember {
//                    mutableStateOf(Offset.Zero)
//                }
//
//                LaunchedEffect(offset, isDragged) {
//                    if (isDragged) {
//                        state.scrollBy(offset.x)
//
//                        when (firstIndex) {
//                            //当滚动到开头时,跳转至结尾
//                            0 -> {
//                                val items = state.layoutInfo.visibleItemsInfo
//
//                                val secondItem = if (items.size >= 2) {
//                                    items[1]
//                                } else {
//                                    return@LaunchedEffect
//                                }
//
//                                if (secondItem.offset > startToEndLimit) {
//                                    state.scrollToItem(list.size - 1, -startToEndLimit)
//                                }
//                            }
//                            //当滚动到结尾时,跳转开头
//                            else -> {
//                                val lastVisibleItem = state.layoutInfo.visibleItemsInfo.last()
//                                val lastOffset = lastVisibleItem.offset
//
//                                if (lastVisibleItem.index == list.size - 1 && lastOffset < startToEndLimit) {
//                                    state.scrollToItem(0, endToStartLimit)
//                                }
//                            }
//                        }
//                    } else {
//                        var currentIndex = firstIndex
//                        state.animateScrollBy(0f)
//
//                        val items = state.layoutInfo.visibleItemsInfo
//
//                        val item = if (items.size == 3) {
//                            items[1]
//                        } else {
//                            items[0]
//                        }
//
//                        println("offset = ${item.offset} abs = ${abs(halfWidthPx)}")
//
//                        if (item.offset < 0 && item.offset > -(itemWidthPx / 2)) {
//                            currentIndex++
//                        }
//
//                        if (firstIndex == 0) {
//                            state.scrollToItem(list.size - 1, -startToEndLimit)
//                            currentIndex = list.size - 2
//                        }
//
//                        state.animateScrollToItem(currentIndex, halfWidthPx)
//                        delay(3000)
//
//                        while (true) {
//                            if (currentIndex >= list.size - 2) {
//                                currentIndex = 0
//                                state.scrollToItem(currentIndex, offset.x.toInt())
//                            }
//                            currentIndex++
//                            state.animateScrollToItem(currentIndex, halfWidthPx)
//                            delay(3000)
//                        }
//                    }
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(BackGroundColor)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(180.dp)
//                    ) {
//                        LazyRow(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Black),
//                            horizontalArrangement = Arrangement.spacedBy(
//                                (-30).dp, Alignment.CenterHorizontally
//                            ),
//                            state = state,
//                            userScrollEnabled = false,
//                            contentPadding = PaddingValues(0.dp, 16.dp)
//                        ) {
//                            itemsIndexed(list) { index, item ->
//                                val show = index - 1 == firstIndex
//                                val anim by animateFloatAsState(
//                                    targetValue = if (show) 1f else .8f, label = ""
//                                )
//
//                                val zIndex = remember(show) {
//                                    if (show) 1f else 0f
//                                }
//
//                                Box(
//                                    modifier = Modifier
//                                        .zIndex(zIndex)
//                                        .fillMaxHeight()
//                                        .width(itemWidth)
//                                        .scale(anim)
//                                        .background(item.second),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Image(
//                                        painter = painterResource(id = item.first),
//                                        contentDescription = null,
//                                        modifier = Modifier.fillMaxSize()
//                                    )
//
//                                    Spacer(
//                                        modifier = Modifier
//                                            .width(1.dp)
//                                            .fillMaxHeight()
//                                            .background(Purple700)
//                                    )
//
//                                    Text(text = "$index", fontSize = 24.sp, color = Error)
//                                }
//                            }
//                        }
//
//                        Box(modifier = Modifier
//                            .fillMaxSize()
//                            .pointerInput(Unit) {
//                                detectDragGestures(onDragStart = {
//                                    isDragged = true
//                                }, onDragEnd = {
//                                    isDragged = false
//                                }, onDrag = { _, dragAmount ->
//                                    offset = -dragAmount
//                                })
//                            })
//                    }
//                }
//            }
}