package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 文件属性操作对话框
* */
@Composable
fun FilePropertiesOperationDialog(
    title: String,
    properties: List<Pair<String, String>>,
    onDismissRequest: () -> Unit,
    fontSize: TextUnit = 16.sp,
    onCopyPropertiesValue: (String) -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickRename: () -> Unit = {},
    onClickSend: () -> Unit = {}
) {
    val icons = remember {
        listOf(
            R.drawable.ic_delete to "删除",
            R.drawable.ic_edit to "修改",
            R.drawable.ic_share to "发送"
        )
    }

    val buttons = remember {
        arrayOf("操作", "关闭")
    }

    var showDropMenu by remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = onDismissRequest) {

        BoxWithConstraints {

            val propertiesKeyMaxWidth = maxWidth * .333f

            val keyWidth =
                with(LocalDensity.current) {
                    (fontSize * properties.maxOf { it.first.length }).toDp()
                }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .background(White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = title,
                    fontSize = 22.sp,
                    color = Black,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    properties.forEach {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = it.first,
                                fontSize = fontSize,
                                color = Black,
                                modifier = Modifier.requiredWidthIn(
                                    keyWidth,
                                    propertiesKeyMaxWidth
                                )
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(text = it.second,
                                fontSize = fontSize,
                                color = Black,
                                modifier = Modifier
                                    .clickable {
                                        onCopyPropertiesValue(it.second)
                                    }
                                    .fillMaxWidth())
                        }
                    }
                }


                Box {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        buttons.forEachIndexed { index, s ->
                            TextButton(onClick = {
                                if (index == 0) {
                                    showDropMenu = !showDropMenu
                                    return@TextButton
                                }

                                onDismissRequest.invoke()
                            }) {
                                Text(text = s, fontSize = 18.sp, color = Primary_1)
                            }
                        }
                    }

                    DropdownMenu(expanded = showDropMenu, onDismissRequest = {
                        showDropMenu = false
                    }) {
                        icons.forEach { pair ->
                            DropdownMenuItem(onClick = {
                                when (pair.first) {
                                    R.drawable.ic_delete -> onClickDelete.invoke()
                                    R.drawable.ic_edit -> onClickRename.invoke()
                                    R.drawable.ic_share -> onClickSend.invoke()
                                }
                                showDropMenu = false
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = pair.first),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(26.dp)
                                            .padding(2.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    PrimaryText(text = pair.second)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
