package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import com.geetest.sdk.GT3ConfigBean
import com.geetest.sdk.GT3ErrorBean
import com.geetest.sdk.GT3GeetestUtils
import com.geetest.sdk.GT3Listener
import com.geetest.sdk.views.GT3GeetestButton
import com.lianyi.paimonsnotebook.common.components.widget.ProgressBar
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.gacha_record.GachaRecordProgressBar
import com.lianyi.paimonsnotebook.ui.theme.GachaStar4Color
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.Transparent

class ComponentsTesting : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ComponentsTestingViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed {
                    finish()
                }
            }
        })

        viewModel.startGeeTest(this)

        setContent {
            PaimonsNotebookTheme {
                Content()
            }
        }
    }


    @Composable
    fun Content() {
        Box(modifier = Modifier.fillMaxWidth()){

            AndroidView(factory = {
                GT3GeetestButton(it)
            }){

            }

        }
    }

}

