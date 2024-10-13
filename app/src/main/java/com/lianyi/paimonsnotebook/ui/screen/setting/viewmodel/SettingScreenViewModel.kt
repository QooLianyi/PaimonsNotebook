package com.lianyi.paimonsnotebook.ui.screen.setting.viewmodel

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.enums.DownloadState
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.HomeDrawerManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.view.ResourceManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.setting.components.dialog.ApplicationUpdateDialog
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.screen.setting.data.ConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.screen.setting.util.SettingsHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.util.UpdateService
import com.lianyi.paimonsnotebook.ui.screen.setting.util.enums.HomeScreenDisplayState
import com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.view.ShortcutsManagerScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingScreenViewModel : ViewModel() {
    private var configurationData by mutableStateOf(ConfigurationData())

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    lateinit var requestInstallPermission: () -> Unit
    lateinit var checkInstallPermission: () -> Boolean

    init {
        viewModelScope.launch {
            SettingsHelper.configurationFlow.collect {
                configurationData = it
            }
        }
    }

    private val updateService by lazy {
        UpdateService()
    }

    private var downloadJob: Job? = null

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
        ),
        OptionListData(
            name = "首页侧边栏功能管理",
            description = "管理首页侧边栏中显示的功能,可以禁用不使用的功能,使其不显示在侧边栏中",
            onClick = {
                HomeHelper.goActivity(HomeDrawerManagerScreen::class.java)
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
            name = "快捷方式管理",
            description = "将常用的功能配置于快捷方式列表(shortcuts)中,在桌面长按派蒙笔记本图标唤起快捷方式列表",
            onClick = {
                HomeHelper.goActivity(ShortcutsManagerScreen::class.java)
            },
            slot = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        ),
//        OptionListData(
//            name = "启用网页顶部边距",
//            description = "默认开启,开启后当打开网页时,会在网页的顶部添加一个边距,防止内容显示在状态栏下,当网页顶部边距太大时,关闭此选项重新进入网页使边距失效",
//            onClick = {
//            },
//            slot = {
//                SettingsOptionSwitch(
//                    checked = configurationData.homeScreenDisplayState == HomeScreenDisplayState.Community
//                )
//            }
//        ),
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
                    PreferenceKeys.EnableAutoCleanExpiredImages.editValue(!configurationData.enableAutoCleanExpiredImages)
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
                clearBrokenImage()
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
                    PreferenceKeys.AlwaysUseDefaultUser.editValue(!configurationData.alwaysUseDefaultUser)
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
            description = "连接互联网并同步本地存储的角色、武器等数据;当完成下载后将启用元数据",
            onClick = {
                viewModelScope.launchIO {
                    "正在检查元数据的信息...".notify()

                    MetadataHelper.checkAndUpdateMetadata(true) {
                        PreferenceKeys.EnableMetadata.editValue(true)
                    }
                }
            }
        ),

//        OptionListData(
//            name = "数据迁移(占位)",
//            description = "将派蒙笔记本当前的设置与数据导出,以便迁移到其它设备",
//            onClick = {
//                "将在不久后的更新中实现".notify()
//            }
//        )
    )

    private var confirmResetConfigDialog by mutableStateOf(false)

    val othersSettings = listOf(
        OptionListData(
            name = "启动时检查更新",
            description = "默认开启,开启后每次启动时都会进行一次新版本检查",
            onClick = {
                viewModelScope.launchIO {
                    PreferenceKeys.EnableCheckNewVersion.editValue(!configurationData.enableCheckNewVersion)
                }
            },
            slot = {
                SettingsOptionSwitch(
                    checked = configurationData.enableCheckNewVersion
                )
            }
        ),
        OptionListData(
            name = "恢复默认设置",
            description = "恢复默认设置后,所有的设置都会被设置成最初的样子(仅当前页面)",
            onClick = {
                confirmResetConfigDialog = true
            },
            slot = {
                if (confirmResetConfigDialog) {
                    ConfirmDialog(
                        title = "重置设置",
                        content = "确认要重置设置中所有的选项吗?(程序整体的设置,如主页状态、自动清除过期图片、自动检查新版本等)",
                        onConfirm = {
                            viewModelScope.launchIO {
                                ConfigurationData.resetConfig()
                                "设置中的选项已重置".warnNotify(false)
                                confirmResetConfigDialog = false
                            }
                        }) {
                        confirmResetConfigDialog = false
                    }
                }
            }
        )
    )

    //是否显示下载对话框
    private var showDownloadDialog by mutableStateOf(false)

    //下载进度
    private var downloadProgress by mutableFloatStateOf(0f)

    //下载状态
    private var downloadState by mutableStateOf(DownloadState.Empty)

    val about = listOf(
        OptionListData(
            name = "派蒙笔记本",
            description = "一款开源、免费、无广告的原神游戏工具",
            onClick = {
                viewModelScope.launchIO {
                    "正在检查更新".notify()

                    updateService.checkNewVersion(onFoundNewVersion = {
                        "发现新版本".notify()
                        //将下载状态设置为未开始
                        downloadState = DownloadState.UnStart
                        showDownloadDialog = true
                    }, onFail = {
                        "检查新版本时发生错误,但依然可以通过项目仓库找到最新的发布版本".errorNotify()
                    }, onNotFoundNewVersion = {
                        "当前版本已是最新".notify()
                    })
                }
            },
            slot = {
                InfoText(text = PaimonsNotebookApplication.version,
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

                if (showDownloadDialog) {
                    ApplicationUpdateDialog(
                        downloadState = downloadState,
                        downloadProgress = downloadProgress,
                        requestInstallPermission = requestInstallPermission,
                        checkInstallPermission = checkInstallPermission,
                        onStartDownload = { endpointName ->
                            //清空下载进度
                            downloadProgress = 0f
                            downloadState = DownloadState.Downloading

                            downloadJob = viewModelScope.launchIO {
                                updateService.downloadNewVersionPackage(
                                    remoteEndpointName = endpointName,
                                    onSuccess = {
                                        downloadState = DownloadState.Success
                                    },
                                    onFail = {
                                        downloadState = DownloadState.Error
                                    },
                                    onProgress = {
                                        downloadProgress = it
                                    }
                                )
                            }
                        },
                        onInstall = {
                            SystemService.installAndroidApplication(updateService.newVersionPackage)
                        }, onDismissRequest = {
                            onUpdateDialogDismissRequest()
                        })
                }
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
            description = "通过QQ群与开发者进行问题反馈、新功能建议等(遇到BUG不反馈的话开发者是没办法修复的),新版本也会第一时间发布到这里",
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
        ),
//        OptionListData(
//            name = "使用到的第三方库",
//            description = "程序在开发过程中使用到的第三方库(),非常感谢这些组织/个人!",
//            onClick = {
//
//            }
//        ),
    )

    private fun clearBrokenImage() {
        viewModelScope.launchIO {
            "开始清理失效图片".notify()

            val dao = PaimonsNotebookDatabase.database.diskCacheDao
            var count = 0
            val list = dao.getAllData().first()
            val deleteUrls = mutableListOf<String>()

            //设置为只检查图片的像素尺寸
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            list.forEach {
                val file = PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)

                val fileAbsolutePath = file?.absolutePath ?: ""

                BitmapFactory.decodeFile(fileAbsolutePath, options)

                /*
                        * 图片路径为空表明这个图片不存在
                        * 如果编码的结果等于-1表示解析时出现错误,表明这个图片损坏了
                        * */
                if (fileAbsolutePath.isEmpty() || options.outWidth == -1 && options.outHeight == -1) {
                    PaimonsNotebookImageLoader.getCacheImageFileByUrl(it.url)?.delete()
                    PaimonsNotebookImageLoader.getCacheImageMetadataFileByUrl(it.url)
                        ?.delete()

                    deleteUrls += it.url

                    count++
                }
            }

            dao.deleteByUrls(deleteUrls)

            "在全部的${list.size}张图片中,清理了${count}张失效图片".warnNotify()
        }
    }


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


    private fun onUpdateDialogDismissRequest() {
        showDownloadDialog = false

        downloadJob?.cancel()
    }

    private var c = 0
}