package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.helper_text.view.HelperText
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingTop
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.notification.NotificationHelper
import com.lianyi.paimonsnotebook.common.util.enums.HelperTextStatus
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotification
import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotificationType
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.ui.screen.develop.viewmodel.DebugPanelViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import kotlin.system.measureTimeMillis

class DebugPanel : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[DebugPanelViewModel::class.java]
    }

    private val dsSaltType = buildMap {
        put(DynamicSecret.SaltType.PROD, DynamicSecret.PROD)
        put(DynamicSecret.SaltType.K2, DynamicSecret.K2)
        put(DynamicSecret.SaltType.LK2, DynamicSecret.LK2)
        put(DynamicSecret.SaltType.X4, DynamicSecret.X4)
        put(DynamicSecret.SaltType.X6, DynamicSecret.X6)
    }
    private val dsVersion = buildMap {
        put(DynamicSecret.Version.Gen1, "Gen1")
        put(DynamicSecret.Version.Gen2, "Gen2")
    }

    override fun onStop() {

        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        lifecycleScope.launch {
//            AccountManager.defaultAccount?.let {
//                val gameAuthKeyData = BindingClient().generateAuthenticationKey(it)
//                val genAuthKey =
//                    GenAuthKeyData.createForWebViewGacha(AccountManager.defaultAccount!!.playerUid)
//                val gachaQueryConfig = GachaQueryConfigData(GachaType.AvatarEventWish,
//                    gameAuthKeyData.data,
//                    genAuthKey)
//
//                val result = GachaInfoClient().getGachaLogPage(gachaQueryConfig)
//                println("result = ${result}")
//
//                viewModel.viewModelScope.launch(Dispatchers.IO) {
//                    PaimonsNotebookDatabase.database.gachaLogItemDao.apply {
//                        insert(*result.data.list.toTypedArray())
//
//                        getAll().collect{
//                            println("it = $it")
//                        }
//
//                    }
//                }
//            }
//        }

        setContent {
            PaimonsNotebookTheme {
                val lifecycleOwner = LocalLifecycleOwner.current
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp)
                        .paddingTop(20.dp)
                ) {
                    InputTextFiled(
                        value = viewModel.output,
                        onValueChange = viewModel::output::set,
                        inputFieldHeight = 100.dp
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        HelperText(
                            text = "Version = ${viewModel.dsVersion} , Type = ${viewModel.dsSaltType}",
                            status = HelperTextStatus.Available
                        )
                        Button(onClick = {
                            SystemService.setClipBoardText(viewModel.output)
                            println(viewModel.output)
                        }) {
                            Text(text = "Copy")
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TitleText(text = "q:")
                        Spacer(modifier = Modifier.width(10.dp))
                        InputTextFiled(
                            value = viewModel.query,
                            onValueChange = viewModel::query::set
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TitleText(text = "b:")
                        Spacer(modifier = Modifier.width(10.dp))
                        InputTextFiled(value = viewModel.body, onValueChange = viewModel::body::set)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.includeChars,
                            onCheckedChange = viewModel::includeChars::set
                        )
                        TitleText(text = "includeChars")
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Button(onClick = {
                            viewModel.apply {
                                output = DynamicSecret.getDynamicSecret(dsVersion,
                                    dsSaltType,
                                    includeChars,
                                    query.split("&").sortedBy { it }
                                        .joinToString(separator = "&") { it },
                                    body
                                )
                                println(output)
                            }
                        }) {
                            TitleText(text = "生成DS")
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box {
                            Button(onClick = {
                                viewModel.showSelectDsType = true
                            }) {
                                Text(text = "DS类型")
                            }

                            DropdownMenu(expanded = viewModel.showSelectDsType,
                                onDismissRequest = { viewModel.showSelectDsType = false }) {
                                dsSaltType.forEach { (k, _) ->
                                    DropdownMenuItem(onClick = {
                                        viewModel.dsSaltType = k
                                        viewModel.showSelectDsType = false
                                    }) {
                                        TitleText(text = k.toString(), fontSize = 20.sp)
                                    }
                                }
                            }

                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box {
                            Button(onClick = {
                                viewModel.showSelectDsVersion = true
                            }) {
                                Text(text = "DS版本")
                            }

                            DropdownMenu(expanded = viewModel.showSelectDsVersion,
                                onDismissRequest = { viewModel.showSelectDsVersion = false }) {
                                dsVersion.forEach { (k, _) ->
                                    DropdownMenuItem(onClick = {
                                        viewModel.dsVersion = k
                                        viewModel.showSelectDsVersion = false
                                    }) {
                                        TitleText(text = k.toString(), fontSize = 20.sp)
                                    }
                                }
                            }

                        }
                    }

                    Row {
                        Button(onClick = {
                            viewModel.geeTestV6()
                        }) {
                            TitleText(text = "通过默认用户开启一个校验流程,未完成")
                        }
                    }
                    Row {
                        Button(onClick = {
                        }) {
                            TitleText(text = "创建一个悬浮窗")
                        }
                    }

                    TitleText(text = viewModel.hoyolabDeviceId, fontSize = 24.sp)
                    Button(onClick = {
                        lifecycleScope.launch {
                            viewModel.hoyolabDeviceId = CoreEnvironment.DeviceId
                        }
                    }) {
                        TitleText(text = "hoyolab设备ID")
                    }

                    InputTextFiled(value = viewModel.timeStampInput, onValueChange = {
                        viewModel.timeStampInput = it
                    })

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Button(onClick = {
                            viewModel.output =
                                URLEncoder.encode(viewModel.timeStampInput, "utf-8")
                        }) {
                            TitleText(text = "parse")
                        }
                        Button(onClick = {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result =
                                    PaimonsNotebookDatabase.database.gachaItemsDao.isExist(viewModel.output)
                                "result:${result}".notify()
                            }
                        }) {
                            TitleText(text = "判断数据库是否存在")
                        }
                    }

                    Row {
                        Button(onClick = {
                            viewModel.tempInt = NotificationHelper.buildNormalNotification(
                                "标题",
                                viewModel.output,
                                autoCancel = true
                            )
                        }) {
                            TitleText(text = "发送Normal通知")
                        }
                        Button(onClick = {
                            NotificationHelper.cancelNotificationByNotificationId(viewModel.tempInt)
                        }) {
                            TitleText(text = "取消Normal通知")
                        }
                    }

                    Row {
                        Button(onClick = {
                            viewModel.tempInt =
                                NotificationHelper.buildHighImportanceNotification(
                                    "重要通知标题",
                                    viewModel.output,
                                    autoCancel = true
                                )
                        }) {
                            TitleText(text = "发送重要通知")
                        }
                        Button(onClick = {
                            NotificationHelper.cancelNotificationByNotificationId(viewModel.tempInt)
                        }) {
                            TitleText(text = "取消重要通知")
                        }
                    }

                    Row {
                        Button(onClick = {
                            val pair =
                                NotificationHelper.buildProgressNotification(
                                    "进度条通知",
                                    viewModel.output
                                )
                            lifecycleScope.launch {

                                while (viewModel.tempInt < 100) {
                                    viewModel.tempInt += 1
                                    NotificationHelper.sendNotify(pair.first, pair.second.apply {
                                        setContentText("当前进度:${viewModel.tempInt}")
                                        setProgress(100, viewModel.tempInt, false)
                                    })
                                    delay(100)
                                }

                            }

                        }) {
                            TitleText(text = "发送进度条通知")
                        }
                    }

                    Row {
                        Button(onClick = {
                            viewModel.articleNavigate()
                        }) {
                            TitleText(text = "通过ID跳转帖子详情")
                        }

                    }

                    Button(onClick = {
                        lifecycleScope.launch {
                            datastorePf.edit {
                                it.remove(PreferenceKeys.PermissionRequestFlag)
                            }
                        }
                    }) {
                        TitleText(text = "重置外部存储权限申请标识")
                    }

                    Row {
                        Button(onClick = {
                            viewModel.showDialog = true
                        }) {
                            TitleText(text = "显示对话框")
                        }
                        Button(onClick = {
                            viewModel.descDialogItemCount = viewModel.output.toIntOrNull() ?: 0
                        }) {
                            TitleText(text = "设置对话框条目个数")
                        }
                    }

                    Row {
                        Button(onClick = {
                            viewModel.genAuthKey()
                        }) {
                            TitleText(text = "使用当前默认用户生成祈愿密钥")
                        }
                        Button(onClick = {
                            viewModel.output =
                                AccountHelper.selectedUserFlow.value?.userEntity?.cookies ?: ""
                        }) {
                            TitleText(text = "默认用户cookie")
                        }
                    }

                    if (viewModel.showDialog) {
                        LazyColumnDialog(
                            title = "标题",
                            onDismissRequest = { viewModel.showDialog = false }, onClickButton = {
                                viewModel.showDialog = false
                            }) {

                            item {
                                LoadingAnimationPlaceholder(shadowDp = 0.dp)
                            }

//                            items(viewModel.descDialogItemCount) {
//                                Text(
//                                    text = "- 描述内容${it}",
//                                    modifier = Modifier
//                                        .padding(0.dp, 2.dp)
//                                        .fillMaxWidth(),
//                                    fontSize = 16.sp,
//                                    color = Color.Gray
//                                )
//                            }
                        }
                    }


                    Button(onClick = {
                    }) {
                        TitleText(text = "添加小组件至桌面")
                    }

                    Row {
                        Button(onClick = {
                            lifecycleScope.launch {
                                PaimonsNotebookNotification.add(viewModel.output)
                            }
                        }) {
                            TitleText(text = "发送内部通知")
                        }
                        Button(onClick = {
                            lifecycleScope.launch {
                                PaimonsNotebookNotification.add(
                                    viewModel.output,
                                    type = PaimonsNotebookNotificationType.Error
                                )
                            }
                        }) {
                            TitleText(text = "发送内部错误通知")
                        }
                    }
                    Row {

                        Button(onClick = {
                            lifecycleScope.launch {
                                PaimonsNotebookNotification.add(
                                    viewModel.output,
                                    type = PaimonsNotebookNotificationType.Warning
                                )
                            }
                        }) {
                            TitleText(text = "发送内部警告通知")
                        }
                        Button(onClick = {
                            lifecycleScope.launch {
                                PaimonsNotebookNotification.add(
                                    viewModel.output,
                                    closeable = true,
                                    keepShow = true
                                )
                            }
                        }) {
                            TitleText(text = "发送内部可关闭通知")
                        }
                    }
                    Row {

                        Button(onClick = {
                            PaimonsNotebookNotification.notifications.clear()
                        }) {
                            TitleText(text = "清除全部内部通知")
                        }
                    }
                    Row {
                        Button(onClick = {
                            lifecycleScope.launch {
                                PreferenceKeys.GachaRecordCurrentGameUid.editValue("")
                            }
                        }) {
                            TitleText(text = "置空当前祈愿账号")
                        }
                        Button(onClick = {
                            lifecycleScope.launch(Dispatchers.IO) {
                                PaimonsNotebookDatabase.database.gachaItemsDao.deleteAllGachaLogItem()
                                "删除完毕".notify()
                            }
                        }) {
                            TitleText(text = "删除所有祈愿记录")
                        }
                    }

                    if (viewModel.showLoading) {
                        LoadingDialog()
                    }

                    Button(onClick = {
                        viewModel.showLoading = true
                    }) {
                        TitleText(text = "显示加载对话框")
                    }
                    Button(onClick = {

                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            val time = measureTimeMillis {

                            }
                            println("time = ${time}")
                        }
                    }) {
                        TitleText(text = "数据库测试")
                    }


                    TitleText(text = "结尾", fontSize = 16.sp)
                }
            }
        }
    }
}
