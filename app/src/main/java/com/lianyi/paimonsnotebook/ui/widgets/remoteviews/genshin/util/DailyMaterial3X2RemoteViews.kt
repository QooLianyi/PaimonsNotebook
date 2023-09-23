package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.util

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.MaterialService
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Materials
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setBackgroundResource
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setImageTint
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setTextColor
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ErrorRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X2
import java.time.LocalDateTime

/*
* 每日材料3*2远端视图
* */
class DailyMaterial3X2RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon3X2::class.java,
    R.layout.widget_layout_daily_material_3_2
) {
    private val count = 10

    private var serviceInit = true
    private val materialService = MaterialService {
        serviceInit = false
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        if (!serviceInit) {
            return ErrorRemoteViews(appWidgetId, targetCls, "更新失败:获取材料时发生错误")
        }

        dataStoreValuesFirst {
            val tempValue =
                intent?.getIntExtra(AppWidgetHelper.PARAM_SHORTCUT_CURRENT_PAGE, -1) ?: -1

            val configuration = appWidgetBinding.configuration


            val pair = Materials.getMaterialsIdByWeek(
                week = LocalDateTime.now().dayOfWeek.value,
                simpleMode = true
            )
            val items = materialService.getMaterialListByIds(pair.first)
                .filter { material -> material.RankLevel == QualityType.QUALITY_PURPLE || material.RankLevel == QualityType.QUALITY_ORANGE }

            val maxPage = items.size / count + if (items.size % count == 0) 0 else 1

            val page = if (tempValue < 0 || tempValue >= maxPage) {
                val tv = it[PreferenceKeys.AppWidgetShortcutCurrentPage] ?: 0

                if (tv < 0 || tv >= maxPage) 0 else tv
            } else {
                tempValue
            }

            PreferenceKeys.AppWidgetDailyMaterialCurrentPage.editValue(page)

            val start = (if(page >= maxPage) 0 else page) * count
            val end = start + count

            setOnClickPendingIntent(
                R.id.next,
                changePage(if (page + 1 >= maxPage) page else page + 1)
            )
            setOnClickPendingIntent(R.id.pre, changePage(if (page - 1 < 0) 0 else page - 1))

            setBackgroundResource(
                R.id.container,
                AppWidgetHelper.getAppWidgetBackgroundResource(configuration.backgroundPattern)
            )

            setBackgroundResource(
                R.id.next_image,
                AppWidgetHelper.getAppWidgetOptionBackgroundResource(configuration.backgroundPattern)
            )

            setBackgroundResource(
                R.id.pre_image,
                AppWidgetHelper.getAppWidgetOptionBackgroundResource(configuration.backgroundPattern)
            )

            configuration.textColor?.let { color ->
                //如果背景主题为透明,选项按钮颜色跟随字体颜色
                val actionImageColor =
                    if (configuration.backgroundPattern != AppWidgetHelper.PATTERN_TRANSPARENT) {
                        AppWidgetHelper.getAppWidgetOptionTintColorResource(configuration.backgroundPattern)
                    } else {
                        color
                    }
                setImageTint(R.id.pre_image, actionImageColor)
                setImageTint(R.id.next_image, actionImageColor)
            }

            setTextColor(R.id.text, configuration.textColor)
            setTextColor(R.id.page_text, configuration.textColor)

            setTextViewText(R.id.page_text, "${page + 1}/${maxPage}")
            setTextViewText(R.id.text, "今天是[${TimeHelper.getWeekName(week = pair.second)}]")

            val list = items.subList(start, if (end >= items.size) items.size else end)

            loadImage(
                urls = items.map { material -> material.iconUrl }.toTypedArray(),
                "桌面组件"
            )

            removeAllViews(R.id.list1)
            removeAllViews(R.id.list2)

            val materialList = list.split(5)

            addMaterialListItemView(R.id.list1, materialList.first(), configuration)

            if (materialList.size > 1) {
                addMaterialListItemView(R.id.list2, materialList.last(), configuration)
            }
        }

        return super.onUpdateContent(intent)
    }

    private fun addMaterialListItemView(
        targetViewId: Int,
        list: List<Material>,
        configuration:AppWidgetConfiguration
    ) {
        list.forEach { itemData ->
            val itemViews =
                RemoteViews(
                    context.packageName,
                    R.layout.widget_item_layout_daily_material
                ).apply {
                    val imageFile =
                        PaimonsNotebookImageLoader.getCacheImageFileByUrl(itemData.iconUrl)
                    val bitmap = BitmapFactory.decodeFile(imageFile?.path)

                    setImageViewBitmap(R.id.image, bitmap)

                    setTextViewText(R.id.text, itemData.Name)

                    setTextColor(R.id.text, configuration.textColor)
                    setTextColor(R.id.page_text, configuration.textColor)
                }

            addView(targetViewId, itemViews)
        }
    }

    private suspend fun changePage(page: Int): PendingIntent {
        PreferenceKeys.AppWidgetDailyMaterialCurrentPage.editValue(page)

        val bundle = Bundle().apply {
            putInt(AppWidgetHelper.PARAM_SHORTCUT_CURRENT_PAGE, page)
        }

        return basePendingIntent(AppWidgetHelper.ACTION_UPDATE_WIDGET, "$page", bundle)
    }
}