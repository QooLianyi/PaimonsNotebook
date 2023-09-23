package com.lianyi.paimonsnotebook.ui.screen.account.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.helper_text.data.HelperTextData
import com.lianyi.paimonsnotebook.common.components.helper_text.view.HelperText
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingBottom
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Gray_97

@Composable
fun CookieInputDialog(
    inputValue: String,
    inputValueChange: (String) -> Unit,
    helperTextHints: List<HelperTextData>,
    onDismissRequest: () -> Unit,
    onSuccess: () -> Unit,
) {

    LazyColumnDialog(
        title = "通过Cookie添加账号", onDismissRequest = onDismissRequest,
        buttons = arrayOf("确定"),
        onClickButton = {
            onSuccess()
        },
    ) {
        item {
            InputTextFiled(value = inputValue,
                onValueChange = inputValueChange,
                inputFieldHeight = 120.dp,
                padding = 8.dp,
                borderColor = Black_10,
                borderWidth = 2.dp,
                placeholder = {
                    Text(
                        text = "在此处输入Cookie",
                        color = Gray_97,
                        fontSize = 14.sp
                    )
                })
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(helperTextHints.split(2)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingBottom(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                it.forEach {
                    HelperText(
                        modifier = Modifier.weight(1f),
                        text = it.text,
                        status = it.state.value
                    )
                }
            }
        }
    }

}