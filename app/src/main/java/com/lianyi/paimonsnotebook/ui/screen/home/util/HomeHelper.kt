package com.lianyi.paimonsnotebook.ui.screen.home.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.ui.screen.abyss.view.AbyssScreen
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetScreen
import com.lianyi.paimonsnotebook.ui.screen.daily_note.view.DailyNoteScreen
import com.lianyi.paimonsnotebook.ui.screen.develop.TypographyScreen
import com.lianyi.paimonsnotebook.ui.screen.gacha.view.GachaRecordScreen
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData
import com.lianyi.paimonsnotebook.ui.screen.items.view.AvatarScreen
import com.lianyi.paimonsnotebook.ui.screen.items.view.CultivationMaterialScreen
import com.lianyi.paimonsnotebook.ui.screen.items.view.WeaponScreen

object HomeHelper {
    private val context by lazy {
        PaimonsNotebookApplication.context
    }


    val modalItems = listOf(
        ModalItemData(
            "实时便笺",
            R.drawable.ic_moon,
            DailyNoteScreen::class.java
        ),
        ModalItemData(
            "祈愿记录",
            R.drawable.ic_genshin_game_wish,
            GachaRecordScreen::class.java
        ),
        ModalItemData(
            "角色资料",
            R.drawable.ic_genshin_game_character,
            AvatarScreen::class.java
        ),
        ModalItemData(
            "武器资料",
            R.drawable.ic_genshin_game_equip,
            WeaponScreen::class.java
        ),
        ModalItemData(
            "养成材料",
            R.drawable.ic_genshin_game_material,
            CultivationMaterialScreen::class.java
        ),
        ModalItemData(
            "深境螺旋",
            R.drawable.ic_genshin_game_spiral_abyss,
            AbyssScreen::class.java
        ),
        ModalItemData(
            "桌面组件",
            R.drawable.ic_appwidget,
            AppWidgetScreen::class.java
        )

    )

    fun <T : Activity> goActivity(
        cls: Class<T>,
        bundle: Bundle = Bundle(),
        flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK
    ) {
        context.apply {
            startActivity(Intent(
                this,
                cls
            ).apply {
                putExtras(bundle)
                addFlags(flags)
            })
        }
    }

    fun goActivityByIntent(block: Intent.() -> Unit) {
        val intent = Intent().apply(block)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goActivityForResultByIntent(
        launcher: ActivityResultLauncher<Intent>,
        block: Intent.() -> Unit
    ) {
        val intent = Intent().apply(block)
        launcher.launch(intent)
    }
}