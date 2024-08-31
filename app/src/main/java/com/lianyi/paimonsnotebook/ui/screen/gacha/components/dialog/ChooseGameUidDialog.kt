package com.lianyi.paimonsnotebook.ui.screen.gacha.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Primary_8
import com.lianyi.paimonsnotebook.ui.theme.Success
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun ChooseGameUidDialog(
    uidList: List<String>,
    onConfirm: (List<String>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selectedUidList = remember {
        mutableStateListOf<String>()
    }

    LazyColumnDialog(title = "选择要导出的uid",
        onDismissRequest = onDismissRequest,
        onClickButton = {
            if (it == 1) {
                onConfirm.invoke(selectedUidList)
            } else {
                onDismissRequest.invoke()
            }
        },
        buttons = arrayOf(
            "关闭", "导出"
        ),
        verticalSpacedBy = 4.dp,
        content = {

            items(uidList) { uid ->
                val selected = selectedUidList.indexOf(uid) != -1
                Row(
                    modifier = Modifier
                        .radius(2.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (selected) {
                                selectedUidList -= uid
                            } else {
                                selectedUidList += uid
                            }
                        }
                        .background(
                            if (selected) Primary_8
                            else Transparent
                        )
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uid,
                        fontSize = 16.sp,
                        color = Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                InfoText(text = "*可多选,多个uid的记录会存储在同一个文件中")
            }
        }
    )

}