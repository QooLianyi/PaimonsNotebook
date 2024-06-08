package com.lianyi.paimonsnotebook.ui.screen.account.components

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.popup.BasePopup
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.text.InfoText
import com.lianyi.paimonsnotebook.common.components.widget.Switch
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.system_service.sdkVersionLessThanOrEqualTo29
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOption
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Font_Primary
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun QRCodeLoginPopup(
    visible: Boolean,
    bitmap: Bitmap?,
    requestStoragePermission: () -> Unit,
    onRequestDismiss: () -> Unit,
    goLoginPage: (Boolean) -> Unit
) {
    //保存二维码到本地
    var saveQRCodeToLocal by remember {
        mutableStateOf(FileHelper.hasWriteExternalStorage)
    }

    BasePopup(
        visible = visible,
        onRequestDismiss = {},
        backgroundColor = BackGroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(16.dp, 8.dp)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                StatusBarPaddingSpacer()

                Crossfade(
                    modifier = Modifier
                        .padding(36.dp)
                        .size(180.dp), targetState = bitmap == null, label = ""
                ) {
                    if (it) {
                        Image(
                            painter = painterResource(id = R.drawable.emotion_icon_nahida_drink),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            model = bitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                InfoText(text = "使用米游社扫码并确认登录游戏以获取Cookie")
                InfoText(text = "* 实际上并不会登录/注册该游戏")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SettingOption(
                    title = "将二维码保存至本地",
                    description = "将登录二维码保存至本地,跳转米游社扫码后可以在相册中找到二维码,保存的二维码将在下次启动程序时删除。使用此功能需要给予外部存储权限。",
                    onClick = {
                        sdkVersionLessThanOrEqualTo29(
                            finally = {
                                saveQRCodeToLocal = !saveQRCodeToLocal
                            }
                        ) {
                            if (!FileHelper.hasReadExternalStorage) {
                                requestStoragePermission.invoke()
                                saveQRCodeToLocal = false
                                return@sdkVersionLessThanOrEqualTo29
                            }
                        }
                    }
                ) {
                    Switch(checked = saveQRCodeToLocal)
                }

                TextButton(text = "跳转米游社扫码") {
                    goLoginPage.invoke(saveQRCodeToLocal)
                }

                TextButton(
                    text = "取消登录",
                    textColor = Font_Primary,
                    backgroundColor = Transparent,
                    onClick = onRequestDismiss
                )

                NavigationBarPaddingSpacer()
            }
        }
    }
}