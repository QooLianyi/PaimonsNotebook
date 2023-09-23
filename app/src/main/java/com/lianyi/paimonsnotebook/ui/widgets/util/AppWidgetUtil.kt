package com.lianyi.paimonsnotebook.ui.widgets.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.AccountData
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AccountAppWidgetBinding
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.ui.widgets.resin.widget.ResinHorizontalWidget
import kotlinx.coroutines.flow.first
import java.io.File

object AppWidgetUtil {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val appWidgetLayoutDataStore by lazy {
        context.filesDir.resolve("datastore")
    }

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    private val appWidgetAccountBindingKey by lazy {
        stringPreferencesKey("app_widget_account_binding")
    }

    private val appWidgetAccountBindingMap by lazy {
        mutableMapOf<String, Int>()
    }

    private val accountAppWidgetBindingDao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }

    val currentState by lazy {
        stringPreferencesKey("currentState")
    }

    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            appWidgetAccountBindingMap.clear()
//
//            val json = context.datastorePf.data.map {
//                it[appWidgetAccountBindingKey]
//            }.first() ?: "[]"
//
//            JSON.parse<Map<String, Int>>(
//                json,
//                getParameterizedType(List::class.java, String::class.java, Int::class.java)
//            ).forEach { (k, v) ->
//                appWidgetAccountBindingMap[k] = v
//            }
//        }
    }

    fun gotoAppWidgetBindingScreen() {

    }

    /*
    * 移除本地的appWidgetLayout-$appWidgetId文件
    * */
    fun deleteAppWidgetLayoutFile(appWidgetId: Int) {
        try {
            File(appWidgetLayoutDataStore, getAppWidgetLayoutPath(appWidgetId)).delete()
        } catch (_: Exception) {
        }
    }

    fun deleteAppWidgetLayoutFile(glanceId: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        deleteAppWidgetLayoutFile(appWidgetId = appWidgetId)
    }

    private fun getAppWidgetLayoutPath(appWidgetId: Int) = "appWidgetLayout-$appWidgetId"

    suspend fun updateHorizontalResinWidgetByGlanceId(
        context: Context,
        glanceId: GlanceId,
        onError: (Int) -> Unit = {},
        onSuccess: () -> Unit = {},
    ) {
//        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)

//        val accountAppWidgetBindingData =
//            accountAppWidgetBindingDao.getDataByAppWidgetId(appWidgetId = appWidgetId).first()

//        if (accountAppWidgetBindingData != null) {
//            val dailyNoteData = getDailyNoteByAppWidgetId(appWidgetId = appWidgetId)
//
//            CoroutineScope(Dispatchers.Main).launch {
//                if (dailyNoteData.success) {
//                    onSuccess.invoke()
//                } else {
//                    onError.invoke(dailyNoteData.retcode)
//                }
//            }
//
//            updateAppWidgetState(context, glanceId) {
//                it[currentState] = "error"
//            }
            ResinHorizontalWidget(null).update(context, glanceId)
    }

    fun setAccountAppWidgetBinding(accountUid: String, appWidgetId: Int) {
    }

    suspend fun getBindingAccountByAppWidgetId(appWidgetId: Int): AccountData? {
        val accountAppWidgetBindingData =
//            accountAppWidgetBindingDao.getDataByAppWidgetId(appWidgetId = appWidgetId).first()

//        if (accountAppWidgetBindingData != null) {
//            AccountManager.accountList.forEach { accountData ->
//                println("accountAppWidgetBindingData = ${accountAppWidgetBindingData}")
//                if (accountData.userInfo.uid == accountAppWidgetBindingData.accountUid) {
//                    return accountData
//                }
//            }
//        }
        return null
    }
}