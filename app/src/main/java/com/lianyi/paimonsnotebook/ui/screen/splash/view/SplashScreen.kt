package com.lianyi.paimonsnotebook.ui.screen.splash.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.ui.screen.develop.TypographyScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.HomeScreen
import com.lianyi.paimonsnotebook.ui.screen.splash.components.EnableMetadataHint
import com.lianyi.paimonsnotebook.ui.screen.splash.viewmodel.SplashScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseActivity(false) {

    val viewModel by lazy {
        ViewModelProvider(this)[SplashScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO 用户协议
//        lifecycleScope.launch {
//            val userAgree = datastorePf.data.first()[PreferenceKeys.AgreeUserAgreement] ?: false
//            if (!userAgree) {
//                HomeHelper.goActivity(
//                    GuideScreen::class.java,
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                )
//            }
//        }

        viewModel.initParam {
            //禁用元数据时,直接进入主界面
            println("viewModel.enableMetadata = ${viewModel.enableMetadata}")
            if (!viewModel.enableMetadata) {
                goTargetScreen()
            }

            //启用元数据时
            //判断第一次下载是否完成,未完成则继续下载
            //已完成进入主界面
            if (viewModel.enableMetadata && viewModel.initialMetadataDownload) {
                goTargetScreen()
            } else {
                downloadMetadata()
            }
        }

        setContent {
            PaimonsNotebookTheme {
                Crossfade(targetState = viewModel.showEnableMetadataHint, label = "") {
                    if (it) {
                        EnableMetadataHint(
                            onCountDownEnd = this::downloadMetadata,
                            skipDownloadMetadata = this::skipDownload,
                            downloadMetadata = this::downloadMetadata
                        )
                    }
                }

                Crossfade(targetState = viewModel.showLoading, label = "") {
                    if (it) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(BackGroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ContentLoadingPlaceholder(text = "正在下载所需的元数据...\n${viewModel.currentMetadataLoadCount}/${viewModel.maxMetadataCount}")
                        }
                    }
                }
            }
        }
    }

    private fun goTargetScreen() {
        goHomeScreen()
//            goDebugPage()
    }

    private fun downloadMetadata() {
        viewModel.metadataDownloadInit {
            goTargetScreen()
        }
    }

    private fun skipDownload() {
        viewModel.onSkipMetadataDownload {
            goTargetScreen()
        }
    }

    private fun goHomeScreen() {
        viewModel.disabledOnLaunchShowMetadataHint {

            HomeHelper.goActivityByIntentNewTask {
                setComponentName(HomeScreen::class.java)
            }
            finish()
        }
    }

    private fun goDebugPage() {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(TypographyScreen::class.java)
        }
        finish()
    }

}