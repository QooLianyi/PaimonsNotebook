package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note

data class DailyNoteData(
    val calendar_url: String,
    val current_expedition_num: Int,
    val current_home_coin: Int,
    val current_resin: Int,
    val expeditions: List<Expedition>,
    val finished_task_num: Int,
    val home_coin_recovery_time: String,
    val is_extra_task_reward_received: Boolean,
    val max_expedition_num: Int,
    val max_home_coin: Int,
    val max_resin: Int,
    val remain_resin_discount_num: Int,
    val resin_discount_num_limit: Int,
    val resin_recovery_time: String,
    val total_task_num: Int,
    val transformer: Transformer,
) {

    data class Expedition(
        val avatar_side_icon: String,
        val remained_time: String,
        val status: String,
    )

    data class Transformer(
        val latest_job_id: String,
        val noticed: Boolean,
        val obtained: Boolean,
        val recovery_time: RecoveryTime,
        val wiki: String,
    ) {
        data class RecoveryTime(
            val Day: Int,
            val Hour: Int,
            val Minute: Int,
            val Second: Int,
            val reached: Boolean,
        )
    }
}