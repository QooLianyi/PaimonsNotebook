package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Color) -> Unit,
) {
    val controller = rememberColorPickerController().apply {
        setDebounceDuration(100)
    }

    LazyColumnDialog(title = "色彩选择器",
        onDismissRequest = onDismissRequest,
        buttons = arrayOf("取消", "确定"),
        onClickButton = {
            if (it == 1) {
                onConfirm.invoke(controller.selectedColor.value)
            }
            onDismissRequest.invoke()
        }
    ) {
        item {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AlphaTile(
                    modifier = Modifier
                        .radius(4.dp)
                        .size(60.dp), controller
                )

                Spacer(modifier = Modifier.height(6.dp))

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(6.dp))

                AlphaSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                BrightnessSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )
            }
        }
    }
}