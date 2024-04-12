package com.lianyi.paimonsnotebook.ui.screen.develop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

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
        Box(modifier = Modifier.fillMaxWidth()) {

//            AndroidView(factory = {
//                GT3GeetestButton(it)
//            }){
//
//            }

        }
    }

}

