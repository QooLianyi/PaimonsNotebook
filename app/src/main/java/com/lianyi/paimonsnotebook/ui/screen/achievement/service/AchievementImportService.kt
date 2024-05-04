package com.lianyi.paimonsnotebook.ui.screen.achievement.service

import androidx.sqlite.db.SupportSQLiteStatement
import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.json.JsonReaderHelper
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.ui.screen.achievement.data.UIAFJsonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader

/*
* 祈愿记录导入服务
* */
class AchievementImportService(
    val database: PaimonsNotebookDatabase = PaimonsNotebookDatabase.database,
) {

    /*
    * 缓存条数最大值: 999 / 5 = 199.8
    * 超出此值将无法解析导致报错
    * */
    private val cacheAchievementsRecordSize = 199

    //stmt 缓存,一般情况下只会缓存最大值与最后一条不到最大值的记录
    private val stmtMap = mutableMapOf<Int, SupportSQLiteStatement>()

    private var cacheUIAFInfo: UIAFJsonData.Info? = null

    suspend fun tryGetUIAFJsonInfo(file: File): UIAFJsonData.Info? {
        val jsonReader = JsonReader(InputStreamReader(file.inputStream()))

        return tryGetUIAFJsonInfo(jsonReader)
    }

    /*
    * 尝试获取UIAFJson数据
    * */
    private suspend fun tryGetUIAFJsonInfo(
        reader: JsonReader
    ): UIAFJsonData.Info? {
        var info: UIAFJsonData.Info? = null

        withContext(Dispatchers.IO) {
            try {
                JsonReaderHelper.getJsonReaderSingleFieldValue(reader, "info", onFound = {
                    info = getUIAFJsonInfo(it)
                    cacheUIAFInfo = info
                }, onNotFound = {
                    error("没有找到info字段")
                })
            } catch (e: Exception) {
                "发生了异常:${e.message}".errorNotify()
            }
        }

        return info
    }

    private fun getUIAFJsonInfo(
        reader: JsonReader
    ) = reader.let {
        reader.beginObject()
        val infoMap = mutableMapOf<String, String>()

        /*
        * 为map设置值,用于后续检查字段是否存在
        * */
        while (reader.hasNext()) {
            val key = when (reader.nextName()) {
                UIAFHelper.Field.Info.ExportTimestamp -> UIAFHelper.Field.Info.ExportTimestamp
                UIAFHelper.Field.Info.ExportApp -> UIAFHelper.Field.Info.ExportApp
                UIAFHelper.Field.Info.ExportAppVersion -> UIAFHelper.Field.Info.ExportAppVersion
                UIAFHelper.Field.Info.UIAFVersion -> UIAFHelper.Field.Info.UIAFVersion
                else -> ""
            }

            when (key) {
                UIAFHelper.Field.Info.ExportTimestamp -> infoMap[key] = reader.nextLong().toString()

                else -> infoMap[key] = reader.nextString()
            }
        }

        reader.endObject()

        UIAFJsonData.Info(
            export_app = infoMap[UIAFHelper.Field.Info.ExportApp] ?: "",
            export_app_version = infoMap[UIAFHelper.Field.Info.ExportAppVersion] ?: "",
            export_timestamp = infoMap[UIAFHelper.Field.Info.ExportTimestamp]?.toLongOrNull()
                ?: (System.currentTimeMillis() / 1000),
            uiaf_version = infoMap[UIAFHelper.Field.Info.UIAFVersion] ?: "",
        )
    }

    suspend fun importAchievementFromUIAFJson(
        userId: Int, file: File
    ) {
        try {
            withContext(Dispatchers.IO) {

                //注释具体参考UIGF Import Service
                if (cacheUIAFInfo == null) {
                    tryGetUIAFJsonInfo(file)
                }

                if (cacheUIAFInfo == null) {
                    return@withContext
                }

                val reader = JsonReader(InputStreamReader(file.inputStream()))

                var objectEnd = false

                JsonReaderHelper.getJsonReaderSingleFieldValue(
                    reader, "list",
                    onFound = {
                        launchIO {
                            saveUIAFJsonList(reader, userId)

                            while (!objectEnd) {
                                delay(1000)
                            }
                            it.close()
                        }
                    },
                    onNotFound = {
                        error("没有找到list字段")
                    },
                    autoClose = false,
                    onObjectEnd = {
                        objectEnd = true
                    }
                )
            }
        } catch (e: Exception) {
            "发生了异常:${e.message}".errorNotify()
        }
    }

    private fun achievementItemListFlush(
        list: List<Achievements>,
    ) {
        //当列表大小超过一次性的解析个数时拆分集合递归调用

        val items = if (list.size > cacheAchievementsRecordSize) {
            achievementItemListFlush(list.subList(cacheAchievementsRecordSize, list.size))
            list.subList(0, cacheAchievementsRecordSize - 1)
        } else {
            list
        }

        saveToDB(items)
    }

    /*
    * 传入的json reader对象的当前指针必须指向list的value部分
    * */
    private fun saveUIAFJsonList(
        reader: JsonReader,
        userId: Int
    ) {
        reader.apply {
            beginArray()

            val list = mutableListOf<Achievements>()

            while (hasNext()) {
                var id = -1
                var timestamp = UIAFHelper.INVALID_TIMESTAMP
                var current = UIAFHelper.INVALID_CURRENT
                var status = UIAFHelper.AchievementStatus.STATUS_INVALID

                beginObject()

                while (hasNext()) {
                    when (nextName()) {
                        UIAFHelper.Field.Item.Id -> id = nextInt()
                        UIAFHelper.Field.Item.Timestamp -> timestamp = nextLong()
                        UIAFHelper.Field.Item.Current -> current = nextInt()
                        UIAFHelper.Field.Item.Status -> status = nextInt()
                    }
                }

                list += Achievements(
                    id = id,
                    timestamp = timestamp,
                    current = current,
                    status = status,
                    userId = userId
                )

                if (list.size == cacheAchievementsRecordSize) {
                    achievementItemListFlush(list)
                    list.clear()
                }

                endObject()
            }
            endArray()

            //最后不为空时才执行插入
            if (list.isNotEmpty()) {
                achievementItemListFlush(list)
            }

            list.clear()
            close()
        }
    }

    private fun saveToDB(list: List<Achievements>) {
        val stmt = if (stmtMap[list.size] != null) {
            stmtMap[list.size]!!
        } else {
            //重复主键更新
            val sb =
                StringBuilder("INSERT OR REPLACE INTO achievements(id,current,status,timestamp,user_id) VALUES ")
            repeat(list.size) {
                when (it) {
                    0 -> {}
                    list.size -> {
                        sb.append(";")
                    }

                    else -> sb.append(",")
                }
                sb.append("(?, ?, ?, ?, ?)")
            }

            //缓存stmt
            database.compileStatement(sb.toString()).apply {
                stmtMap[list.size] = this
            }
        }

        list.forEachIndexed { index, uiafItem ->

            uiafItem.apply {
                stmt.bindLong(5 * index + 1, id.toLong())
                stmt.bindLong(5 * index + 2, current.toLong())
                stmt.bindLong(5 * index + 3, status.toLong())
                stmt.bindLong(5 * index + 4, timestamp)
                stmt.bindLong(5 * index + 5, userId.toLong())
            }
        }

        stmt.executeInsert()
        stmt.clearBindings()
    }
}