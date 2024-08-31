package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note

data class DailyNoteData(
    val calendar_url: String,
    val current_expedition_num: Int,
    val current_home_coin: Int,
    val current_resin: Int,
    val daily_task: DailyTask,
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
    val transformer: Transformer
) {

    data class DailyTask(
        val attendance_rewards: List<AttendanceReward>,
        val attendance_visible: Boolean,
        val finished_num: Int,
        val is_extra_task_reward_received: Boolean,
        val task_rewards: List<TaskReward>,
        val total_num: Int
    )

    data class Expedition(
        val avatar_side_icon: String,
        val remained_time: String,
        val status: String
    )

    data class Transformer(
        val latest_job_id: String,
        val noticed: Boolean,
        val obtained: Boolean,
        val recovery_time: RecoveryTime,
        val wiki: String
    ) {
        fun getRecoveryTimeText() =
            if (recovery_time.reached) {
                "准备完成"
            } else {
                with(StringBuilder()) {
                    val day = recovery_time.Day
                    val hour = recovery_time.Hour
                    val minute = recovery_time.Minute
                    val second = recovery_time.Second

                    if (day != 0) {
                        append("${day}天")
                    }
                    if (hour != 0) {
                        append("${hour}小时")
                    }
                    if (minute != 0) {
                        append("${minute}分钟")
                    }
                    if (second != 0) {
                        append("${second}秒")
                    }
                    toString()
                }
            }
    }

    data class AttendanceReward(
        val progress: Int,
        val status: String
    )

    data class TaskReward(
        val status: String
    )

    data class RecoveryTime(
        val Day: Int,
        val Hour: Int,
        val Minute: Int,
        val Second: Int,
        val reached: Boolean
    )
}