package com.lianyi.paimonsnotebook.ui.screen.achievement.data

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementGoalData

data class AchievementGoalOverviewData(
    val goal: AchievementGoalData,
    val userId: Int,
    val finishCount: Int,
    val total: Int
)
