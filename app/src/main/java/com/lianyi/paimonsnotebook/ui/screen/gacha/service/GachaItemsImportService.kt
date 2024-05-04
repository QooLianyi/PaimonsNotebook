package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import androidx.sqlite.db.SupportSQLiteStatement
import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.common.util.json.JsonReaderHelper
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.UIGFJsonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader

/*
* 祈愿记录导入
* database:数据库
* */
class GachaItemsImportService(
    val database: PaimonsNotebookDatabase = PaimonsNotebookDatabase.database,
) {

    private val dao = database.gachaItemsDao

    /*
    * 祈愿记录导入时缓存条数
    * 导入时使用
    *
    * SQLite解析器能够接收最多999个?(通配符)
    * 祈愿记录表有11个字段
    * 缓存条数最大值: 999 / 11 = 90.81
    * 超出此值将无法解析导致报错
    *
    * 最多创建的对象为 SemaphorePermits * DispatcherThreads * CacheGachaRecordSize =
    * */
    private val cacheGachaRecordSize = 90

    //stmt 缓存,一般情况下只会缓存最大值与最后一条不到最大值的记录
    private val stmtMap = mutableMapOf<Int, SupportSQLiteStatement>()

    //缓存上一次查询的info
    private var cacheUIGFInfo: UIGFJsonData.Info? = null

    //以Stream的方式读取UIGFJson
    suspend fun importGachaRecordFromUIGFJson(file: File, gachaLogService: GachaLogService) {
        try {
            withContext(Dispatchers.IO) {
                //如果之前没有调用获取tryGetUIGFJsonInfo方法,那这里就调用一次
                if (cacheUIGFInfo == null) {
                    tryGetUIGFJsonInfo(file)
                }

                //如果依然为空,直接返回,提示已经在获取方法中调用
                if (cacheUIGFInfo == null) {
                    return@withContext
                }

                val reader = JsonReader(InputStreamReader(file.inputStream()))

                var objectEnd = false

                JsonReaderHelper.getJsonReaderSingleFieldValue(
                    reader, "list",
                    onFound = {
                        launchIO {
                            saveUIGFList(cacheUIGFInfo!!, it, gachaLogService)

                            /*
                            * 等待reader读取完毕,否则close后还在访问reader会报错
                            * 一般来说用不到这个,加个判断防止意外
                            * */
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

    //保存至数据库
    suspend fun gachaItemListFlush(
        list: List<GachaItems>,
    ) {
        //当列表大小超过一次性的解析个数时拆分集合递归调用

        val items = if (list.size > cacheGachaRecordSize) {
            gachaItemListFlush(list.subList(cacheGachaRecordSize, list.size))
            list.subList(0, cacheGachaRecordSize - 1)
        } else {
            list
        }

        saveToDB(items)
    }

    //从JsonReader获取jsonInfo在外部调用
    private suspend fun tryGetUIGFJsonInfo(reader: JsonReader): UIGFJsonData.Info? {
        var info: UIGFJsonData.Info? = null

        try {
            withContext(Dispatchers.IO) {
                JsonReaderHelper.getJsonReaderSingleFieldValue(reader, "info",
                    onFound = {
                        info = getUIGFInfo(reader)
                        cacheUIGFInfo = info
                    },
                    onNotFound = {
                        error("没有找到info字段")
                    }
                )
            }
        } catch (e: Exception) {
            "发生了异常:${e.message}".errorNotify()
        }

        return info
    }

    //获取jsonInfo
    suspend fun tryGetUIGFJsonInfo(file: File): UIGFJsonData.Info? {
        val jsonReader = JsonReader(InputStreamReader(file.inputStream()))

        return tryGetUIGFJsonInfo(jsonReader)
    }

    private fun getUIGFInfo(reader: JsonReader) = reader.let {
        reader.beginObject()
        val infoMap = mutableMapOf<String, String>()

        while (reader.hasNext()) {
            val key = when (reader.nextName()) {
                UIGFHelper.Field.Info.Uid -> UIGFHelper.Field.Info.Uid
                UIGFHelper.Field.Info.Lang -> UIGFHelper.Field.Info.Lang
                UIGFHelper.Field.Info.ExportTimestamp -> UIGFHelper.Field.Info.ExportTimestamp
                UIGFHelper.Field.Info.ExportApp -> UIGFHelper.Field.Info.ExportApp
                UIGFHelper.Field.Info.ExportAppVersion -> UIGFHelper.Field.Info.ExportAppVersion
                UIGFHelper.Field.Info.UIGFVersion -> UIGFHelper.Field.Info.UIGFVersion
                UIGFHelper.Field.Info.RegionTimeZone -> UIGFHelper.Field.Info.RegionTimeZone
                else -> ""
            }

            when (key) {
                UIGFHelper.Field.Info.ExportTimestamp, UIGFHelper.Field.Info.RegionTimeZone -> infoMap[key] =
                    reader.nextLong().toString()

                else ->
                    infoMap[key] = reader.nextString()
            }
        }
        reader.endObject()

        //检查info是否包含所需的字段
        UIGFHelper.Field.Info.fields.forEach { infoField ->
            if (infoMap[infoField].isNullOrBlank()) {
                error("导入的数据结构错误:data.info中的[$infoField]字段未找到")
            }
        }

        val uid = infoMap[UIGFHelper.Field.Info.Uid]!!

        //此处转换时区,当时区不存在时,根据UID生成
        val regionTimeZone =
            infoMap[UIGFHelper.Field.Info.RegionTimeZone]?.toLongOrNull()
                ?: UIGFHelper.getRegionTimeZoneByUid(uid)

        UIGFJsonData.Info(
            uid = uid,
            lang = infoMap[UIGFHelper.Field.Info.Lang]!!,
            export_timestamp = infoMap[UIGFHelper.Field.Info.ExportTimestamp]
                ?.toLongOrNull() ?: 0L,
            export_app = infoMap[UIGFHelper.Field.Info.ExportApp]!!,
            export_app_version = infoMap[UIGFHelper.Field.Info.ExportAppVersion]!!,
            uigf_version = infoMap[UIGFHelper.Field.Info.UIGFVersion]!!,
            region_time_zone = regionTimeZone
        )
    }


    /*
    * 传入的json reader对象的当前指针必须指向list的value部分
    * */
    private suspend fun saveUIGFList(
        info: UIGFJsonData.Info,
        reader: JsonReader,
        gachaLogService: GachaLogService
    ) {
        reader.apply {
            val itemMap = mutableMapOf<String, String>()
            val list = mutableListOf<GachaItems>()

            var count = 0

            beginArray()

            while (hasNext()) {
                beginObject()

                //获取key
                while (hasNext()) {
                    val key = when (nextName()) {
                        UIGFHelper.Field.Item.UigfGachaType -> UIGFHelper.Field.Item.UigfGachaType
                        UIGFHelper.Field.Item.GachaType -> UIGFHelper.Field.Item.GachaType
                        UIGFHelper.Field.Item.ItemId -> UIGFHelper.Field.Item.ItemId
                        UIGFHelper.Field.Item.Count -> UIGFHelper.Field.Item.Count
                        UIGFHelper.Field.Item.Time -> UIGFHelper.Field.Item.Time
                        UIGFHelper.Field.Item.Name -> UIGFHelper.Field.Item.Name
                        UIGFHelper.Field.Item.ItemType -> UIGFHelper.Field.Item.ItemType
                        UIGFHelper.Field.Item.RankType -> UIGFHelper.Field.Item.RankType
                        UIGFHelper.Field.Item.Id -> UIGFHelper.Field.Item.Id
                        else -> ""
                    }
                    itemMap[key] = nextString()
                }

                endObject()

                //检查info是否包含所需的字段
                UIGFHelper.Field.Item.fields.forEach { itemField ->
                    when (itemField) {
                        /*
                        * id是可空的
                        * id为UIGF历史遗留问题,早期的祈愿记录没有存储id,因此需要支持UIGF格式的软件有生成ID的功能
                        * */
                        UIGFHelper.Field.Item.Id, UIGFHelper.Field.Item.ItemId -> {}
                        else -> {
                            if (itemMap[itemField].isNullOrBlank()) {
                                error("导入的数据结构错误:data.list.item $itemField field not found")
                            }
                        }
                    }
                }
                val name = itemMap[UIGFHelper.Field.Item.Name] ?: ""
                val itemType = itemMap[UIGFHelper.Field.Item.ItemType] ?: ""
                val itemId =
                    itemMap[UIGFHelper.Field.Item.ItemId].takeIf { !it.isNullOrBlank() }
                        ?: "${gachaLogService.getItemIdByName(name)}"

                if (itemId.isEmpty()) {
                    error("数据结构或本地元数据错误:data.list.item item_id field is empty")
                }

                list += GachaItems(
                    count = itemMap[UIGFHelper.Field.Item.Count]!!,
                    gacha_type = itemMap[UIGFHelper.Field.Item.GachaType]!!,
                    id = itemMap[UIGFHelper.Field.Item.Id] ?: "",
                    item_id = itemId,
                    item_type = itemType,
                    name = name,
                    rank_type = itemMap[UIGFHelper.Field.Item.RankType]!!,
                    time = itemMap[UIGFHelper.Field.Item.Time]!!,
                    uigf_gacha_type = itemMap[UIGFHelper.Field.Item.UigfGachaType]!!,
                    uid = info.uid,
                    lang = info.lang
                )

                count++

                if (count % cacheGachaRecordSize == 0) {
                    gachaItemListFlush(list)
                    list.clear()
                }
            }

            //最后不为空时才执行插入
            if (list.isNotEmpty()) {
                gachaItemListFlush(list)
            }

            //通知gacha_items表更新
            dao.notifyRoomGachaItemsUpdate()

            list.clear()
            itemMap.clear()

            endArray()

            //清除stmt缓存
            stmtMap.clear()
        }

    }

    //外部数据导入本地数据库
    private suspend fun saveToDB(list: List<GachaItems>) {
        val size = list.size

        val stmt = if (stmtMap[size] != null) {
            stmtMap[size]!!
        } else {
            //重复主键更新
            val sb =
                StringBuilder("INSERT OR REPLACE INTO gacha_items(count,gacha_type,id,item_id,item_type,lang,name,rank_type,time,uid,uigf_gacha_type) VALUES ")

            repeat(list.size) {
                when (it) {
                    0 -> {}
                    list.size -> {
                        sb.append(";")
                    }

                    else -> sb.append(",")
                }
                sb.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
            }

            database.compileStatement(sb.toString()).apply {
                stmtMap[size] = this
            }
        }

        //字段数量
        val tableColumnCount = 11
        list.forEachIndexed { index, uigfGachaItem ->
            val gachaItemId =
                uigfGachaItem.id.takeIf { it.isNotBlank() } ?: "${
                    generateGachaLogItemId(
                        uigfGachaItem.uid
                    )
                }"

            uigfGachaItem.apply {
                stmt.bindString(tableColumnCount * index + 1, count)
                stmt.bindString(tableColumnCount * index + 2, gacha_type)
                stmt.bindString(tableColumnCount * index + 3, gachaItemId)
                stmt.bindString(tableColumnCount * index + 4, item_id)
                stmt.bindString(tableColumnCount * index + 5, item_type)
                stmt.bindString(tableColumnCount * index + 6, lang)
                stmt.bindString(tableColumnCount * index + 7, name)
                stmt.bindString(tableColumnCount * index + 8, rank_type)
                stmt.bindString(tableColumnCount * index + 9, time)
                stmt.bindString(tableColumnCount * index + 10, uid)
                stmt.bindString(tableColumnCount * index + 11, uigf_gacha_type)
            }
        }
        stmt.executeInsert()
        stmt.clearBindings()
    }

    //生成ID
    private suspend fun generateGachaLogItemId(uid: String): Long {
        //获取从本地存储生成的祈愿记录id并+1
        var id = PaimonsNotebookApplication.context.datastorePf.data.map {
            (it[PreferenceKeys.GenerateGachaRecordId] ?: 1000000000000000000L) + 1L
        }.first()

        //1300000000000000000往后的值已经被占用了
        while (id < 1300000000000000000L) {
            if (!dao.isExist("$id", uid)) {
                break
            }
            id++
        }

        //同步到本地datastore
        PreferenceKeys.GenerateGachaRecordId.editValue(id)
        return id
    }
}