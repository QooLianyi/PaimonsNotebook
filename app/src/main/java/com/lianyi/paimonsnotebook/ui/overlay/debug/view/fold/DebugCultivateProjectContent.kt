package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateProject
import com.lianyi.paimonsnotebook.common.extension.string.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugCultivateProjectContent() {
    Column {
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val c = PaimonsNotebookDatabase.database.cultivateProjectDao.deleteAll()
                println("c = $c")

                launch(Dispatchers.Main) {
                    "已清空".show()
                }
            }
        }) {
            Text(text = "清空全部养成计划表", fontSize = 16.sp)
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val c = PaimonsNotebookDatabase.database.cultivateEntityDao.deleteAll()
                println("c = $c")

                launch(Dispatchers.Main) {
                    "已清空".show()
                }
            }
        }) {
            Text(text = "清空全部养成实体表", fontSize = 16.sp)
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val c = PaimonsNotebookDatabase.database.cultivateItemsDao.deleteAll()
                println("c = $c")

                launch(Dispatchers.Main) {
                    "已清空".show()
                }
            }
        }) {
            Text(text = "清空全部养成项表", fontSize = 16.sp)
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val c = PaimonsNotebookDatabase.database.cultivateItemMaterialsDao.deleteAll()
                println("c = $c")

                launch(Dispatchers.Main) {
                    "已清空".show()
                }
            }
        }) {
            Text(text = "清空全部养成材料表", fontSize = 16.sp)
        }

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val c = PaimonsNotebookDatabase.database.cultivateProjectDao.insert(
                    CultivateProject(
                        -100,
                        "测试养成计划",
                        true
                    )
                )
                println("c = $c")

                launch(Dispatchers.Main) {
                    "已添加".show()
                }
            }
        }) {
            Text(text = "添加一个临时养成计划", fontSize = 16.sp)
        }
    }
}

