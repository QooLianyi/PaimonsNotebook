package com.lianyi.paimonsnotebook.ui.widgets.util

import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.ui.widgets.common.data.RemoteViewsInfo
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note.DailyNote2X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note.DailyNoteOverview3X2RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note.Expedition3X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.DailyNoteWidget2X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.HomeCoinRingProgressBar1X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.Resin2X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.ResinProgressBarRecoverTime3X2RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.ResinRecoverTime2X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget.ResinRingProgressBar1X1RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.util.DailyMaterial3X2RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.util.Shortcut3X2RemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon1X1
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X1
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X1
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X2

/*
* 远端视图索引
* 在此注册所有的远端视图,并配置所需的数据类型和组件类型
* */
object RemoteViewsIndexes {

    //所有组件
    val list by lazy {
        remoteViewsIndexesInfo.map { it.value }
    }

    private val defaultConfigurationOption by lazy {
        setOf(
            AppWidgetConfigurationOption.ChangeWidget,
            AppWidgetConfigurationOption.BackgroundColor,
            AppWidgetConfigurationOption.BackgroundRadius,
            AppWidgetConfigurationOption.TextColor
        )
    }

    private val defaultConfigurationOptionsGameRole by lazy {
        setOf(
            AppWidgetConfigurationOption.GameRole
        ) + defaultConfigurationOption
    }
    private val defaultConfigurationOptionsUser by lazy {
        setOf(
            AppWidgetConfigurationOption.User
        ) + defaultConfigurationOption
    }

    //远端视图索引信息
    private val remoteViewsIndexesInfo by lazy {
        dailyNoteRemoteViewsInfoList +
        mapOf(
            Expedition3X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon3X1::class.java,
                remoteViewsClass = Expedition3X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNote),
                remoteViewsName = "派遣委托3*1",
                remoteViewsType = RemoteViewsType.DailyNoteExpedition,
                configurationOptions = defaultConfigurationOptionsGameRole
            ),
            DailyMaterial3X2RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon3X2::class.java,
                remoteViewsClass = DailyMaterial3X2RemoteViews::class.java,
                remoteViewsName = "每日材料3*2",
                remoteViewsType = RemoteViewsType.DailyMaterial,
                configurationOptions = setOf(
                    AppWidgetConfigurationOption.BackgroundPatten,
                    AppWidgetConfigurationOption.TextColor
                )
            ),
            Shortcut3X2RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon3X1::class.java,
                remoteViewsClass = Shortcut3X2RemoteViews::class.java,
                remoteViewsName = "快捷启动3*2",
                remoteViewsType = RemoteViewsType.Util,
                configurationOptions = setOf(
                    AppWidgetConfigurationOption.BackgroundColor,
                    AppWidgetConfigurationOption.TextColor,
                    AppWidgetConfigurationOption.ImageTintColor
                )
            ),
        )
    }

    //实时便笺桌面组件远端视图列表
    private val dailyNoteRemoteViewsInfoList by lazy {
        mapOf(
            DailyNoteWidget2X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon2X1::class.java,
                remoteViewsClass = DailyNoteWidget2X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "实时便笺2*1",
                remoteViewsType = RemoteViewsType.DailyNote,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            HomeCoinRingProgressBar1X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon1X1::class.java,
                remoteViewsClass = HomeCoinRingProgressBar1X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "洞天宝钱环形进度条1*1",
                remoteViewsType = RemoteViewsType.DailyNoteHomeIcon,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            DailyNote2X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon2X1::class.java,
                remoteViewsClass = DailyNote2X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNote),
                remoteViewsName = "实时便笺2*1",
                remoteViewsType = RemoteViewsType.DailyNote,
                configurationOptions = defaultConfigurationOptionsGameRole
            ),
            ResinRingProgressBar1X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon1X1::class.java,
                remoteViewsClass = ResinRingProgressBar1X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "原粹树脂环形进度条1*1",
                remoteViewsType = RemoteViewsType.DailyNoteResin,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            Resin2X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon2X1::class.java,
                remoteViewsClass = Resin2X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "原粹树脂2*1",
                remoteViewsType = RemoteViewsType.DailyNoteResin,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            ResinRecoverTime2X1RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon2X1::class.java,
                remoteViewsClass = ResinRecoverTime2X1RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "原粹树脂恢复时间2*1",
                remoteViewsType = RemoteViewsType.DailyNoteResin,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            ResinProgressBarRecoverTime3X2RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon3X2::class.java,
                remoteViewsClass = ResinProgressBarRecoverTime3X2RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNoteWidget),
                remoteViewsName = "原粹树脂恢复时间与进度3*2",
                remoteViewsType = RemoteViewsType.DailyNoteResin,
                configurationOptions = defaultConfigurationOptionsUser
            ),
            DailyNoteOverview3X2RemoteViews::class.java.name to RemoteViewsInfo(
                appWidgetClass = AppWidgetCommon3X2::class.java,
                remoteViewsClass = DailyNoteOverview3X2RemoteViews::class.java,
                dataType = setOf(RemoteViewsDataType.DailyNote),
                remoteViewsName = "实时便笺3*2",
                remoteViewsType = RemoteViewsType.DailyNote,
                configurationOptions = defaultConfigurationOption
            ),
        )
    }


    //根据远端视图类名获取远端视图信息
    fun getRemoteViewsInfoByRemoteViewsClassName(clsName: String) = remoteViewsIndexesInfo[clsName]

    //根据远端视图类名获取组件名称
    fun getAppWidgetClassNameByRemoteViewsClassName(clsName: String) =
        remoteViewsIndexesInfo[clsName]?.appWidgetClass?.name ?: ""

    //每个小组件支持的远端视图类型
    private val appWidgetRemoteViewsMap by lazy {
        remoteViewsIndexesInfo.toList().groupBy {
            it.second.appWidgetClass.name
        }.map { clsList ->
            clsList.key to clsList.value.map { it.first to it.second }
        }.toMap()
    }

    //通过标准组件类名获取支持的远端视图信息列表
    fun getSupportRemoteViewsListByAppWidgetClsName(appwidgetClsName: String) =
        appWidgetRemoteViewsMap[appwidgetClsName] ?: listOf()

    /*
    * 通过反射创建一个新的远端视图
    * 当目标类不存在时,返回空
    * */
    fun getRemoteViews(appWidgetBinding: AppWidgetBinding): BaseRemoteViews? =
        try {
            Class.forName(appWidgetBinding.remoteViewsClassName)
                .getConstructor(AppWidgetBinding::class.java)
                .newInstance(appWidgetBinding) as BaseRemoteViews
        } catch (e: Exception) {
            null
        }
}