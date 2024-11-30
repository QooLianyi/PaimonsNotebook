package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.config

import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss.SpiralAbyssData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData

class AppWidgetBindingConfig {

    val list = listOf(
        DailyNoteData::class,
        DailyNoteWidgetData::class,
        SpiralAbyssData::class
    )

}