package com.lianyi.paimonsnotebook.ui.widgets.util

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.EmptyRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ErrorRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ValidateRemoteViews

object AppWidgetRemoViewsHelper {
    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    //获取远端视图
    suspend fun getRemoteViews(appWidgetBinding: AppWidgetBinding, user: User?,intent: Intent?): RemoteViews {
        val views = RemoteViewsIndexes.getRemoteViews(appWidgetBinding) ?: return EmptyRemoteViews()

        val updateResultViews = views.onUpdateContent(intent)

        if(updateResultViews != null){
            return updateResultViews
        }

        if (user != null) {
            val resultViews = setRemoteViewsContentWithUser(views,appWidgetBinding, user)
            if(resultViews != null) return resultViews
        }

        return views
    }

    /*
    * 为远端视图设置需要使用用户信息的内容
    * 成功时返回null
    * 当返回值不为空时,代表更新时出现了错误
    * */
    private suspend fun setRemoteViewsContentWithUser(
        views: BaseRemoteViews,
        appWidgetBinding: AppWidgetBinding,
        user: User
    ): RemoteViews? {
        //小组件实时便笺
        if (appWidgetBinding.dataType.contains(RemoteViewsDataType.DailyNoteWidget)) {
            val dailyNoteForWidget = gameRecordClient.getDailyNoteForWidget(user)

            if (!dailyNoteForWidget.success) {
                return getFailRemoteViews(appWidgetBinding, dailyNoteForWidget.retcode)
            }
            views.setDailyNoteWidget(dailyNoteForWidget.data)
        }

        //实时便笺
        if (appWidgetBinding.dataType.contains(RemoteViewsDataType.DailyNote)) {
            val playerUid = appWidgetBinding.configuration.bindingGameRole?.playerUid
                ?: return EmptyRemoteViews("游戏角色错误")

            val userAndUid =
                UserAndUid(user, playerUid)
            val dailyNote = gameRecordClient.getDailyNote(userAndUid)

            if (!dailyNote.success) {
                return getFailRemoteViews(appWidgetBinding, dailyNote.retcode)
            }
            views.setDailyNote(dailyNote.data)
        }
        return null
    }

    //根据请求结果值返回错误视图
    private fun getFailRemoteViews(
        appWidgetBinding: AppWidgetBinding,
        requestCode: Int
    ): RemoteViews {
        val targetCls =
            RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(appWidgetBinding.remoteViewsClassName)?.appWidgetClass
                ?: return EmptyRemoteViews()

        return when (requestCode) {
            ResultData.NEED_VALIDATE -> {
                ValidateRemoteViews(appWidgetBinding.appWidgetId, targetCls)
            }

            ResultData.NETWORK_ERROR->{
                ErrorRemoteViews(
                    appWidgetBinding.appWidgetId,
                    targetCls,
                    "网络异常"
                )
            }

            else -> {
                ErrorRemoteViews(
                    appWidgetBinding.appWidgetId,
                    targetCls,
                    "发生了错误:${requestCode}"
                )
            }
        }
    }
}