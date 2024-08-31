package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.google.gson.stream.JsonWriter
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.DataStoreHelper
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

/*
* 祈愿导出
* currentGameUid:目标uid
* saveFile:保存的文件
* database:数据库
* */
class GachaItemsExportService(
    val database: PaimonsNotebookDatabase = PaimonsNotebookDatabase.database,
) {

    /*
    * 祈愿记录一页条数
    * */
    private val queryPageSize = 2000

    private val dao by lazy {
        database.gachaItemsDao
    }

    //导出当前用户的祈愿记录
    suspend fun exportGachaRecordToUIGFJson(
        uidList: List<String>,
        saveFile: File,
        exportUIGFV3: Boolean
    ) {
        if (uidList.isEmpty()) {
            "导出失败:请先选中一个祈愿记录的id".warnNotify()
            return
        }

        //v3只能导出一个uid
        if (exportUIGFV3) {
            val uid = uidList.first()

            val gachaLogItemList = dao.getGachaLogItemByUidPage(uid, 0, 1)
            val lang = gachaLogItemList.first().lang

            val region =
                DataStoreHelper.getLocalDataMap<String, Long>(PreferenceKeys.GachaRecordGameUidRegionMap)[uid]
                    ?: UIGFHelper.getRegionTimeZoneByUid(uid)

            exportUIGFJsonV3File(
                saveFile = saveFile,
                uid = uid,
                lang = lang,
                regionTimeZone = region
            )
        } else {

            exportUIGFJsonV4File(
                saveFile = saveFile,
                uidList = uidList
            )
        }
    }

    //v3
    private suspend fun exportUIGFJsonV3File(
        saveFile: File,
        uid: String,
        lang: String,
        regionTimeZone: Long
    ) {
        withContext(Dispatchers.IO) {
            val writer = JsonWriter(FileWriter(saveFile)).apply {
                setIndent("  ")
            }

            writer.apply {
                beginObject()
                name("info")
                beginObject()
                name(UIGFHelper.Field.Info.Uid).value(uid)
                name(UIGFHelper.Field.Info.Lang).value(lang)
                name(UIGFHelper.Field.Info.ExportTimestamp).value(System.currentTimeMillis())
                name(UIGFHelper.Field.Info.ExportApp).value(PaimonsNotebookApplication.name)
                name(UIGFHelper.Field.Info.ExportAppVersion).value(PaimonsNotebookApplication.version)
                name(UIGFHelper.Field.Info.UIGFVersion).value("v3.0")
                name(UIGFHelper.Field.Info.RegionTimeZone).value(regionTimeZone)
                endObject()

                name("list")
                saveGachaItems(writer = writer, uid = uid)

                endObject()

                flush()
                close()
            }
        }
    }

    //v4
    private suspend fun exportUIGFJsonV4File(
        saveFile: File,
        uidList: List<String>,
    ) {
        withContext(Dispatchers.IO) {
            val writer = JsonWriter(FileWriter(saveFile)).apply {
                setIndent("  ")
            }

            writer.apply {
                beginObject()
                name("info")
                beginObject()
                name(UIGFHelper.Field.Info.ExportTimestamp).value(System.currentTimeMillis() / 1000)
                name(UIGFHelper.Field.Info.ExportApp).value(PaimonsNotebookApplication.name)
                name(UIGFHelper.Field.Info.ExportAppVersion).value(PaimonsNotebookApplication.version)
                name(UIGFHelper.Field.Info.Version).value(UIGFHelper.UIGF_VERSION)
                endObject()

                name("hk4e")
                beginArray()

                val timeZoneIdCacheMap =
                    DataStoreHelper.getLocalDataMap<String, Long>(PreferenceKeys.GachaRecordGameUidRegionMap)

                uidList.forEach { uid ->
                    val timeZone = timeZoneIdCacheMap[uid] ?: UIGFHelper.getRegionTimeZoneByUid(uid)

                    val gachaLogItemList = dao.getGachaLogItemByUidPage(uid, 0, 1)

                    //如果为空,就设置为zh-cn
                    val lang =
                        if (gachaLogItemList.isNotEmpty()) gachaLogItemList.first().lang else "zh-cn"

                    beginObject()
                    name(UIGFHelper.Field.Info.Uid).value(uid)
                    name(UIGFHelper.Field.Info.TimeZone).value(timeZone)
                    name(UIGFHelper.Field.Info.Lang).value(lang)

                    name("list")
                    saveGachaItems(writer = writer, uid = uid)
                    endObject()
                }

                endArray()
                endObject()

                flush()
                close()
            }
        }
    }

    private fun saveGachaItems(
        writer: JsonWriter,
        uid: String
    ) {
        writer.apply {
            beginArray()

            var list: List<GachaItems>
            var page = 0

            do {
                list = dao.getGachaLogItemByUidPage(uid, page, queryPageSize)
                list.forEach { uigfGachaItem ->
                    beginObject()
                    name(UIGFHelper.Field.Item.UigfGachaType).value(uigfGachaItem.uigf_gacha_type)
                    name(UIGFHelper.Field.Item.GachaType).value(uigfGachaItem.gacha_type)
                    name(UIGFHelper.Field.Item.ItemId).value(uigfGachaItem.item_id)
                    name(UIGFHelper.Field.Item.Count).value(uigfGachaItem.count)
                    name(UIGFHelper.Field.Item.Time).value(uigfGachaItem.time)
                    name(UIGFHelper.Field.Item.Name).value(uigfGachaItem.name)
                    name(UIGFHelper.Field.Item.ItemType).value(uigfGachaItem.item_type)
                    name(UIGFHelper.Field.Item.RankType).value(uigfGachaItem.rank_type)
                    name(UIGFHelper.Field.Item.Id).value(uigfGachaItem.id)
                    endObject()
                }
                page++
            } while (list.size >= queryPageSize)

            endArray()
        }
    }

}