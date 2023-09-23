package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note

data class DailyNoteWidgetData(
    val current_expedition_num: Int,
    val current_home_coin: Int,
    val current_resin: Int,
    val expeditions: List<Expedition>,
    val finished_task_num: Int,
    val has_signed: Boolean,
    val home_url: String,
    val is_extra_task_reward_received: Boolean,
    val max_expedition_num: Int,
    val max_home_coin: Int,
    val max_resin: Int,
    val note_url: String,
    val resin_recovery_time: String,
    val sign_url: String,
    val total_task_num: Int
){
    data class Expedition(
        val avatar_side_icon: String,
        val status: String
    )
}

