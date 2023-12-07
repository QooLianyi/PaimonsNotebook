package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementGoalData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import java.io.File
import java.io.InputStreamReader

class AchievementService(private val onMissingFile: () -> Unit) {

    //成就列表
    var achievementList = listOf<AchievementData>()
        private set

    //成就分类
    var achievementGoalList = listOf<AchievementGoalData>()
        private set

    init {
        val achievementFile = FileHelper.getMetadata(MetadataHelper.FileNameAchievement)
        val achievementGoalFile = FileHelper.getMetadata(MetadataHelper.FileNameAchievementGoal)

        if (achievementFile != null && achievementGoalFile != null) {
            setAchievementList(achievementFile)

        }else{
            onMissingFile.invoke()
        }
    }


    //以流的方式处理列表,避免OOM
    private fun setAchievementList(achievementFile:File){
        val list = mutableListOf<AchievementData>()

        try {
            JsonReader(InputStreamReader(achievementFile.inputStream())).use {
                it.beginArray()

                while (it.hasNext()) {

                    var id = -1
                    var goal = -1
                    var order = -1
                    var process = -1
                    var previousId = 0
                    var isDeleteWatcher = false
                    var title = ""
                    var description = ""
                    var version = ""
                    var reward = AchievementData.FinishReward(0, 0)
                    var icon = ""

                    it.apply {
                        beginObject()

                        while (hasNext()) {
                            when (nextName()) {
                                UIAFHelper.Field.Data.Id -> id = nextInt()
                                UIAFHelper.Field.Data.Goal -> goal = nextInt()
                                UIAFHelper.Field.Data.Order -> order = nextInt()
                                UIAFHelper.Field.Data.Title -> title = nextString()
                                UIAFHelper.Field.Data.Description -> description =
                                    nextString()

                                UIAFHelper.Field.Data.FinishReward -> {
                                    beginObject()

                                    var rewardId = -1
                                    var count = -1
                                    while (hasNext()) {
                                        when (nextName()) {
                                            UIAFHelper.Field.Data.Reward.Id -> rewardId =
                                                nextInt()

                                            UIAFHelper.Field.Data.Reward.Count -> count =
                                                nextInt()
                                        }
                                    }
                                    endObject()

                                    reward = AchievementData.FinishReward(count, rewardId)

                                    continue
                                }

                                UIAFHelper.Field.Data.IsDeleteWatcherAfterFinish -> isDeleteWatcher =
                                    nextBoolean()

                                UIAFHelper.Field.Data.Progress -> process = nextInt()
                                UIAFHelper.Field.Data.Version -> version = nextString()
                                UIAFHelper.Field.Data.Icon -> icon = nextString()
                                UIAFHelper.Field.Data.PreviousId -> previousId = nextInt()
                            }
                        }

                        endObject()
                    }

                    list += AchievementData(
                        description,
                        reward,
                        goal,
                        id,
                        isDeleteWatcher,
                        order,
                        process,
                        title,
                        version,
                        icon,
                        previousId
                    )
                }

                it.endArray()
            }

            achievementList = list
        }catch (_:Exception){
            onMissingFile.invoke()
        }
    }


    private fun setAchievementGoalList(achievementGoalFile:File){
        val list = mutableListOf<AchievementGoalData>()

        try {
            JsonReader(InputStreamReader(achievementGoalFile.inputStream())).use {
                it.beginArray()

                while (it.hasNext()) {

                    var id = -1
                    var goal = -1
                    var order = -1
                    var process = -1
                    var previousId = 0
                    var isDeleteWatcher = false
                    var title = ""
                    var description = ""
                    var version = ""
                    var reward = AchievementData.FinishReward(0, 0)
                    var icon = ""

                    it.apply {
                        beginObject()

                        while (hasNext()) {
                            when (nextName()) {
                                UIAFHelper.Field.Data.Id -> id = nextInt()
                                UIAFHelper.Field.Data.Goal -> goal = nextInt()
                                UIAFHelper.Field.Data.Order -> order = nextInt()
                                UIAFHelper.Field.Data.Title -> title = nextString()
                                UIAFHelper.Field.Data.Description -> description =
                                    nextString()

                                UIAFHelper.Field.Data.FinishReward -> {
                                    beginObject()

                                    var rewardId = -1
                                    var count = -1
                                    while (hasNext()) {
                                        when (nextName()) {
                                            UIAFHelper.Field.Data.Reward.Id -> rewardId =
                                                nextInt()

                                            UIAFHelper.Field.Data.Reward.Count -> count =
                                                nextInt()
                                        }
                                    }
                                    endObject()

                                    reward = AchievementData.FinishReward(count, rewardId)

                                    continue
                                }

                                UIAFHelper.Field.Data.IsDeleteWatcherAfterFinish -> isDeleteWatcher =
                                    nextBoolean()

                                UIAFHelper.Field.Data.Progress -> process = nextInt()
                                UIAFHelper.Field.Data.Version -> version = nextString()
                                UIAFHelper.Field.Data.Icon -> icon = nextString()
                                UIAFHelper.Field.Data.PreviousId -> previousId = nextInt()
                            }
                        }

                        endObject()
                    }

                }

                it.endArray()
            }

        }catch (_:Exception){
            onMissingFile.invoke()
        }
    }

}