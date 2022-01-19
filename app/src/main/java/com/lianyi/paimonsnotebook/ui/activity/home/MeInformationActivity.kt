package com.lianyi.paimonsnotebook.ui.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.ActivityMeInformationBinding
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseActivity

class MeInformationActivity : BaseActivity() {
    lateinit var bind:ActivityMeInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMeInformationBinding.inflate(layoutInflater)
        setContentView(bind.root)


    }
}