package com.lianyi.paimonsnotebook.ui.screen.achievement.service

import com.google.gson.stream.JsonWriter
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.achievement.entity.AchievementUser
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.ui.screen.achievement.data.UIAFJsonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

class AchievementExportService {

    private val achievementsDao = PaimonsNotebookDatabase.database.achievementsDao

    private val queryPageSize = 2000

    suspend fun exportAchievementToUIAF(file: File, user: AchievementUser) {
        val info = UIAFJsonData.Info(
            export_timestamp = System.currentTimeMillis() / 1000,
            export_app_version = PaimonsNotebookApplication.version,
            export_app = PaimonsNotebookApplication.name,
            uiaf_version = UIAFHelper.UIAF_VERSION
        )

        writeAchievementsToFile(file, info, user)
    }

    private suspend fun writeAchievementsToFile(
        saveFile: File,
        info: UIAFJsonData.Info,
        user: AchievementUser
    ) {
        withContext(Dispatchers.IO) {

            val writer = JsonWriter(FileWriter(saveFile)).apply {
                setIndent("  ")
            }

            writer.apply {
                beginObject()
                name("info")
                beginObject()
                name(UIAFHelper.Field.Info.ExportApp).value(info.export_app)
                name(UIAFHelper.Field.Info.ExportAppVersion).value(info.export_app_version)
                name(UIAFHelper.Field.Info.UIAFVersion).value(info.uiaf_version)
                name(UIAFHelper.Field.Info.ExportTimestamp).value(info.export_timestamp)
                endObject()

                name("list")
                beginArray()

                var list: List<Achievements>
                var page = 0

                do {
                    list = achievementsDao.getAchievementsByUserIdPage(user.id, page, queryPageSize)
                    list.forEach { achievements ->
                        beginObject()
                        name(UIAFHelper.Field.Item.Id).value(achievements.id)
                        name(UIAFHelper.Field.Item.Status).value(achievements.status)
                        name(UIAFHelper.Field.Item.Current).value(achievements.current)
                        name(UIAFHelper.Field.Item.Timestamp).value(achievements.timestamp)
                        endObject()
                    }
                    page++
                } while (list.size >= queryPageSize)

                endArray()
                endObject()

                flush()
                close()
            }
        }
    }

}