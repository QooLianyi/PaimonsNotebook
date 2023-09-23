package com.lianyi.paimonsnotebook.ui.screen.account.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.ui.screen.account.viewmodel.LoginViewModel
import com.lianyi.paimonsnotebook.ui.theme.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.components.widget.WebView


class LoginScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()

        setContent {
            PaimonsNotebookTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    Surface(Modifier
                        .fillMaxWidth(.95f)
                        .shadow(3.dp, RoundedCornerShape(5.dp))
                        .clip(RoundedCornerShape(5.dp))) {

                        Column(modifier = Modifier
                            .padding(20.dp)
                        ) {
                            TitleText(text = "设置米游社Cookie", fontSize = 20.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            InputTextFiled(value = viewModel.cookieInput,
                                onValueChange = viewModel::cookieInput::set)

                            Spacer(modifier = Modifier.height(30.dp))
//
//                            IconHintText(R.drawable.ic_web, "自动获取", "登录米游社账号后自动获取Cookie") {
//                                viewModel.checkWebViewCookieValue(this@LoginScreen)
//                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(onClick = {
                                viewModel.checkInputCookieValue(this@LoginScreen)
                            }, modifier = Modifier.align(Alignment.End)) {
                                Text(text = "检查Cookie")
                            }
                        }
                    }


                    AnimatedVisibility(visible = viewModel.accountLogin,
                        enter = slideInHorizontally { it },
                        exit = slideOutHorizontally { it }
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(White)
                        ) {
                            WebView(url = viewModel.WEB_URL)

                            Column(Modifier
                                .align(Alignment.BottomStart)
                                .padding(10.dp)
                            ) {
//                                IconHintText(R.drawable.ic_cookie,
//                                    "Cookie登录",
//                                    "输入Cookie后完成登录") {
//                                    viewModel.accountLogin = false
//                                }
                                NavigationPaddingSpacer()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initData() {
        viewModel.addAccount = intent.getBooleanExtra("addAccount", false)
    }

    override fun onBackPressed() {
        if (viewModel.accountLogin) {
            viewModel.accountLogin = false
        } else {
            super.onBackPressed()
        }
    }
}