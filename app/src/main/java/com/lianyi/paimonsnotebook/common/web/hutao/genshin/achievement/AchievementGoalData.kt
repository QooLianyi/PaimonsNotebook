package com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AchievementIconConverter

data class AchievementGoalData(
    @SerializedName("")
    val finishReward: FinishReward,
    @SerializedName("Icon")
    val icon: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Order")
    val order: Int
) {
    data class FinishReward(
        val Count: Int,
        val Id: Int
    )

    val iconUrl: String
        get() = AchievementIconConverter.iconNameToUrl(icon)

}