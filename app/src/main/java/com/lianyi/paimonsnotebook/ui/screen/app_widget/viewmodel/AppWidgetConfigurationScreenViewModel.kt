package com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.AppWidgetConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.util.ColorPickerType
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.widgets.common.data.RemoteViewsInfo
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppWidgetConfigurationScreenViewModel : ViewModel() {
    private val dao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }

    //当前组件配置
    val configuration = AppWidgetConfigurationData()

    val context by lazy {
        PaimonsNotebookApplication.context
    }

    val defaultBackgroundConfiguration = listOf(
        "浅色" to AppWidgetHelper.PATTERN_LIGHT,
        "深色" to AppWidgetHelper.PATTERN_DARK,
        "透明" to AppWidgetHelper.PATTERN_TRANSPARENT
    )

    val firstEntryDialogButtons by lazy {
        arrayOf("不再显示", "关闭")
    }

    val defaultColorList = mutableStateListOf(
        Black,
        White,
        Color(0xFFDC4E5B),
        Color(0xFFFFC5F6),
        Color(0xFFCC93EA),
        Color(0xFFCC93E8),
        Color(0xFF95B8EC),
        Color(0xFF42A6FF),
        Color(0xFFF2743D),
        Color(0xFFFFB08D),
        Color(0xFFFFBE46),
        Color(0xFFB1EA7A),
        Color(0xFF66CF46),
        Color(0xFF79D5D5)
    )

    var showColorPickerPopup by mutableStateOf(false)

    var showRemoteViewsPickerPopup by mutableStateOf(false)

    var showGameRoleSelectDialog by mutableStateOf(false)

    var showUserSelectDialog by mutableStateOf(false)

    var textColorSelectedIndex by mutableIntStateOf(1)

    var imageTintColorSelectedIndex by mutableIntStateOf(1)

    private var colorPickerType = ColorPickerType.None

    var firstEntry by mutableStateOf(false)

    init {
        viewModelScope.launch {
            firstEntry =
                context.datastorePf.data.first()[PreferenceKeys.FirstEntryAppWidgetConfigurationScreen]
                    ?: true
        }
    }

    lateinit var finishActivity: () -> Unit

    fun init(intent: Intent?) {
        configuration.add = intent?.getIntExtra(AppWidgetHelper.PARAM_ADD_FLAG, -1) != -1
        configuration.remoteViewsClassName =
            intent?.getStringExtra(AppWidgetHelper.PARAM_REMOTE_VIEWS_CLASS_NAME) ?: ""
        configuration.appWidgetClassName =
            intent?.getStringExtra(AppWidgetHelper.PARAM_APPWIDGET_CLASS_NAME) ?: ""

        configuration.appWidgetId =
            intent?.getIntExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, -1) ?: -1

        if (configuration.remoteViewsClassName.isEmpty()) {
            val list =
                RemoteViewsIndexes.getSupportRemoteViewsListByAppWidgetClsName(configuration.appWidgetClassName)

            //设置支持的组件列表的第一个为选中的对象
            if (list.isNotEmpty()) {
                val remoteViewsInfo = list.first().second
                setConfigurationInfo(remoteViewsInfo)
            }
        } else {
            val remoteViewsInfo =
                RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(configuration.remoteViewsClassName)
            setConfigurationInfo(remoteViewsInfo)
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (configuration.add) {
                registerAddWidgetSuccessBroadcast()
            } else {
                updateInit(configuration.appWidgetId)
            }
        }
    }

    private fun setConfigurationInfo(remoteViewsInfo: RemoteViewsInfo?) {
        if (remoteViewsInfo == null) return

        configuration.setValueForRemoteViewsInfo(remoteViewsInfo)
    }

    private suspend fun updateInit(appWidgetId: Int) {
        AccountHelper.userListFlow.collectLatest { list ->
            if (list.isEmpty()) return@collectLatest

            val appWidgetBinding = dao.getAppWidgetBindingByAppWidgetId(appWidgetId)

            val user =
                AccountHelper.userListFlow.value.takeFirstIf { it.userEntity.mid == appWidgetBinding?.userEntityMid }

            configuration.bindUser = user

            if (appWidgetBinding != null) {
                configuration.setValueForConfiguration(appWidgetBinding.configuration)

                textColorSelectedIndex =
                    defaultColorList.indexOf(configuration.textColor).let {
                        if (it != -1) {
                            it
                        } else {
                            configuration.customTextColor = configuration.textColor
                            0
                        }
                    }

                imageTintColorSelectedIndex =
                    defaultColorList.indexOf(configuration.imageTintColor).let {
                        if (it != -1) {
                            it
                        } else {
                            configuration.customImageTintColor = configuration.imageTintColor
                            0
                        }
                    }
            }

            snapshotFlow { user?.userGameRoles?.toList() }.collectLatest { roles ->
                configuration.setGameRole(
                    roles?.takeFirstIf { it.game_uid == appWidgetBinding?.configuration?.bindingGameRole?.playerUid?.value }
                )
            }
        }
    }

    fun onFirstEntryDialogButtonSelect(index: Int) {
        if (index == 0) {
            viewModelScope.launch {
                PreferenceKeys.FirstEntryAppWidgetConfigurationScreen.editValue(false)
            }
        }
        firstEntry = false
    }

    private fun showColorPickerPopup() {
        showColorPickerPopup = true
    }

    fun dismissColorPickerPopup() {
        println("dismissColorPickerPopup")
        showColorPickerPopup = false
    }

    fun showGameRoleSelectDialog() {
        showGameRoleSelectDialog = true
    }

    fun dismissGameRoleSelectDialog() {
        showGameRoleSelectDialog = false
    }

    fun showRemoteViewsPickPopup() {
        showRemoteViewsPickerPopup = true
    }

    fun dismissRemoteViewsPickPopup() {
        showRemoteViewsPickerPopup = false
    }

    fun dismissUserDialog() {
        showUserSelectDialog = false
    }

    fun showUserDialog() {
        showUserSelectDialog = true
    }

    fun changeRemoteViews(
        remoteViewsInfo: RemoteViewsInfo
    ) {
        configuration.setValueForRemoteViewsInfo(remoteViewsInfo)
    }

    fun changeBackgroundPattern(pair: Pair<String, String>, scope: CoroutineScope) {
        scope.launch {
            configuration.setBackgroundPattern(pattern = pair.second)
        }
    }

    fun changeTextColor(color: Color, index: Int, scope: CoroutineScope) {
        if (index == 0) {
            showColorPickerPopup()
            colorPickerType = ColorPickerType.Text
        }
        textColorSelectedIndex = index

        scope.launch {
            configuration.setTextColor(color)
        }
    }

    fun changeImageTintColor(color: Color, index: Int, scope: CoroutineScope) {
        if (index == 0) {
            showColorPickerPopup()
            colorPickerType = ColorPickerType.Image
        }

        imageTintColorSelectedIndex = index

        scope.launch {
            configuration.setImageTintColor(color)
        }
    }

    fun onColorPickerSelectedColor(color: Color, scope: CoroutineScope) {
        when (colorPickerType) {
            ColorPickerType.Image -> {
                changeImageTintColor(color, 0, scope)
                configuration.customImageTintColor = color
            }

            ColorPickerType.Text -> {
                changeTextColor(color, 0, scope)
                configuration.customTextColor = color
            }

            else -> {}
        }
        dismissColorPickerPopup()
    }

    fun changeGameRole(user: User, role: UserGameRoleData.Role) {
        configuration.setGameRole(role)
        configuration.bindUser = user
        dismissGameRoleSelectDialog()
    }

    fun changeUser(user: User) {
        configuration.bindUser = user
        dismissUserDialog()
    }

    fun submit() {
        if (configuration.showUser && configuration.bindUser == null) {
            "你还没有绑定用户".errorNotify(false)
            return
        }

        if (configuration.showGameRole && (configuration.bindUser == null || configuration.bindGameRole == null)) {
            "你还没有绑定角色".errorNotify(false)
            return
        }

        if (configuration.add) {
            addMode()
        } else {
            updateMode()
        }
    }

    private fun updateMode() {
        val appWidgetBinding = configuration.toAppWidgetBinding()

        if (appWidgetBinding == null) {
            "修改小组件时发生了错误".errorNotify()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(appWidgetBinding)
            val success = AppWidgetHelper.updateAppWidgetContentById(appWidgetBinding.appWidgetId)

            if (success) {
                "小组件修改完毕".notify()
            } else {
                "修改小组件时发生错误".errorNotify()
            }
        }
    }

    private fun addMode() {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            val componentName = ComponentName(context, configuration.appWidgetClassName)
            val broadcast =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(AppWidgetHelper.ACTION_UPDATE_CONFIGURATION),
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

            appWidgetManager.requestPinAppWidget(componentName, null, broadcast)
        } else {
            "你的系统不支持通过程序添加小组件".warnNotify()
        }
    }

    private val addWidgetSuccessBroadcast: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                val appWidgetManager = AppWidgetManager.getInstance(context)

                /*
                * 从intent中获取携带的小组件id
                * 在一些系统中(如MIUI)successCallback的intent不会携带小组件id(EXTRA_APPWIDGET_ID)
                * 当没有id时,根据添加的类型获取全部的id并获取最大值作为更新的id
                * */
                val appwidgetId = intent?.extras?.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetManager.getAppWidgetIds(
                        ComponentName(
                            context,
                            configuration.appWidgetClassName
                        )
                    ).maxOrNull() ?: -1
                ) ?: -1

                val appWidgetBinding =
                    configuration.toAppWidgetBinding(appwidgetId)

                if (appWidgetBinding == null) {
                    "小组件创建完成,但自动配置失败,请通过已创建的小组件进行配置".errorNotify()
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        dao.insert(appWidgetBinding)
                    } catch (e: Exception) {
                        "小组件添加本地数据时出现异常".errorNotify()
                        return@launch
                    }

                    AppWidgetHelper.updateAppWidgetContentById(appwidgetId)
                    "小组件添加并配置成功".notify()

                    if(this@AppWidgetConfigurationScreenViewModel::finishActivity.isInitialized){
                        finishActivity.invoke()
                    }
                }
            }
        }
    }

    private fun registerAddWidgetSuccessBroadcast() {
        val filter = IntentFilter()
        filter.addAction(AppWidgetHelper.ACTION_UPDATE_CONFIGURATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                addWidgetSuccessBroadcast,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(addWidgetSuccessBroadcast, filter)
        }
    }

    fun unregisterAddWidgetSuccessBroadcast() {
        try {
            if (configuration.add) {
                context.unregisterReceiver(addWidgetSuccessBroadcast)
            }
        } catch (_: Exception) {
        }
    }
}