package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import androidx.sqlite.db.SupportSQLiteStatement
import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.file.createJsonReader
import com.lianyi.paimonsnotebook.common.extension.json.findField
import com.lianyi.paimonsnotebook.common.util.data_store.DataStoreHelper
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.UIGFInfoCompat
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.UIGFJsonData
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.UIGFJsonV4Data
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    //获取uigf version
    fun getUIGFVersion(file: File): UIGFInfoCompat? {
        val jsonReader = JsonReader(InputStreamReader(file.inputStream()))

        val hasInfoField = jsonReader.findField("info")

        if (!hasInfoField) {
            return null
        }

        var uigfVersion = ""
        var version = ""
        var regionTimeZone = 0L


        jsonReader.apply {
            beginObject()

            while (hasNext()) {
                when (nextName()) {
                    UIGFHelper.Field.Info.UIGFVersion -> uigfVersion = nextString()
                    UIGFHelper.Field.Info.Version -> version = nextString()
                    UIGFHelper.Field.Info.RegionTimeZone -> regionTimeZone =
                        nextString().toLongOrNull() ?: 0L

                    else -> {
                        skipValue()
                    }
                }
            }

            endObject()
        }

        jsonReader.close()

        return UIGFInfoCompat(
            uigfVersion = uigfVersion,
            version = version,
            regionTimeZone = regionTimeZone
        )
    }

    fun getUIGFJsonPropertyListCompat(file: File): List<Pair<String, String>> {
        val uigfInfoCompat =
            getUIGFVersion(file) ?: error("无法读取该json中的info对象")

        //正常情况下不会同时触发v3与v4的判断
        //v3
        if (!uigfInfoCompat.uigfVersion.isNullOrBlank()) {
            return getUIGFInfoV3(file).getPropertyList()
        }

        //v4
        if (!uigfInfoCompat.version.isNullOrBlank()) {
            return getUIGFInfoV4(file).getPropertyList()
        }

        return emptyList()
    }

    //保存uigf items,兼容性
    suspend fun saveUIGFJsonItemsCompat(
        file: File,
        gachaLogService: GachaLogService
    ) {
        val uigfInfoCompat =
            getUIGFVersion(file) ?: error("数据结构错误:无法读取该json中的[info]字段")

        //正常情况下不会同时触发两个判断
        //v3
        if (!uigfInfoCompat.uigfVersion.isNullOrBlank()) {
            saveUIGFV3Items(file, gachaLogService)
        }

        //v4
        if (!uigfInfoCompat.version.isNullOrBlank()) {
            saveHK4EItems(file, gachaLogService)
        }
    }

    private suspend fun saveUIGFV3Items(
        file: File,
        gachaLogService: GachaLogService
    ) {
        val info = getUIGFInfoV3(file)
        val jsonReader = file.createJsonReader()

        val hasListField = jsonReader.findField("list")

        if (!hasListField) {
            error("数据结构错误:{data}中的[list]字段未找到")
        }

        saveUIGFGachaItems(
            uid = info.uid,
            lang = info.lang,
            reader = jsonReader,
            gachaLogService = gachaLogService
        )

        jsonReader.close()

        DataStoreHelper.applyLocalDataMap(PreferenceKeys.GachaRecordGameUidRegionMap) {
            this[info.uid] = info.region_time_zone
        }
    }

    //保存hk4e的记录
    private suspend fun saveHK4EItems(
        file: File,
        gachaLogService: GachaLogService
    ) {
        val jsonReader = file.createJsonReader()

        val hasHK4E = jsonReader.findField("hk4e")
        if (!hasHK4E) {
            error("数据结构错误:{data}中的[hk4e]字段未找到")
        }

        jsonReader.apply {
            beginArray()

            while (hasNext()) {
                beginObject()

                var uid = ""
                var lang = ""
                var timezone = -1L

                while (hasNext()) {
                    when (nextName()) {
                        UIGFHelper.Field.Info.Uid -> uid = nextString()
                        UIGFHelper.Field.Info.Lang -> lang = nextString()
                        UIGFHelper.Field.Info.TimeZone -> timezone = nextLong()
                        "list" -> {
                            if (uid.isBlank()) {
                                error("字段缺失错误:{data}.items.list.item中的[${UIGFHelper.Field.Info.Uid}]字段未找到,或json顺序错误")
                            }

                            if (timezone == -1L) {
                                error("字段缺失错误:{data}.items.list.item中的[${UIGFHelper.Field.Info.TimeZone}]字段未找到,或json顺序错误")
                            }

                            saveUIGFGachaItems(
                                uid = uid,
                                lang = lang,
                                reader = jsonReader,
                                gachaLogService = gachaLogService
                            )

                            //更新时区至本地
                            DataStoreHelper.applyLocalDataMap(PreferenceKeys.GachaRecordGameUidRegionMap) {
                                this[uid] = timezone
                            }

                            uid = ""
                            lang = ""
                            timezone = -1L
                        }

                        else -> {
                            skipValue()
                        }
                    }
                }

                endObject()
            }

            endArray()

            close()
        }
    }

    //获取json info对象,指针需指向info的value部分
    fun getUIGFInfoV3(file: File): UIGFJsonData.Info {
        val jsonReader = file.createJsonReader()

        val hasInfoField = jsonReader.findField("info")

        if (!hasInfoField) {
            error("数据结构错误:无法读取该json中的[info]字段")
        }

        val infoMap = mutableMapOf<String, String>()

        jsonReader.apply {
            beginObject()

            while (hasNext()) {
                infoMap[nextName()] = nextString()
            }

            endObject()
            close()
        }

        //检查info是否包含所需的字段
        UIGFHelper.Field.Info.requiredFields.forEach { infoField ->
            if (infoMap[infoField].isNullOrBlank()) {
                error("字段缺失错误:data.info中的[$infoField]字段未找到")
            }
        }

        val uid = infoMap[UIGFHelper.Field.Info.Uid]!!

        //此处转换时区,当时区不存在时,根据UID生成
        val regionTimeZone =
            infoMap[UIGFHelper.Field.Info.RegionTimeZone]?.toLongOrNull()
                ?: UIGFHelper.getRegionTimeZoneByUid(uid)


        return UIGFJsonData.Info(
            uid = uid,
            lang = infoMap[UIGFHelper.Field.Info.Lang] ?: "",
            export_timestamp = infoMap[UIGFHelper.Field.Info.ExportTimestamp]
                ?.toLongOrNull() ?: 0L,
            export_time = infoMap[UIGFHelper.Field.Info.ExportTime] ?: "",
            export_app = infoMap[UIGFHelper.Field.Info.ExportApp] ?: "",
            export_app_version = infoMap[UIGFHelper.Field.Info.ExportAppVersion] ?: "",
            uigf_version = infoMap[UIGFHelper.Field.Info.UIGFVersion]!!,
            region_time_zone = regionTimeZone
        )
    }

    //通过不同的游戏id获取记录中的uid
    private fun getUidListByField(
        jsonReader: JsonReader,
        gameId: String = "hk4e"
    ): List<String> {
        val uidList = mutableListOf<String>()

        val hasGameIdItems = jsonReader.findField(gameId)

        if (!hasGameIdItems) {
            return emptyList()
        }

        jsonReader.apply {
            beginArray()

            while (hasNext()) {
                beginObject()

                while (hasNext()) {

                    if (nextName() == "uid") {
                        uidList += nextString()
                    } else {
                        skipValue()
                    }

                }
                endObject()
            }
            endArray()

            close()
        }

        return uidList
    }

    fun getUIGFInfoV4(file: File): UIGFJsonV4Data.Info {
        val jsonReader = JsonReader(InputStreamReader(file.inputStream()))

        val hasInfoFiled = jsonReader.findField("info")

        if (!hasInfoFiled) {
            error("数据结构错误:无法读取该json中的[info]字段")
        }

        val infoMap = mutableMapOf<String, String>()

        jsonReader.apply {
            beginObject()

            while (hasNext()) {
                infoMap[nextName()] = nextString()
            }

            endObject()
            close()
        }

        UIGFHelper.Field.Info.requiredFieldsV4.forEach { infoField ->
            if (infoMap[infoField].isNullOrBlank()) {
                error("字段缺失错误:{data}.info中的[$infoField]字段未找到")
            }
        }

        return UIGFJsonV4Data.Info(
            exportTimestamp = infoMap[UIGFHelper.Field.Info.ExportTimestamp] ?: "",
            exportApp = infoMap[UIGFHelper.Field.Info.ExportApp] ?: "",
            exportAppVersion = infoMap[UIGFHelper.Field.Info.ExportAppVersion] ?: "",
            version = infoMap[UIGFHelper.Field.Info.Version] ?: ""
        ).apply {
            uidList += getUidListByField(JsonReader(InputStreamReader(file.inputStream())))
        }
    }


    private suspend fun saveUIGFGachaItems(
        uid: String,
        lang: String,
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

                while (hasNext()) {
                    itemMap[nextName()] = nextString()
                }

                endObject()

                //检查info是否包含所需的字段
                UIGFHelper.Field.Item.requiredFields.forEach { itemField ->
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

                val itemId = itemMap[UIGFHelper.Field.Item.ItemId] ?: ""
                val name = itemMap[UIGFHelper.Field.Item.Name] ?: ""

                var model = gachaLogService.getGachaModelByItemId(itemId = itemId)
                if (model == null) {
                    model = gachaLogService.getModelByName(name = name)
                }

                if (model == null) {
                    error("没有找到id为[${itemId}],name为[${name}]的实体,请检查json文件或尝试更新元数据后再次尝试")
                }

                val itemType = itemMap[UIGFHelper.Field.Item.ItemType] ?: model.type

                list += GachaItems(
                    count = itemMap[UIGFHelper.Field.Item.Count] ?: "1",
                    gacha_type = itemMap[UIGFHelper.Field.Item.GachaType]!!,
                    id = itemMap[UIGFHelper.Field.Item.Id] ?: "",
                    item_id = "${model.id}",
                    item_type = itemType,
                    name = model.name,
                    rank_type = itemMap[UIGFHelper.Field.Item.RankType] ?: "${model.rank}",
                    time = itemMap[UIGFHelper.Field.Item.Time]!!,
                    uigf_gacha_type = itemMap[UIGFHelper.Field.Item.UigfGachaType]!!,
                    uid = uid,
                    lang = lang
                )

                itemMap.clear()

                count++

                if (count % cacheGachaRecordSize == 0) {
                    gachaItemListFlushToDB(list)
                    list.clear()
                }
            }

            //最后不为空时才执行插入
            if (list.isNotEmpty()) {
                gachaItemListFlushToDB(list)
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

    //保存至数据库
    suspend fun gachaItemListFlushToDB(
        list: List<GachaItems>,
    ) {
        //当列表大小超过一次性的解析个数时拆分集合递归调用

        val items = if (list.size > cacheGachaRecordSize) {
            gachaItemListFlushToDB(list.subList(cacheGachaRecordSize, list.size))
            list.subList(0, cacheGachaRecordSize - 1)
        } else {
            list
        }

        saveToDB(items)
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