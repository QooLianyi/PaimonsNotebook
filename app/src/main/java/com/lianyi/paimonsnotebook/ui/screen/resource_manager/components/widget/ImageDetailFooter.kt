package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black_90
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun ImageDetailFooter(
    deleteImage: () -> Unit,
    saveImage: () -> Unit,
    showImageProperty: () -> Unit
) {
    val list = remember {
        listOf(
            Triple(R.drawable.ic_delete, "删除", deleteImage),
            Triple(R.drawable.ic_save_image, "保存", saveImage),
            Triple(R.drawable.ic_view_property, "属性", showImageProperty)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
            }
            .background(White)
            .padding(8.dp, 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            list.forEach {
                Column(
                    modifier = Modifier
                        .radius(2.dp)
                        .requiredWidthIn(40.dp, 120.dp)
                        .clickable {
                            it.third.invoke()
                        }
                        .padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(id = it.first), contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Black_90
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(text = it.second, fontSize = 12.sp, color = Font_Normal)
                }
            }
        }

        NavigationBarPaddingSpacer()
    }
}