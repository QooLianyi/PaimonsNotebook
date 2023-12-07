package com.lianyi.paimonsnotebook.ui.screen.setting.viewmodel

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.view.ResourceManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.setting.components.static_resources.StaticResourcesChannelDialogItem
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.screen.setting.data.ConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.screen.setting.data.StaticResourcesChannelData
import com.lianyi.paimonsnotebook.ui.screen.setting.util.SettingsHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.util.configuration_enum.HomeScreenDisplayState
import com.lianyi.paimonsnotebook.ui.theme.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingScreenViewModel : ViewModel() {
    private var configurationData by mutableStateOf(ConfigurationData())

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    init {
        viewModelScope.launch {
            SettingsHelper.configurationFlow.collect {
                configurationData = it
            }
        }
    }

    val settings = listOf(
        OptionListData(
            name = "启用社区主页",
            description = "勾选后开启社区主页,社区主页有更丰富的多媒体表示方式,第二种主页正在开发中",
            onClick = {
                setHomeScreenDisplayState(configurationData.homeScreenDisplayState)
            },
            slot = {
                SettingsOptionSwitch(
                    checked = configurationData.homeScreenDisplayState == HomeScreenDisplayState.Community
                )
            }
        )
    )

    val storageSettings = listOf(
        OptionListData(
            name = "图片缓存管理",
            description = "管理程序在使用过程中加载的各种网络图片",
            onClick = {
                HomeHelper.goActivity(ResourceManagerScreen::class.java)
            },
            slot = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        ),
        OptionListData(
            name = "自动清除过期图片缓存",
            description = "默认开启,开启后,程序在启动时会将最后一次使用时间为7天以上,并且图片类型为临时的图片删除,以节省存储空间",
            onClick = {
                viewModelScope.launchIO {
                    configurationData.enableAutoCleanExpiredImages =
                        !configurationData.enableAutoCleanExpiredImages

                    PreferenceKeys.EnableAutoCleanExpiredImages.editValue(configurationData.enableAutoCleanExpiredImages)
                }
            },
            slot = {
                SettingsOptionSwitch(checked = configurationData.enableAutoCleanExpiredImages)
            }
        ),
        OptionListData(
            name = "清除失效图片",
            description = "此操作会立即清除所有无法显示的图片;当图片加载异常时,进行此操作后重新载入图片可能会得到改善",
            onClick = {
                viewModelScope.launchIO {
                    val dao = PaimonsNotebookDatabase.database.diskCacheDao
                    var c = 0
                    dao.getAllData().first().forEach {
                        val file = PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)
                        val bitmap = BitmapFactory.decodeFile(file?.absolutePath ?: "")
                        if (bitmap == null) {
                            PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)?.delete()
                            PaimonsNotebookImageLoader.getCacheImageMetadataFileByUrl(it.url)
                                ?.delete()
                            dao.deleteByUrl(it.url)
                            c++
                        }
                    }

                    "清理了${c}张失效图片".warnNotify(false)
                }
            },
            slot = {
            }
        )
    )

    val userSettings = listOf(
        OptionListData(
            name = "始终使用默认用户",
            description = "默认开启,开启后,当进行一些操作需要指定使用的用户时,直接使用默认用户。(如签到、扫码、桌面组件默认绑定的用户、角色)",
            onClick = {
                viewModelScope.launch(Dispatchers.IO) {
                    configurationData.alwaysUseDefaultUser = !configurationData.alwaysUseDefaultUser
                    PreferenceKeys.AlwaysUseDefaultUser.editValue(configurationData.alwaysUseDefaultUser)
                }
            },
            slot = {
                SettingsOptionSwitch(
                    checked = configurationData.alwaysUseDefaultUser
                )
            }
        )
    )

    val dataSettings = listOf(
        OptionListData(
            name = "同步元数据",
            description = "连接互联网并同步本地存储的角色、武器等数据",
            onClick = {
                "正在检查更新...".notify()
                viewModelScope.launch(Dispatchers.IO) {
                    MetadataHelper.updateMetadata(
                        onSuccess = {
                            "已更新到最新的元数据".notify()
                        },
                        onFailed = {
                            "更新元数据时出现错误".errorNotify()
                        }
                    ) {}
                }
            },
            slot = {

            }
        )
    )

    val othersSettings = listOf(
        OptionListData(
            name = "刷新全部账号信息",
            description = "刷新当前本地所有账号的信息",
            onClick = {
                HomeHelper.goActivity(ResourceManagerScreen::class.java)
            },
            slot = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    )

    val about = listOf(
        OptionListData(
            name = "派蒙笔记本",
            description = "一款开源、免费、无广告的游戏工具",
            onClick = {
                "检查更新中...很可惜当前版本还没有这个功能:P".notify()
            },
            slot = {
                Text(
                    text = PaimonsNotebookApplication.version,
                    color = Info,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                if (c == 8 && dragAmount.y > 10) {
                                    "下次启动时会开启调试面板".show()

                                    PaimonsNotebookApplication.context
                                        .getExternalFilesDir(null)
                                        ?.resolve("debug")
                                        ?.mkdirs()

                                    viewModelScope.launch {
                                        PreferenceKeys.EnableOverlay.editValue(true)
                                    }
                                    c++
                                }
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                c++
                            })
                        })
            }
        ),
        OptionListData(
            name = "Github上的派蒙笔记本",
            description = "在此你能够找到派蒙笔记本的源代码,遇到问题也欢迎issue",
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(PaimonsNotebookApplication.githubUrl)
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    "唤起浏览器失败,但你可以通过手动输入以下网址找到本项目的仓库:${PaimonsNotebookApplication.githubUrl}".errorNotify()
                }
            }
        ),
        OptionListData(
            name = "派蒙笔记本QQ群",
            description = "通过QQ群与开发者进行问题反馈、新功能建议等(遇到BUG不反馈的话开发者是没办法修复的)",
            onClick = {
                "正在尝试唤起手机QQ".show()
                val intent = Intent().apply {
                    data =
                        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D${PaimonsNotebookApplication.qqGroupKey}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    "唤起手机QQ失败:可能是未安装手Q或安装的版本不支持,但你可以通过以下qq群号进行添加:584804229".errorNotify()
                }
            }
        )
    )

    //切换展示方式
    private fun setHomeScreenDisplayState(value: HomeScreenDisplayState) {
        val state = when (value) {
            HomeScreenDisplayState.Simple -> HomeScreenDisplayState.Community
            HomeScreenDisplayState.Community -> HomeScreenDisplayState.Simple
            else -> HomeScreenDisplayState.Simple
        }

        configurationData.homeScreenDisplayState = state

        viewModelScope.launch {
            PreferenceKeys.HomeScreenDisplayState.editValue(state.name)
        }
    }

    private var c = 0
}