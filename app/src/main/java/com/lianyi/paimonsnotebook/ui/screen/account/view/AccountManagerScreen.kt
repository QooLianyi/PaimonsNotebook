package com.lianyi.paimonsnotebook.ui.screen.account.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.widget.IconButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingStart
import com.lianyi.paimonsnotebook.common.extension.modifier.state.isScrollToEnd
import com.lianyi.paimonsnotebook.ui.screen.account.components.AccountItem
import com.lianyi.paimonsnotebook.ui.screen.account.components.QRCodeLoginPopup
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.CookieInputDialog
import com.lianyi.paimonsnotebook.ui.screen.account.viewmodel.AccountManagerScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AccountManagerScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AccountManagerScreenViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed {
                    finish()
                }
            }
        })

        viewModel.checkStoragePermission = this::checkStoragePermission
        viewModel.requestStoragePermission = this::requestStoragePermission

        setContent {
            PaimonsNotebookTheme(this) {
                val state = rememberLazyListState()
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ) {

                    ContentSpacerLazyColumn(state = state, modifier = Modifier.fillMaxSize()) {
                        item {
                            PrimaryText(
                                text = "账号列表", fontSize = 20.sp, modifier = Modifier
                                    .paddingStart(8.dp)
                                    .padding(8.dp)
                            )
                        }

                        items(viewModel.userList, { it.userEntity.mid }) { user ->
                            AccountItem(
                                modifier = Modifier
                                    .background(BackGroundColor)
                                    .animateItemPlacement(),
                                user = user,
                                onSetDefault = viewModel::switchUserSelectState,
                                onDelete = viewModel::deleteUser,
                                onCopyCookie = viewModel::copyCookie,
                                onChangeAccountPlayerUid = viewModel::changeUserChosePlayer,
                                onRefreshCookie = viewModel::showConfirmDialog,
                                diskCache = viewModel.getUserAvatarDiskCacheData(user)
                            )
                        }
                    }

                    //添加账号按钮动画
                    val anim = remember {
                        Animatable(1f)
                    }
                    val isScrollToEnd by remember {
                        derivedStateOf {
                            state.isScrollToEnd
                        }
                    }

                    LaunchedEffect(isScrollToEnd, anim.isRunning) {
                        if (isScrollToEnd && anim.value != 0f && ((AccountHelper.userListFlow.value.size * 66.dp.value) > maxHeight.value)) {
                            anim.animateTo(0f, tween(200))
                        } else if (!isScrollToEnd && anim.value != 1f) {
                            anim.snapTo(.8f)
                            anim.animateTo(1f, spring(.3f))
                        }
                    }

                    //下拉框与添加按钮
                    Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.End)
                        ) {
                            DropdownMenu(expanded = viewModel.showMenu, onDismissRequest = {
                                viewModel.dismissMenu()
                            }, properties = PopupProperties(focusable = false)) {
                                DropdownMenuItem(onClick = {
                                    viewModel.dismissMenu()
                                    viewModel.showCookieInputDialog()
                                }) {
                                    PrimaryText(text = "通过Cookie", fontSize = 16.sp)
                                }
                                DropdownMenuItem(onClick = {
                                    viewModel.dismissMenu()
                                    viewModel.showQRCodePopup()
                                }) {
                                    PrimaryText(text = "通过米游社扫码", fontSize = 16.sp)
                                }
                            }

                            IconButton(
                                modifier = Modifier
                                    .padding(0.dp, 10.dp, 20.dp, 20.dp)
                                    .scale(anim.value),
                                text = "添加账号",
                                icon = painterResource(id = R.drawable.ic_add),
                                block = viewModel::showMenu
                            )
                        }

                        NavigationBarPaddingSpacer()
                    }

                    //通过cookie添加账号对话框
                    if (viewModel.showAddAccountByCookieDialog) {
                        CookieInputDialog(
                            inputValue = viewModel.cookieInputValue,
                            viewModel::onInputTextValueChange,
                            helperTextHints = viewModel.helperTextHints,
                            onDismissRequest = { viewModel.dismissCookieInputDialog() },
                            onSuccess = viewModel::checkCookie
                        )
                    }

                    //确认对话框
                    if (viewModel.showConfirmDialog) {
                        ConfirmDialog(
                            content = "接下来将要刷新Cookie,你确定要这样做吗?",
                            onConfirm = {
                                viewModel.confirmRefreshDialog()
                            },
                            onCancel = viewModel::dismissConfirmDialog
                        )
                    }

                    QRCodeLoginPopup(
                        visible = viewModel.showQRCodePopup,
                        bitmap = viewModel.loginQrCodeBitmap,
                        requestStoragePermission = this@AccountManagerScreen::requestStoragePermission,
                        onRequestDismiss = viewModel::onRequestQRCodePopupDismiss,
                        goLoginPage = viewModel::goHoyolabSelfPage
                    )

                    if (viewModel.showLoadingDialog) {
                        LoadingDialog()
                    }
                }
            }
        }
    }
}