package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InformationDialog(
    title: String = "信息",
    content: String,
    buttons:Array<String> = arrayOf("取消", "确定"),
    onClickButton:(Int)->Unit
) {
    LazyColumnDialog(title = title, titleSpacer = 20.dp, onDismissRequest = { },
        buttons = buttons,
        onClickButton = onClickButton
    ) {
        item {
            Text(text = content, fontSize = 16.sp)
        }
    }
}