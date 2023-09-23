package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import android.content.Intent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen

@Composable
fun DebugPostContent() {
    var postId by remember {
        mutableStateOf("")
    }
    InputTextFiled(value = postId, onValueChange = {
        postId = it
        println(it)
    })
    Button(onClick = {
        with(PaimonsNotebookApplication.context) {
            startActivity(
                Intent(
                    PaimonsNotebookApplication.context,
                    PostDetailScreen::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("postId", postId.toLongOrNull() ?: 0L)
                })
        }
    }) {
        Text(text = "通过ID跳转帖子详情", fontSize = 18.sp)
    }
}