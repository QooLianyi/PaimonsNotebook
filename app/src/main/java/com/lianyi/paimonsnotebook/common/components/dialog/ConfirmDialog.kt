package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmDialog(
    title: String = "提示",
    content: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    LazyColumnDialog(title = title, titleSpacer = 20.dp, onDismissRequest = { },
        buttons = arrayOf("取消", "确定"),
        onClickButton = {
            if (it == 0) {
                onCancel.invoke()
            } else {
                onConfirm.invoke()
            }
        }
    ) {
        item {
            Text(text = content, fontSize = 16.sp)
        }
    }
}