package com.lianyi.paimonsnotebook.ui.widgets.util

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase

object AppWidgetHelper {
    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val dao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }

    private const val PACKAGE_NAME = "com.lianyi.paimonsnotebook.appwidget"
    private const val PACKAGE_NAME_ACTION = "${PACKAGE_NAME}.action"
    private const val PACKAGE_NAME_PARAM = "${PACKAGE_NAME}.param"

    const val REQUEST_CODE_TEST = 100

    const val APPWIDGET_REQUEST_CODE = 10

    const val APPWIDGET_ACTIVITY_REQUEST_CODE = 11

    //更新组件
    const val ACTION_UPDATE_WIDGET = "${PACKAGE_NAME_ACTION}.UPDATE_APPWIDGET"

    //更新组件
    const val ACTION_DELETE_WIDGET = "${PACKAGE_NAME_ACTION}.DELETE_WIDGET"

    //添加组件
    const val ACTION_ADD_WIDGET = "${PACKAGE_NAME_ACTION}.ADD_WIDGET"

    //组件配置更新
    const val ACTION_UPDATE_CONFIGURATION = "${PACKAGE_NAME_ACTION}.UPDATE_CONFIGURATION"

    //小组件id
    const val PARAM_APPWIDGET_ID = "${PACKAGE_NAME_PARAM}.APPWIDGET_ID"

    //显示模式参数
    const val PARAM_PATTERN_TYPE = "${PACKAGE_NAME_PARAM}.PATTERN_TYPE"

    //背景样式,浅色
    const val PATTERN_LIGHT = "LIGHT"

    //背景样式,深色
    const val PATTERN_DARK = "DARK"

    //背景样式,透明
    const val PATTERN_TRANSPARENT = "TRANSPARENT"

    //背景样式,自定义
    const val PATTERN_CUSTOM = "CUSTOM"

    //改变组件字体颜色,优先级高于显示模式
    const val ACTION_CHANGE_TEXT_COLOR = "${PACKAGE_NAME_ACTION}.CHANGE_TEXT_COLOR"

    //字体颜色参数,传递int
    const val PARAM_TEXT_COLOR = "${PACKAGE_NAME_PARAM}.TEXT_COLOR"

    //绑定组件通过game_role
    const val ACTION_GAME_ROLE_BIND_APPWIDGET = "${PACKAGE_NAME_ACTION}.GAME_ROLE_BIND_APPWIDGET"

    //前往配置界面
    const val ACTION_GO_CONFIGURATION = "${PACKAGE_NAME_ACTION}.GO_CONFIGURATION"

    //前往验证界面
    const val ACTION_GO_VALIDATE = "${PACKAGE_NAME_ACTION}.GO_VALIDATE"

    //传递的参数,传递json
    const val PARAM_DATA_JSON = "${PACKAGE_NAME_PARAM}.DATA_JSON"

    const val PARAM_ADD_FLAG = "${PACKAGE_NAME_PARAM}.ADD_FLAG"

    const val FLAG_ADD = 0

    //小组件类名,
    const val PARAM_APPWIDGET_CLASS_NAME = "${PACKAGE_NAME_PARAM}.APP_WIDGET_CLASS_NAME"

    const val PARAM_REMOTE_VIEWS_CLASS_NAME = "${PACKAGE_NAME_PARAM}.REMOTE_VIEWS_CLASS_NAME"

    //快速启动当前页面
    const val PARAM_SHORTCUT_CURRENT_PAGE = "${PACKAGE_NAME_PARAM}.SHORTCUT_CURRENT_PAGE"

    //通知组件
    const val ACTION_OPEN_MIYOUSHE_WEB = "${PACKAGE_NAME_ACTION}.OPEN_MIYOUSHE_WEB"

    //验证携带的url
    const val PARAM_MIYOUSHE_URL = "MIYOUSHE_URL"


    /*
    * 创建唯一的uri
    * 当有多个相同的action在同一个组件内,需要设置不同的data让系统区别PendingIntent
    * 否则最后声明的会覆盖前面所有的
    * */
    private const val PACKAGE_NAME_URI = "${PACKAGE_NAME}.uri."

    fun updateAppWidgetContentById(appwidgetId: Int): Boolean {
        val appWidgetBinding = dao.getAppWidgetBindingByAppWidgetId(appwidgetId) ?: return false

        val baseAppWidget =
            RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(appWidgetBinding.remoteViewsClassName)?.appWidgetClass
                ?: return false

        context.sendBroadcast(Intent(ACTION_UPDATE_WIDGET).apply {
            component = ComponentName(context, baseAppWidget)
            putExtra(PARAM_APPWIDGET_ID, appwidgetId)
            data = getUnionData(appwidgetId)
        })
        return true
    }

    //获取小组件背景
    fun getAppWidgetBackgroundResource(type: String?) =
        when (type) {
            PATTERN_DARK -> R.drawable.bg_widget_dark
            PATTERN_TRANSPARENT -> R.drawable.bg_widget_transparent
            else -> R.drawable.bg_widget_light
        }

    //获取小组件选择背景
    fun getAppWidgetOptionBackgroundResource(type: String?) =
        when (type) {
            PATTERN_DARK -> R.drawable.bg_widget_option_dark
            PATTERN_TRANSPARENT -> R.drawable.bg_widget_option_transparent
            else -> R.drawable.bg_widget_option_light
        }

    fun getAppWidgetOptionTintColorResource(type: String?) =
        when (type) {
            PATTERN_DARK -> context.getColor(R.color.white)
            else -> context.getColor(R.color.black)
        }

    fun getUnionData(appwidgetId: Int, unionIdentity: String = ""): Uri =
        Uri.parse("${PACKAGE_NAME_URI}.${unionIdentity}_${appwidgetId}")
}