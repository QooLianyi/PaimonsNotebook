package com.lianyi.paimonsnotebook.common.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.PaimonsNotebookNotificationCard
import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotification

/*
* 通知组件
* */
@Composable
fun PaimonsNotebookNotificationComponents() {

//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    if (notificationListIsEmpty) {
//        expanded = false
//    }
//
//    val backgroundAnim by animateFloatAsState(
//        targetValue = if (expanded) 1f else 0f,
//        animationSpec = tween(300)
//    )

    Box(
        modifier = Modifier
            .zIndex(100f)
            .fillMaxSize()
//            .background(
//                White, shape = CirclePath(
//                    progress = backgroundAnim,
//                    start = CirclePathStartPoint.TopEnd,
//                    offset = Offset(-25f, -15f)
//                )
//            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 12.dp, 12.dp, 0.dp),
            horizontalAlignment = Alignment.End
        ) {
            StatusBarPaddingSpacer()

            PaimonsNotebookNotification.notifications.forEachIndexed { index, data ->
                key(data.notificationId) {
                    if (index != 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    PaimonsNotebookNotificationCard(data = data)
                }
            }
        }

//        AnimatedVisibility(
//            visible = expanded,
//            enter = slideIn { IntOffset(0,-it.height) },
//            exit = slideOut{ IntOffset(0,-it.height) }
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp, 12.dp, 12.dp, 0.dp)
//            ) {
//
//            }
//        }

//        Crossfade(
//            targetState = notificationListIsEmpty,
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .zIndex(1100f)
//        ) {
//            if (!it) {
//                Box {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_channel),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(4.dp))
//                            .padding(12.dp)
//                            .size(30.dp)
//                            .clickable {
//                                expanded = !expanded
//                            }
//                    )
//                    Text(
//                        text = "${PaimonsNotebookNotification.list.size}",
//                        fontSize = 10.sp,
//                        color = White,
//                        modifier = Modifier
//                            .align(Alignment.TopCenter)
//                            .offset(x = 8.dp, y = 5.dp)
//                            .clip(RoundedCornerShape(6.dp))
//                            .background(Error)
//                            .padding(1.dp)
//                    )
//                }
//            }
//        }

    }
}
