package com.lianyi.paimonsnotebook.ui.screen.geetest.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.geetest.viewmodel.GeeTestScreenViewModel

class GeeTestScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[GeeTestScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent) {
            finish()
        }


    }
}