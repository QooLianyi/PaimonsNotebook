package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_60
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
    inputFieldMaxHeight: Dp = 40.dp,
    padding: PaddingValues = PaddingValues(8.dp, 5.dp, 5.dp, 5.dp),
    backgroundColor: Color = White,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Gray_F0,
    borderRadius: Dp = 2.dp,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = Black),
    contentAlignment: Alignment = Alignment.TopStart,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    placeholder: @Composable () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        cursorBrush = SolidColor(colorPrimary),
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = keyboardActions, maxLines = maxLines,
        enabled = enabled,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .radius(borderRadius)
                    .fillMaxWidth()
                    .requiredHeightIn(inputFieldHeight, inputFieldMaxHeight)
                    .background(backgroundColor)
                    .border(borderWidth, borderColor, RoundedCornerShape(borderRadius))
                    .padding(padding)
                    .focusRequester(focusRequester),
                contentAlignment = contentAlignment
            ) {
                if (value.isBlank()) {
                    placeholder()
                }
                innerTextField()
            }
        }, textStyle = textStyle
    )
}


@Composable
fun InputTextFiled(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    inputFieldHeight: Dp = 40.dp,
    inputFieldMaxHeight: Dp = 40.dp,
    padding: PaddingValues = PaddingValues(8.dp, 5.dp, 5.dp, 5.dp),
    backgroundColor: Color = White,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Gray_F0,
    borderRadius: Dp = 2.dp,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = Black),
    contentAlignment: Alignment = Alignment.TopStart,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    placeholder: String,
) {
    InputTextFiled(
        modifier,
        value,
        onValueChange,
        inputFieldHeight,
        inputFieldMaxHeight,
        padding,
        backgroundColor,
        borderWidth,
        borderColor,
        borderRadius,
        textStyle, contentAlignment, imeAction, keyboardActions, keyboardOptions, maxLines, enabled,
        placeholder = {
            Text(text = placeholder, fontSize = 14.sp, color = Black_60)
        }
    )
}

@Composable
fun PasswordInputTextFiled(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    inputFieldHeight: Dp = 40.dp,
    inputFieldMaxHeight: Dp = 40.dp,
    padding: PaddingValues = PaddingValues(8.dp, 5.dp, 5.dp, 5.dp),
    backgroundColor: Color = White,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Gray_F0,
    borderRadius: Dp = 2.dp,
    textStyle: TextStyle = TextStyle(fontSize = 18.sp, color = Black),
    contentAlignment: Alignment = Alignment.TopStart,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    placeholder: String,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(colorPrimary),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = keyboardActions, maxLines = maxLines,
        visualTransformation = PasswordVisualTransformation(),
        enabled = enabled,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .radius(borderRadius)
                    .fillMaxWidth()
                    .requiredHeightIn(inputFieldHeight, inputFieldMaxHeight)
                    .background(backgroundColor)
                    .border(borderWidth, borderColor, RoundedCornerShape(borderRadius))
                    .focusRequester(focusRequester)
                    .padding(padding),
                contentAlignment = contentAlignment
            ) {
                if (value.isBlank()) {
                    Text(text = placeholder, fontSize = 14.sp, color = Black_60)
                }
                innerTextField()
            }
        }, textStyle = textStyle, modifier = modifier
    )
}


