package com.lianyi.paimonsnotebook.ui.screen.home.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.ui.screen.abyss.view.AbyssScreen
import com.lianyi.paimonsnotebook.ui.screen.achievement.view.AchievementScreen
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetScreen
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.view.CultivateProjectScreen
import com.lianyi.paimonsnotebook.ui.screen.daily_note.view.DailyNoteScreen
import com.lianyi.paimonsnotebook.ui.screen.develop.TypographyScreen
import com.lianyi.paimonsnotebook.ui.screen.gacha.view.GachaRecordScreen
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData
import com.lianyi.paimonsnotebook.ui.screen.items.view.AvatarScreen
import com.lianyi.paimonsnotebook.ui.screen.items.view.CultivationMaterialScreen
import com.lianyi.paimonsnotebook.ui.screen.items.view.WeaponScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object HomeHelper {
    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    val modalItemData = listOf(
        ModalItemData(
            name = "实时便笺",
            icon = R.drawable.ic_moon,
            target = DailyNoteScreen::class.java,
            sortIndex = 10
        ),
        ModalItemData(
            name = "祈愿记录",
            icon = R.drawable.ic_genshin_game_wish,
            target = GachaRecordScreen::class.java,
            sortIndex = 20,
            requireMetadata = true
        ),
        ModalItemData(
            name = "角色资料",
            icon = R.drawable.ic_genshin_game_character,
            target = AvatarScreen::class.java,
            sortIndex = 30,
            requireMetadata = true
        ),
        ModalItemData(
            name = "武器资料",
            icon = R.drawable.ic_genshin_game_equip,
            target = WeaponScreen::class.java,
            sortIndex = 40,
            requireMetadata = true
        ),
        ModalItemData(
            name = "养成计划",
            icon = R.drawable.ic_genshin_game_ggc_book,
            target = CultivateProjectScreen::class.java,
            sortIndex = 50,
            requireMetadata = true
        ),
        ModalItemData(
            name = "养成材料",
            icon = R.drawable.ic_genshin_game_material,
            target = CultivationMaterialScreen::class.java,
            sortIndex = 60,
            requireMetadata = true
        ),
        ModalItemData(
            name = "深境螺旋",
            icon = R.drawable.ic_genshin_game_spiral_abyss,
            target = AbyssScreen::class.java,
            sortIndex = 70,
            requireMetadata = true
        ),
        ModalItemData(
            name = "成就管理",
            icon = R.drawable.ic_genshin_game_sign_cup,
            target = AchievementScreen::class.java,
            sortIndex = 80,
            requireMetadata = true
        ),
        ModalItemData(
            name = "桌面组件",
            icon = R.drawable.ic_appwidget,
            target = AppWidgetScreen::class.java,
            sortIndex = 90,
            requireMetadata = true
        ),
    )

    private val ModalItemsStateFlow = MutableStateFlow<List<ModalItemData>>(listOf())

    //对外开放的modalItems流
    val modalItemsFlow = ModalItemsStateFlow.asStateFlow()

    init {
//        CoroutineScope(Dispatchers.IO).launchIO {
//            val v = dataStoreValuesFirstLambda {
//                this[PreferenceKeys.EnableMetadata] ?: false
//            }
//
//        }
    }

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

    fun goActivityByIntentNewTask(block: Intent.() -> Unit) = goActivityByIntent {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        block.invoke(this)
    }

    @SuppressLint("IntentWithNullActionLaunch")
    fun goActivityByIntent(block: Intent.() -> Unit) {
        val intent = Intent().apply(block)
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