package com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement

import com.google.gson.annotations.SerializedName

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
    val icon:String,
    @SerializedName("PreviousId")
    val previousId:Int
) {
    data class FinishReward(
        val Count: Int,
        val Id: Int
    )
}