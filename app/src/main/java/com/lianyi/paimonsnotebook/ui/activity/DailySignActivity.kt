package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityDailySignBinding

class DailySignActivity : BaseActivity() {
    lateinit var bind:ActivityDailySignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityDailySignBinding.inflate(layoutInflater)
        setContentView(bind.root)



    }
}