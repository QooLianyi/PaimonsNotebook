package com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper

data class AchievementData(
    @SerializedName("Description")
    val description: String,
    @SerializedName("FinishReward")
    val finishReward: FinishReward,
    @SerializedName("Goal")
    val goal: Int,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("IsDeleteWatcherAfterFinish")
    val isDeleteWatcherAfterFinish: Boolean,
    @SerializedName("Order")
    val order: Int,
    @SerializedName("Progress")
    val progress: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Version")
    val version: String,
    @SerializedName("Icon")
    val icon: String,
    @SerializedName("PreviousId")
    val previousId: Int,
//    @SerializedName("IsDailyQuest")
//    val isDailyQuest: Boolean
) {
    data class FinishReward(
        val Count: Int,
        val Id: Int
    )

    /*
    * 转换为数据库实体
    * */
    fun toDatabaseEntity(
        userId: Int,
        current: Int = UIAFHelper.VALID_CURRENT,
        status: Int = UIAFHelper.AchievementStatus.STATUS_REWARD_TAKEN
    ): Achievements {
        return Achievements(
            id = id,
            current = current,
            status = status,
            timestamp = System.currentTimeMillis() / 1000,
            userId = userId
        )
    }

}