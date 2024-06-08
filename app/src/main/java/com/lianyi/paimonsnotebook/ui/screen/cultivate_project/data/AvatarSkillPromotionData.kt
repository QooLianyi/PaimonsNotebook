package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

data class AvatarSkillPromotionData(
    val id: Int,
    val name: String,
    val icon: String
) {
    var fromLevel: Int by mutableIntStateOf(1)
    var toLevel: Int by mutableIntStateOf(10)
}
