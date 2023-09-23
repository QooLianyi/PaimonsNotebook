package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Gray_F0
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.colorPrimary

/*
* 输入框
*
* */
@Composable
fun InputTextFiled(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    inputFieldHeight: Dp = 40.dp,
    padding: Dp = 5.dp,
    backgroundColor: Color = White,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Gray_F0,
    borderRadius: Dp = 2.dp,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = Black),
    contentAlignment: Alignment = Alignment.TopStart,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: @Composable () -> Unit = {},
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(colorPrimary),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .radius(borderRadius)
                    .fillMaxWidth()
                    .height(inputFieldHeight)
                    .background(backgroundColor)
                    .border(borderWidth, borderColor, RoundedCornerShape(borderRadius))
                    .padding(padding),
                contentAlignment = contentAlignment
            ) {
                if (value.isBlank()) {
                    placeholder()
                }
                innerTextField()
            }
        }, textStyle = textStyle, modifier = modifier
    )
}