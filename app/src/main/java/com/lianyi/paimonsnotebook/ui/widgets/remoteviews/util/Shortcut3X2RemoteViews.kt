package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.util

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X2

/*
* 快捷启动3*2远端视图
* */
class Shortcut3X2RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon3X2::class.java,
    R.layout.widget_layout_shortcut_3_1
) {
    private val count = 5

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        dataStoreValuesFirst {
            val tempValue =
                intent?.getIntExtra(AppWidgetHelper.PARAM_SHORTCUT_CURRENT_PAGE, -1) ?: -1

            val page = if (tempValue < 0) {
                val tv = it[PreferenceKeys.AppWidgetShortcutCurrentPage] ?: 0
                if (tv < 0) 0 else tv
            } else {
                tempValue
            }
            PreferenceKeys.AppWidgetShortcutCurrentPage.editValue(tempValue)

            val items = HomeHelper.modalItemData

            val maxPage = items.size / count + if (items.size % count == 0) 0 else 1

            val start = page * count
            val end = start + count

            setOnClickPendingIntent(
                R.id.next,
                changePage(if (page + 1 >= maxPage) page else page + 1)
            )
            setOnClickPendingIntent(R.id.pre, changePage(if (page - 1 < 0) 0 else page - 1))

            setTextViewText(R.id.page_text, "${page + 1}/${maxPage}")

            removeAllViews(R.id.list)

            val list = items.subList(start, if (end >= items.size) items.size else end)

            val textIds = mutableListOf(
                R.id.text, R.id.page_text
            )

            val imageIds = mutableListOf(
                R.id.pre_image, R.id.next_image
            )

            list.forEach { itemData ->
                val itemViews =
                    RemoteViews(context.packageName, R.layout.widget_item_layout_shortcut).apply {
                        setImageViewResource(R.id.image, itemData.icon)

                        setTextViewText(R.id.text, itemData.name)

                        val activityIntent = Intent(context, itemData.target)

                        val pendingIntent = PendingIntent.getActivity(
                            context,
                            AppWidgetHelper.APPWIDGET_ACTIVITY_REQUEST_CODE,
                            activityIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )

                        imageIds += R.id.image
                        textIds += R.id.text

                        setOnClickPendingIntent(R.id.container, pendingIntent)
                    }

                addView(R.id.list, itemViews)
            }

            setCommonStyle(
                appWidgetBinding.configuration,
                textIds.toIntArray(),
                imageIds.toIntArray()
            )
        }

        return super.onUpdateContent(intent)
    }

    private suspend fun changePage(page: Int): PendingIntent {
        PreferenceKeys.AppWidgetShortcutCurrentPage.editValue(page)

        val bundle = Bundle().apply {
            putInt(AppWidgetHelper.PARAM_SHORTCUT_CURRENT_PAGE, page)
        }

        return basePendingIntent(AppWidgetHelper.ACTION_UPDATE_WIDGET, "$page", bundle)
    }
}