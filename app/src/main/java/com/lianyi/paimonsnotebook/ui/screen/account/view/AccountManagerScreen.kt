package com.lianyi.paimonsnotebook.ui.screen.account.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.ViewModelProvider
import com.geetest.sdk.GT3GeetestUtils
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.dialog.InputDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.IconButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingStart
import com.lianyi.paimonsnotebook.common.extension.modifier.state.isScrollToEnd
import com.lianyi.paimonsnotebook.ui.screen.account.components.AccountItemV2
import com.lianyi.paimonsnotebook.ui.screen.account.components.QRCodeLoginPopup
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.CookieInputDialog
import com.lianyi.paimonsnotebook.ui.screen.account.components.popup.IconTextClickableItem
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

        viewModel.getGeeTestUtils = this::getGeeTestUtils

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
                                text = "账号列表", textSize = 20.sp, modifier = Modifier
                                    .paddingStart(8.dp)
                                    .padding(8.dp)
                            )
                        }

                        items(viewModel.userList, { it.userEntity.mid }) { user ->
                            AccountItemV2(
                                modifier = Modifier
                                    .background(BackGroundColor)
                                    .animateItemPlacement(),
                                user = user,
                                onSetDefault = viewModel::switchUserSelectState,
                                onDelete = viewModel::onDeleteUser,
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
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        horizontalAlignment = Alignment.End
                    ) {

                        AnimatedVisibility(visible = viewModel.showMenu) {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.End),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {

                                IconTextClickableItem(
                                    iconResId = R.drawable.ic_cookie,
                                    text = "通过Cookie",
                                    onClick = {
                                        viewModel.dismissMenu()
                                        viewModel.showCookieInputDialog()
                                    }
                                )
                                IconTextClickableItem(
                                    iconResId = R.drawable.ic_scan,
                                    text = "通过米游社扫码",
                                    onClick = {
                                        viewModel.dismissMenu()
                                        viewModel.showQRCodePopup()
                                    }
                                )
                                IconTextClickableItem(
                                    iconResId = R.drawable.ic_phone_mobile,
                                    text = "通过短信验证码",
                                    onClick = {
                                        viewModel.dismissMenu()
                                        viewModel.showPhoneNumberInputDialog()
                                    }
                                )
                            }
                        }

                        IconButton(
                            modifier = Modifier
                                .padding(0.dp,6.dp,20.dp,24.dp)
                                .scale(anim.value),
                            text = "添加账号",
                            icon = painterResource(id = R.drawable.ic_add),
                            block = viewModel::toggleMenu
                        )

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
                    if (viewModel.showRefreshCookieConfirmDialog) {
                        ConfirmDialog(
                            content = "接下来将要刷新Cookie,你确定要这样做吗?",
                            onConfirm = viewModel::confirmRefreshCookieDialog,
                            onCancel = viewModel::dismissConfirmDialog
                        )
                    }

                    if (viewModel.showConfirmDeleteUserDialog) {
                        ConfirmDialog(
                            content = "接下来将要删除用户[${viewModel.pendingActionUser?.userInfo?.nickname}],你确定要这样做吗?",
                            onConfirm = viewModel::deleteUser,
                            onCancel = viewModel::dismissConfirmDeleteUserDialog
                        )
                    }

                    QRCodeLoginPopup(
                        visible = viewModel.showQRCodePopup,
                        bitmap = viewModel.loginQrCodeBitmap,
                        requestStoragePermission = this@AccountManagerScreen::requestStoragePermission,
                        onRequestDismiss = viewModel::onRequestQRCodePopupDismiss,
                        goLoginPage = viewModel::goHoyolabSelfPage
                    )

                    if (viewModel.showPhoneNumberInputDialog) {
                        InputDialog(
                            title = "短信验证码登录",
                            placeholder = "在此输入11位手机号",
                            hint = "仅支持中国大陆的手机号(+86)",
                            textMaxLength = 11,
                            onConfirm = viewModel::createLoginCaptcha,
                            onCancel = viewModel::dismissPhoneNumberInputDialog
                        )
                    }

                    if (viewModel.showLoginCaptchaCodeInputDialog) {
                        InputDialog(
                            title = "短信验证码登录",
                            placeholder = "在此输入验证码",
                            onConfirm = viewModel::loginByMobileCaptcha,
                            onCancel = viewModel::dismissLoginCaptchaCodeInputDialog,
                            onlyNumber = true
                        )
                    }

                    if (viewModel.showLoadingDialog) {
                        LoadingDialog()
                    }
                }
            }
        }
    }

    private fun getGeeTestUtils() = GT3GeetestUtils(this)
}
