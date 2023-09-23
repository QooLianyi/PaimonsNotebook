package com.lianyi.paimonsnotebook.common.components.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LoadingAnimationPlaceholder()
        }
    }
}