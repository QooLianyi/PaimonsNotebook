package com.lianyi.paimonsnotebook.ui.screen.develop

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateListOf
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementData
import com.lianyi.paimonsnotebook.ui.screen.setting.data.StaticResourcesChannelData
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme


class TypographyScreen : BaseActivity() {

    private lateinit var start: ActivityResultLauncher<Intent>

    override fun onStartActivityForResult(result: ActivityResult) {
        super.onStartActivityForResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start = registerStartActivityForResult()

        setContent {
//            PaimonsNotebookTheme(this) {
//
//                ContentSpacerLazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(BackGroundColor)
//                ) {
//                    item {
//                        Button(onClick = {
//                            val achi = FileHelper.getMetadata(MetadataHelper.FileNameAchievement)
//                            val goalFile = FileHelper.getMetadata(MetadataHelper.FileNameAchievementGoal)
//
//                            if (achi == null || goalFile == null) {
//                                "缺少文件".errorNotify()
//                                return@Button
//                            }
//
//                            JsonReader(InputStreamReader(achi.inputStream())).use {
//                                it.beginArray()
//
//                                while (it.hasNext()) {
//
//                                    var id = -1
//                                    var goal = -1
//                                    var order = -1
//                                    var process = -1
//                                    var previousId = 0
//                                    var isDeleteWatcher = false
//                                    var title = ""
//                                    var description = ""
//                                    var version = ""
//                                    var reward = AchievementData.FinishReward(0, 0)
//                                    var icon = ""
//
//                                    it.apply {
//                                        beginObject()
//
//                                        while (hasNext()) {
//                                            when (nextName()) {
//                                                UIAFHelper.Field.Data.Id -> id = nextInt()
//                                                UIAFHelper.Field.Data.Goal -> goal = nextInt()
//                                                UIAFHelper.Field.Data.Order -> order = nextInt()
//                                                UIAFHelper.Field.Data.Title -> title = nextString()
//                                                UIAFHelper.Field.Data.Description -> description =
//                                                    nextString()
//
//                                                UIAFHelper.Field.Data.FinishReward -> {
//                                                    beginObject()
//
//                                                    var rewardId = -1
//                                                    var count = -1
//                                                    while (hasNext()) {
//                                                        when (nextName()) {
//                                                            UIAFHelper.Field.Data.Reward.Id -> rewardId =
//                                                                nextInt()
//
//                                                            UIAFHelper.Field.Data.Reward.Count -> count =
//                                                                nextInt()
//                                                        }
//                                                    }
//                                                    endObject()
//
//                                                    reward = AchievementData.FinishReward(count, rewardId)
//
//                                                    continue
//                                                }
//
//                                                UIAFHelper.Field.Data.IsDeleteWatcherAfterFinish -> isDeleteWatcher =
//                                                    nextBoolean()
//
//                                                UIAFHelper.Field.Data.Progress -> process = nextInt()
//                                                UIAFHelper.Field.Data.Version -> version = nextString()
//                                                UIAFHelper.Field.Data.Icon -> icon = nextString()
//                                                UIAFHelper.Field.Data.PreviousId -> previousId = nextInt()
//                                            }
//                                        }
//
//                                        endObject()
//                                    }
//
//                                    list += AchievementData(
//                                        description,
//                                        reward,
//                                        goal,
//                                        id,
//                                        isDeleteWatcher,
//                                        order,
//                                        process,
//                                        title,
//                                        version,
//                                        icon,
//                                        previousId
//                                    )
//                                }
//
//                                it.endArray()
//                            }
//                        }) {
//                            Text(text = "JSON流测试")
//                        }
//                    }
//                    items(list) { item ->
//                        Row(
//                            modifier = Modifier
//                                .padding(10.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Column {
//                                TitleText(text = item.title)
//                                Spacer(modifier = Modifier.height(2.dp))
//                                Text(text = item.description, fontSize = 12.sp, color = Font_Second)
//                            }
//                        }
//                    }
//                }
//
//            }

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
    }
}