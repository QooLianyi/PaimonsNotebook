package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled


@Composable
fun InputDialog(
    title: String = "输入框",
    placeholder: String = "请输入内容",
    onConfirm: (value: String) -> Unit,
    onCancel: () -> Unit,
) {
    //输入值
    var value by remember {
        mutableStateOf("")
    }

    LazyColumnDialog(
        title = title,
        onDismissRequest = onCancel, buttons = arrayOf("取消", "确定"),
        onClickButton = {
            if (it == 0) {
                onCancel.invoke()
            } else {
                onConfirm.invoke(value)
            }
        },
        content = {
            item {
                InputTextFiled(
                    value = value, onValueChange = {
                        value = it
                    }, placeholder = placeholder,
                    contentAlignment = Alignment.CenterStart
                )
            }
        }
    )

}