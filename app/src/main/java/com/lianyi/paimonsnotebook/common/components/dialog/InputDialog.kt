package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.components.widget.PasswordInputTextFiled
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.LoginByAccountModel
import com.lianyi.paimonsnotebook.ui.theme.Error


@Composable
fun InputDialog(
    title: String = "输入框",
    placeholder: String = "请输入内容",
    initialValue: String = "",
    hint: String = "",
    onlyNumber: Boolean = false,
    onConfirm: (value: String) -> Unit,
    onCancel: () -> Unit,
    textMaxLength: Int = Int.MAX_VALUE
) {
    //输入值
    var value by remember {
        mutableStateOf(initialValue)
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
        verticalSpacedBy = 12.dp,
        content = {
            item {
                InputTextFiled(
                    value = value, onValueChange = { newValue ->
                        if (newValue.length > textMaxLength) return@InputTextFiled

                        value = if (onlyNumber) {
                            newValue.filter {
                                it.isDigit()
                            }
                        } else {
                            newValue
                        }
                    }, placeholder = placeholder,
                    contentAlignment = Alignment.CenterStart
                )
            }
            if (hint.isNotEmpty()) {
                item {
                    Text(text = "*${hint}", fontSize = 12.sp, color = Error)
                }
            }
        }
    )
}


@Composable
fun PasswordInputDialog(
    title: String = "账号登录",
    showHint: Boolean = true,
    onConfirm: (LoginByAccountModel) -> Unit,
    onCancel: () -> Unit,
    textMaxLength: Int = Int.MAX_VALUE
) {
    //输入值
    var account by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    LazyColumnDialog(
        title = title,
        onDismissRequest = onCancel, buttons = arrayOf("取消", "确定"),
        verticalSpacedBy = 6.dp,
        onClickButton = {
            if (it == 0) {
                onCancel.invoke()
            } else {
                onConfirm.invoke(
                    LoginByAccountModel(account, password)
                )
            }
        },
        content = {
            item {
                InputTextFiled(
                    value = account, onValueChange = {
                        if (it.length < textMaxLength) {
                            account = it
                        }
                    }, placeholder = "账号",
                    contentAlignment = Alignment.CenterStart
                )
            }

            item {
                PasswordInputTextFiled(
                    value = password,
                    onValueChange = {
                        if (it.length < textMaxLength) {
                            password = it
                        }
                    },
                    placeholder = "密码",
                    contentAlignment = Alignment.CenterStart,
                )
            }

            if (showHint) {
                item {
                    Column {
                        Text(
                            text = "*程序不会保存输入的账号与密码",
                            fontSize = 12.sp,
                            color = Error
                        )
                        Text(
                            text = "*需要在使用短信验证码登录一次后,才能使用账号密码登录",
                            fontSize = 12.sp,
                            color = Error
                        )
                    }
                }
            }
        }
    )
}

