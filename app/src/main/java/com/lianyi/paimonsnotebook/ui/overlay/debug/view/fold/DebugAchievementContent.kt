package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.ui.screen.achievement.util.helper.AchievementHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun DebugAchievementContent() {
    Column {
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                PaimonsNotebookDatabase.database.achievementUserDao.deleteAllUser()
                launch(Dispatchers.Main) {
                    "成就数据已清空".show()
                }
            }
        }) {
            Text(text = "清空全部成就数据", fontSize = 16.sp)
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val selectedUserId =
                    PaimonsNotebookDatabase.database.achievementUserDao.getSelectedUser()?.id
                        ?: return@launch

                val ids = mutableListOf<Int>()

                repeat(9999) {
                    ids += it + 1
                }

                val count = AchievementHelper.getAchievementsFinishCountByIds(selectedUserId, ids)

                println("count = ${count}")
            }
        }) {
            Text(text = "获取完成个数(随机不存在的Id测试)", fontSize = 16.sp)
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val selectedUserId =
                    PaimonsNotebookDatabase.database.achievementUserDao.getSelectedUser()?.id
                        ?: return@launch

                val currentAchievements = PaimonsNotebookDatabase.database.achievementsDao.getAchievementListByUserId(selectedUserId).first()

                val tIds = currentAchievements
                        .map { it.id }

                val ids = mutableListOf<Int>()

                println("tIds.size = ${tIds.size}")

                repeat(100) {
                    ids += tIds
                }

                println("ids.size = ${ids.size}")

                val count = AchievementHelper.getAchievementsFinishCountByIds(selectedUserId, ids)
                println("count = ${count}")
            }
        }) {
            Text(text = "获取完成个数(重复的Id测试)", fontSize = 16.sp)
        }
    }
}

