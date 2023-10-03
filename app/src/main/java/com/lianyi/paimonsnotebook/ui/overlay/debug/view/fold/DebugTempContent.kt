package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugTempContent() {
    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            val user = AccountHelper.selectedUserFlow.value
            if (user == null) {
                launch(Dispatchers.Main) {
                    "当前没有选中用户".show()
                }
                return@launch
            }

            val role = user.getSelectedGameRole()

            if (role == null) {
                launch(Dispatchers.Main) {
                    "当前用户没有默认角色".show()
                }
                return@launch
            }

        }
    }) {
        Text(text = "测试按钮", fontSize = 18.sp)
    }
}