package com.lianyi.paimonsnotebook.ui.widgets.util

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.database.daily_note.util.DailyNoteHelper
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.EmptyRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ErrorRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ValidateRemoteViews

object AppWidgetRemoViewsHelper {
    /*
    * 获取远端视图
    *
    * appWidgetBinding:桌面组件绑定信息
    * user:绑定的用户
    * intent:意图
    * */
    suspend fun getRemoteViews(
        appWidgetBinding: AppWidgetBinding,
        user: User?,
        intent: Intent?
    ): RemoteViews {
        val views = RemoteViewsIndexes.getRemoteViews(appWidgetBinding) ?: return EmptyRemoteViews()

        val updateResultViews = views.onUpdateContent(intent)

        if (updateResultViews != null) {
            return updateResultViews
        }

        if (user != null) {
            val resultViews = setRemoteViewsContentWithUser(views, appWidgetBinding, user)
            if (resultViews != null) return resultViews
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
        //桌面组件实时便笺
        if (appWidgetBinding.dataType.contains(RemoteViewsDataType.DailyNoteWidget)) {
            val result = DailyNoteHelper.getDailyNoteWidgetResultData(user)

            if (!result.success) {
                return getFailRemoteViews(appWidgetBinding, result.retcode)
            }
            views.setDailyNoteWidget(result.data)
        }

        //实时便笺
        if (appWidgetBinding.dataType.contains(RemoteViewsDataType.DailyNote)) {
            val playerUid = appWidgetBinding.configuration.bindingGameRole?.playerUid
                ?: return EmptyRemoteViews("游戏角色错误")

            val result = DailyNoteHelper.getDailyNoteResultData(user,playerUid)

            if (!result.success) {
                return getFailRemoteViews(appWidgetBinding, result.retcode)
            }
            views.setDailyNote(result.data)
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
                ValidateRemoteViews(
                    appWidgetBinding.appWidgetId,
                    appWidgetBinding.userEntityMid,
                    targetCls
                )
            }

            ResultData.NETWORK_ERROR -> {
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