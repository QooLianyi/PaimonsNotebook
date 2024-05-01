package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import com.lianyi.paimonsnotebook.ui.screen.home.view.TopicScreen

@Composable
fun DebugPostContent() {
    var input by remember {
        mutableStateOf("")
    }
    InputTextFiled(value = input, onValueChange = {
        input = it
        println(it)
    })
    Button(onClick = {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(PostDetailScreen::class.java)
            putExtra(PostHelper.PARAM_POST_ID, input.toLongOrNull() ?: 0L)
        }
    }) {
        Text(text = "通过ID跳转帖子详情", fontSize = 18.sp)
    }
    Button(onClick = {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(TopicScreen::class.java)
            putExtra(PostHelper.PARAM_TOPIC_ID, input.toLongOrNull() ?: 0L)
        }
    }) {
        Text(text = "通过ID跳转主题详情", fontSize = 18.sp)
    }
}