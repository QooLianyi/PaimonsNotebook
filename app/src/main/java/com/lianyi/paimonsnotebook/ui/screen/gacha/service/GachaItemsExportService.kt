package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.google.gson.stream.JsonWriter
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.UIGFHelper
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
    private val GachaRecordPageSize = 2000

    private val dao by lazy {
        database.gachaItemsDao
    }

    //导出当前用户的祈愿记录
    suspend fun exportGachaRecordToUIGFJson(gameUid: String,saveFile: File) {
        if (gameUid.isBlank()) {
            "导出失败:当前还未选择当前记录的账号".warnNotify()
            return
        }

        val gachaLogItemList = dao.getGachaLogItemByUid(gameUid)

        if (gachaLogItemList.isEmpty()) {
            "导出失败:当前记录账号没有祈愿记录".warnNotify()
            return
        }

        val item = gachaLogItemList.first()

        withContext(Dispatchers.IO) {
            exportUIGFJsonFile(saveFile,item)
        }
    }

    //以Stream的方式保存UIGFJson
    private suspend fun exportUIGFJsonFile(saveFile: File, item: GachaItems) {
        return withContext(Dispatchers.IO) {

            val writer = JsonWriter(FileWriter(saveFile)).apply {
                setIndent("  ")
            }
            writer.apply {
                beginObject()
                name("info")
                beginObject()
                name(UIGFHelper.Field.Info.Uid).value(item.uid)
                name(UIGFHelper.Field.Info.Lang).value(item.lang)
                name(UIGFHelper.Field.Info.ExportTimestamp).value(System.currentTimeMillis())
                name(UIGFHelper.Field.Info.ExportApp).value(PaimonsNotebookApplication.name)
                name(UIGFHelper.Field.Info.ExportAppVersion).value(PaimonsNotebookApplication.version)
                name(UIGFHelper.Field.Info.UIGFVersion).value(UIGFHelper.UIGF_VERSION)
                endObject()

                name("list")
                beginArray()

                var list: List<GachaItems>
                var page = 0

                do {
                    list = dao.getGachaLogItemByUidPage(item.uid, page, GachaRecordPageSize)
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
                } while (list.size >= GachaRecordPageSize)

                endArray()
                endObject()

                flush()
                close()
            }
        }
    }

}