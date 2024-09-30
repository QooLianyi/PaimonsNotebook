package com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.service

import com.google.gson.stream.JsonReader
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementGoalData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import java.io.File
import java.io.InputStreamReader

class AchievementService(onMissingFile: () -> Unit) {
    var achievementList = listOf<AchievementData>()
        private set

    var achievementGoalList = listOf<AchievementGoalData>()

    init {
        val achievementFile = FileHelper.getMetadata(MetadataHelper.FileNameAchievement)
        val achievementGoalFile = FileHelper.getMetadata(MetadataHelper.FileNameAchievementGoal)

        if (achievementFile != null && achievementGoalFile != null) {
            setAchievementList(achievementFile)
            setAchievementGoalList(achievementGoalFile)
        } else {
            onMissingFile.invoke()
        }
    }

    private fun setAchievementList(achi: File) {
        JsonReader(InputStreamReader(achi.inputStream())).use {
            it.beginArray()

            while (it.hasNext()) {

                var id = -1
                var goal = -1
                var order = -1
                var process = -1
                var previousId = 0
                var isDeleteWatcher = false
                var isDailyQuest = false
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

                            UIAFHelper.Field.FinishReward.FinishReward -> {
                                beginObject()

                                var rewardId = -1
                                var count = -1
                                while (hasNext()) {
                                    when (nextName()) {
                                        UIAFHelper.Field.FinishReward.Id -> rewardId =
                                            nextInt()

                                        UIAFHelper.Field.FinishReward.Count -> count =
                                            nextInt()
                                    }
                                }
                                endObject()

                                reward = AchievementData.FinishReward(count, rewardId)

                                continue
                            }

                            UIAFHelper.Field.Data.IsDeleteWatcherAfterFinish -> isDeleteWatcher =
                                nextBoolean()

                            UIAFHelper.Field.Data.IsDailyQuest -> isDailyQuest =
                                nextBoolean()

                            UIAFHelper.Field.Data.Progress -> process = nextInt()
                            UIAFHelper.Field.Data.Version -> version = nextString()
                            UIAFHelper.Field.Data.Icon -> icon = nextString()
                            UIAFHelper.Field.Data.PreviousId -> previousId = nextInt()
                        }
                    }

                    endObject()
                }

                achievementList += AchievementData(
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

                //重置标识
                isDailyQuest = false
            }

            it.endArray()
        }

    }

    private fun setAchievementGoalList(goalFile: File) {
        JsonReader(InputStreamReader(goalFile.inputStream())).use {
            it.beginArray()

            while (it.hasNext()) {

                var id = -1
                var order = -1
                var name = ""
                var reward = AchievementGoalData.FinishReward(0, 0)
                var icon = ""

                it.apply {
                    beginObject()

                    while (hasNext()) {
                        when (nextName()) {
                            UIAFHelper.Field.Goal.Name -> name = nextString()
                            UIAFHelper.Field.Goal.Order -> order = nextInt()
                            UIAFHelper.Field.Goal.Id -> id = nextInt()
                            UIAFHelper.Field.Goal.Icon -> icon = nextString()

                            UIAFHelper.Field.FinishReward.FinishReward -> {
                                beginObject()

                                var rewardId = -1
                                var count = -1
                                while (hasNext()) {
                                    when (nextName()) {
                                        UIAFHelper.Field.FinishReward.Id -> rewardId =
                                            nextInt()

                                        UIAFHelper.Field.FinishReward.Count -> count =
                                            nextInt()
                                    }
                                }
                                endObject()

                                reward = AchievementGoalData.FinishReward(count, rewardId)

                                continue
                            }
                        }
                    }
                    endObject()
                }

                achievementGoalList += AchievementGoalData(
                    reward,
                    icon, id, name, order
                )
            }

            it.endArray()
        }

    }
}