package com.lianyi.paimonsnotebook.ui.screen.app_widget.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.extension.color.parseColor
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.widgets.common.data.RemoteViewsInfo
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetConfigurationOption
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.enums.RemoteViewsDataType
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsPreviewHelper

/*
* 桌面组件配置类
* */
class AppWidgetConfigurationData {
    private val previewAnim by lazy {
        RemoteViewsPreviewAnimData(
            textColor = textColor,
            backgroundColor = backgroundColor,
            imageTintColor = imageTintColor,
            backgroundRadius = backgroundRadius
        )
    }

    private val backgroundColorDefault = White
    var backgroundColor by mutableStateOf(backgroundColorDefault)
        private set

    var customBackgroundColor by mutableStateOf(backgroundColorDefault)

    private val backgroundRadiusDefault = 8f
    var backgroundRadius by mutableFloatStateOf(backgroundRadiusDefault)
        private set

    private val textColorDefault = Black
    var textColor by mutableStateOf(textColorDefault)
        private set
    var customTextColor by mutableStateOf(textColorDefault)

    private val imageTintColorDefault = Black
    var imageTintColor by mutableStateOf(imageTintColorDefault)
        private set
    var customImageTintColor by mutableStateOf(imageTintColorDefault)

    var customBackgroundImage by mutableStateOf("")
    var customBackgroundImageUrl by mutableStateOf("https://i1.hdslb.com/bfs/archive/bb7538a835e5cd8fa494cac755236901d56622e8.jpg")

    var bindGameRole by mutableStateOf<AppWidgetConfiguration.BindingGameRole?>(null)
        private set
    var bindUser by mutableStateOf<User?>(null)

    var add by mutableStateOf(true)

    var remoteViewsClassName by mutableStateOf("")
    var appWidgetClassName = ""

    var remoteViewsName by mutableStateOf("")
    var remoteViewsDesc by mutableStateOf("")
    var remoteViewsSize by mutableStateOf("")

    private var dataType = mutableSetOf<RemoteViewsDataType>()

    var appWidgetId = -1

    private var previewComponent by mutableStateOf<@Composable (RemoteViewsPreviewAnimData) -> Unit>(
        {})

    private var configurationOptions = setOf<AppWidgetConfigurationOption>()

    val showChangeWidget: Boolean
        get() = configurationOptions.contains(AppWidgetConfigurationOption.ChangeWidget)

    val showGameRole
        get() =
            configurationOptions.contains(AppWidgetConfigurationOption.GameRole)

    val showBackgroundColor
        get() =
            configurationOptions.contains(AppWidgetConfigurationOption.BackgroundColor)

    val showBackgroundRadius
        get() =
            configurationOptions.contains(AppWidgetConfigurationOption.BackgroundRadius)

    val showTextColor
        get() =
            configurationOptions.contains(AppWidgetConfigurationOption.TextColor)

    val showImageTintColor
        get() =
            configurationOptions.contains(AppWidgetConfigurationOption.ImageTintColor)

    val showUser
        get() = configurationOptions.contains(AppWidgetConfigurationOption.User)

    val showProcessColorPrimary
        get() = configurationOptions.contains(AppWidgetConfigurationOption.ProgressBarColorPrimary)

    val showProcessColorSecond
        get() = configurationOptions.contains(AppWidgetConfigurationOption.ProgressBarColorSecond)

    val showTimeFormat
        get() = configurationOptions.contains(AppWidgetConfigurationOption.TimeFormat)

    val showCustomBackgroundImage
        get() = configurationOptions.contains(AppWidgetConfigurationOption.CustomBackgroundImage)

    @Composable
    fun ShowPreview() {
        previewComponent.invoke(previewAnim)
    }

    fun setGameRole(role: UserGameRoleData.Role?) {
        bindGameRole = if (role != null) {
            AppWidgetConfiguration.BindingGameRole(
                playerUid = role.let {
                    PlayerUid(it.game_uid, it.region)
                },
                regionName = role.region_name,
                gameBiz = role.game_biz,
                nickname = role.nickname
            )
        } else {
            null
        }
    }


    suspend fun setTextColor(color: Color) {
        textColor = color
        previewAnim.changeTextColor(color)
    }

    suspend fun setBackgroundPattern(pattern: String) {
        if(backgroundColor == backgroundColorDefault) return

        val color = getColorByPattern(pattern)
        backgroundColor = color
        previewAnim.changeBackgroundColor(color)
    }

    suspend fun setBackgroundRadius(value:Float){
        backgroundRadius = value

        previewAnim.changeBackgroundRadius(backgroundRadius)
    }

    suspend fun setBackgroundColor(color: Color){
        backgroundColor = color

        previewAnim.changeBackgroundColor(color)
    }

    suspend fun setImageTintColor(color: Color) {
        imageTintColor = color
        previewAnim.changeImageTintColor(color)
    }


    private fun getColorByPattern(pattern: String) =
        when (pattern) {
            AppWidgetHelper.PATTERN_DARK -> {
                Black
            }

            AppWidgetHelper.PATTERN_LIGHT -> {
                White
            }

            else -> {
                Transparent
            }
        }

    fun toAppWidgetBinding(
        widgetId: Int = appWidgetId
    ): AppWidgetBinding? {

        if (widgetId == -1) {
            return null
        }

        val configuration = AppWidgetConfiguration(
            textColor = if (textColor != textColorDefault) textColor.toArgb() else null,
            imageTintColor = if (imageTintColor != imageTintColorDefault) imageTintColor.toArgb() else null,
            background = AppWidgetConfiguration.AppWidgetBackground(
                backgroundColor = if(backgroundColor != backgroundColorDefault) backgroundColor.toArgb() else null,
                backgroundRadius = if (backgroundRadius != backgroundRadiusDefault) backgroundRadius else null,
                backgroundImageUrl = customBackgroundImageUrl.takeIf { it.isNotEmpty() }
            )
        )

        if (showUser) {
            if (bindUser == null) {
                return null
            }
            return AppWidgetBinding(
                appWidgetId = widgetId,
                userEntityMid = bindUser!!.userEntity.mid,
                dataType = dataType,
                remoteViewsClassName = remoteViewsClassName,
                configuration = configuration
            )
        }

        if (showGameRole) {
            if (bindGameRole == null) {
                return null
            }
            return AppWidgetBinding(
                appWidgetId = widgetId,
                userEntityMid = bindUser!!.userEntity.mid,
                dataType = dataType,
                remoteViewsClassName = remoteViewsClassName,
                configuration = configuration.copy(
                    bindingGameRole = bindGameRole
                )
            )
        }

        return AppWidgetBinding(
            appWidgetId = widgetId,
            userEntityMid = "",
            dataType = dataType,
            remoteViewsClassName = remoteViewsClassName,
            configuration = configuration
        )
    }

    //传入一个远端视图信息并根据内容设置
    fun setValueForRemoteViewsInfo(remoteViewsInfo: RemoteViewsInfo) {
        remoteViewsClassName = remoteViewsInfo.remoteViewsClass.name
        configurationOptions = remoteViewsInfo.configurationOptions

        remoteViewsName = remoteViewsInfo.remoteViewsName
        remoteViewsDesc = remoteViewsInfo.description
        remoteViewsSize = remoteViewsInfo.size

        previewComponent =
            RemoteViewsPreviewHelper.getPreviewByRemoteViewsClassName(remoteViewsClassName)

        dataType.addAll(remoteViewsInfo.dataType)
    }

    //通过另一个配置进行数据设置
    fun setValueForConfiguration(configuration: AppWidgetConfiguration) {
        textColor = configuration.textColor?.parseColor() ?: textColorDefault
        imageTintColor = configuration.imageTintColor?.parseColor() ?: imageTintColorDefault
        backgroundRadius = configuration.background?.backgroundRadius ?: backgroundRadiusDefault
        backgroundColor = configuration.background?.backgroundColor?.parseColor() ?: backgroundColorDefault

        bindGameRole = configuration.bindingGameRole
    }
}