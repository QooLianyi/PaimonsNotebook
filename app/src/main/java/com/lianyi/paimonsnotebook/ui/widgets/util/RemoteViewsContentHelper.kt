package com.lianyi.paimonsnotebook.ui.widgets.util

import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData


//远端视图显示内容
object RemoteViewsContentHelper {
    fun getDailyTaskContentByState(dailyNoteData: DailyNoteData): String =
        getDailyTaskContentByState(
            dailyNoteData.daily_task.finished_num,
            dailyNoteData.daily_task.total_num,
            dailyNoteData.daily_task.is_extra_task_reward_received
        )

    fun getDailyTaskContentByState(dailyNoteData: DailyNoteWidgetData): String =
        getDailyTaskContentByState(
            dailyNoteData.finished_task_num,
            dailyNoteData.total_task_num,
            dailyNoteData.is_extra_task_reward_received
        )


    //根据每日任务状态获取内容
    fun getDailyTaskContentByState(
        finishTaskNum: Int,
        totalTaskNum: Int,
        isExtraTaskRewardReceived: Boolean
    ): String {
        return if (isExtraTaskRewardReceived) {
            "已完成"
        } else {
            if (finishTaskNum == totalTaskNum) {
                "待领取"
            } else {
                "${finishTaskNum}/${totalTaskNum}"
            }
        }
    }
}