package com.lianyi.paimonsnotebook.common.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class CrashScreen : BaseActivity() {

    //错误信息
    private val stackTrace by lazy {
        CustomActivityOnCrash.getStackTraceFromIntent(intent) ?: "没有找到错误信息"
    }

    private val config by lazy {
        CustomActivityOnCrash.getConfigFromIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerPressedCallback()

        setContent {
            PaimonsNotebookTheme {
                ContentSpacerLazyColumn(
                    modifier = Modifier
                        .background(BackGroundColor)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.size(160.dp, 240.dp)) {
                                ErrorPlaceholder("程序崩溃啦！") {}
                            }

                            if (config != null) {
                                Button(onClick = {
                                    CustomActivityOnCrash.restartApplication(
                                        this@CrashScreen,
                                        config!!
                                    )
                                }) {
                                    Text(text = "重启程序")
                                }
                            }

                            Button(onClick = {
                                SystemService.setClipBoardText(stackTrace)
                            }) {
                                Text(text = "复制错误信息至剪切板")
                            }

                            Text(text = stackTrace)
                        }
                    }

                }
            }
        }
    }
}