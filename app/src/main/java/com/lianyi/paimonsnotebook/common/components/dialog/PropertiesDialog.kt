package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun PropertiesDialog(
    title: String,
    properties: List<Pair<String, String>>,
    onDismissRequest: () -> Unit,
    fontSize: TextUnit = 16.sp,
    buttonGroupAlign: Arrangement.Horizontal = Arrangement.End,
    buttons: Array<String> = arrayOf("关闭"),
    slot: @Composable ColumnScope.() -> Unit = {},
    onButtonClick: (Int) -> Unit = {},
    onCopyPropertiesValue: (String) -> Unit = {},
) {
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
                    .padding(16.dp)
            ) {

                Text(
                    text = title,
                    fontSize = 22.sp,
                    color = Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                properties.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 1.5.dp)
                    ) {
                        Text(
                            text = it.first,
                            fontSize = fontSize,
                            color = Black,
                            modifier = Modifier.requiredWidthIn(keyWidth, propertiesKeyMaxWidth)
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

                slot.invoke(this)

                if (buttons.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = buttonGroupAlign
                    ) {
                        buttons.forEachIndexed { index, s ->
                            TextButton(onClick = { onButtonClick(index) }) {
                                Text(text = s, fontSize = 18.sp, color = Primary_1)
                            }
                            if (index != buttons.size - 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}