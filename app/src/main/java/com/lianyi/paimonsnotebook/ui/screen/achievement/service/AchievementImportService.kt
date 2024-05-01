package com.lianyi.paimonsnotebook.ui.screen.achievement.service

import androidx.sqlite.db.SupportSQLiteStatement
import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.ui.screen.achievement.data.UIAFJsonData
import kotlinx.coroutines.Dispatchers
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

    suspend fun tryGetUIAFJsonInfo(file: File): UIAFJsonData.Info? {
        val jsonReader = JsonReader(InputStreamReader(file.inputStream()))
        val uiafJsonData = tryGetUIAFJsonInfo(jsonReader)
        jsonReader.close()

        return uiafJsonData
    }

    /*
    * 尝试获取UIAFJson数据
    * */
    private suspend fun tryGetUIAFJsonInfo(
        jsonReader: JsonReader
    ): UIAFJsonData.Info? {
        var info: UIAFJsonData.Info? = null

        withContext(Dispatchers.IO) {
            jsonReader.apply {
                beginObject()

                val infoName = nextName()

                if (infoName != "info") {
                    error("导入的数据结构错误:not found info")
                }

                beginObject()
                val infoMap = mutableMapOf<String, String>()

                /*
                * 为map设置值,用于后续检查字段是否存在
                * */
                while (hasNext()) {
                    val key = when (nextName()) {
                        UIAFHelper.Field.Info.ExportTimestamp -> UIAFHelper.Field.Info.ExportTimestamp
                        UIAFHelper.Field.Info.ExportApp -> UIAFHelper.Field.Info.ExportApp
                        UIAFHelper.Field.Info.ExportAppVersion -> UIAFHelper.Field.Info.ExportAppVersion
                        UIAFHelper.Field.Info.UIAFVersion -> UIAFHelper.Field.Info.UIAFVersion
                        else -> ""
                    }

                    when (key) {
                        UIAFHelper.Field.Info.ExportTimestamp -> infoMap[key] =
                            nextLong().toString()

                        else -> infoMap[key] = nextString()
                    }
                }

                endObject()

                info = UIAFJsonData.Info(
                    export_app = infoMap[UIAFHelper.Field.Info.ExportApp] ?: "",
                    export_app_version = infoMap[UIAFHelper.Field.Info.ExportAppVersion] ?: "",
                    export_timestamp = infoMap[UIAFHelper.Field.Info.ExportTimestamp]?.toLongOrNull()
                        ?: (System.currentTimeMillis() / 1000),
                    uiaf_version = infoMap[UIAFHelper.Field.Info.UIAFVersion] ?: "",
                )
            }
        }

        return info
    }


    suspend fun importAchivementFromUIAFJson(
        userId: Int,
        UIAFJson: File
    ) {
        try {
            withContext(Dispatchers.IO) {
                JsonReader(InputStreamReader(UIAFJson.inputStream())).apply {
                    tryGetUIAFJsonInfo(this)

                    val listName = nextName()

                    if (listName != "list") {
                        error("导入的数据结构错误:not found list")
                    }

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
                    if(list.isNotEmpty()){
                        achievementItemListFlush(list)
                    }

                    list.clear()
                    close()
                }
            }
        } catch (e: Exception) {
            "发生了异常:${e.message}".errorNotify()
        }
    }


    suspend fun achievementItemListFlush(
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