package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugGachaContent() {

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PaimonsNotebookDatabase.database.gachaItemsDao.deleteAllGachaLogItem()
            PreferenceKeys.GachaRecordCurrentGameUid.editValue("")
            launch(Dispatchers.Main) {
                "祈愿数据已清空".show()
            }
        }
    }) {
        Text(text = "清空全部祈愿数据", fontSize = 16.sp)
    }

}