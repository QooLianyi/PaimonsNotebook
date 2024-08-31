package com.lianyi.paimonsnotebook.ui.widgets.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import kotlin.math.roundToInt

@Composable
fun RemoteViewsPreviewTemplate(
    previewAnimData: RemoteViewsPreviewAnimData
) {
    Row(
        modifier = Modifier
            .radius(previewAnimData.backgroundRadius.value.roundToInt().dp)
            .background(previewAnimData.backgroundColor.value)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

    }
}
