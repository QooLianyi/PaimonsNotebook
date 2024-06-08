package com.lianyi.paimonsnotebook.ui.screen.setting.components.dialog

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.util.enums.DownloadState
import com.lianyi.paimonsnotebook.ui.screen.setting.util.UpdateService
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.ElementGrassColor
import com.lianyi.paimonsnotebook.ui.theme.Primary_9


@Composable
fun ApplicationUpdateDialog(
    downloadState: DownloadState,
    downloadProgress: Float,
    requestInstallPermission: () -> Unit,
    checkInstallPermission: () -> Boolean,
    onStartDownload: (String) -> Unit,
    onInstall: () -> Unit,
    onDismissRequest: () -> Unit
) {
    LazyColumnDialog(
        title = "版本更新",
        onDismissRequest = { },
        titleTextSize = 18.sp,
        buttons =
        when (downloadState) {
            DownloadState.Success -> arrayOf("关闭", "立即安装")
            else -> arrayOf("关闭")
        },
        onClickButton = { index ->
            if (index == 0) {
                onDismissRequest.invoke()
            }
            if (index == 1) {
                when (downloadState) {
                    DownloadState.Success -> {
                        if (!checkInstallPermission.invoke()) {
                            "缺少安装权限,请给予安装权限后再次尝试安装".show()
                            requestInstallPermission.invoke()
                        } else {
                            onInstall.invoke()
                        }
                    }

                    else -> {}
                }
            }
        },
        content = {
            item {
                Crossfade(targetState = downloadState, label = "") { state ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(
                                id =
                                when (state) {
                                    DownloadState.Success -> R.drawable.emotion_icon_keqing_sleeping
                                    DownloadState.Downloading -> R.drawable.emotion_icon_nahida_drink
                                    DownloadState.UnStart -> R.drawable.emotion_icon_nahida_thinking
                                    else -> R.drawable.emotion_icon_paimon_error
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )

                        when (state) {
                            DownloadState.Success -> {
                                Text(
                                    text = "下载完成,现在要进行安装吗?",
                                    modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
                                    color = Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            DownloadState.UnStart -> {
                                Text(
                                    text = "发现新版本!请选择一个下载的站点",
                                    modifier = Modifier,
                                    color = Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    UpdateService.updateRemoteEndpoints.forEach {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .radius(2.dp)
                                                .fillMaxWidth()
                                                .background(Primary_9)
                                                .clickable {
                                                    onStartDownload.invoke(it)
                                                }
                                                .padding(8.dp, 6.dp),
                                            color = Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            DownloadState.Downloading -> {
                                Text(
                                    text = "正在下载安装包...",
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    LinearProgressIndicator(
                                        progress = downloadProgress,
                                        color = ElementGrassColor,
                                        strokeCap = StrokeCap.Round,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        text = "${"%.2f".format(downloadProgress * 100)}%",
                                        color = Black,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.width(42.dp)
                                    )
                                }
                            }

                            else -> {
                                Text(
                                    text = "下载过程中出现了错误",
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}