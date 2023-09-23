package com.lianyi.paimonsnotebook.ui.screen.daily_note.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Info

@Composable
internal fun DailyNoteSettingDialog(
    onDismissRequest:()->Unit,
    onButtonClick:()->Unit
){
    var optionSortValue by remember {
        mutableIntStateOf(1)
    }
}