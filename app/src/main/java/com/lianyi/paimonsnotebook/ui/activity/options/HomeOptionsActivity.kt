package com.lianyi.paimonsnotebook.ui.activity.options

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityHomeOptionsBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.util.sp

class HomeOptionsActivity : BaseActivity() {
    lateinit var bind:ActivityHomeOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHomeOptionsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initConfig()
    }

    private fun initView() {

    }

    private fun initConfig() {
        bind.bannerJump.isChecked = sp.getBoolean(AppConfig.SP_HOME_BANNER_JUMP_TO_ARTICLE,true)
        bind.nearActivityJump.isChecked = sp.getBoolean(AppConfig.SP_HOME_NEAR_ACTIVITY_JUMP_TO_ARTICLE,true)
        bind.homeNoticeJump.isChecked = sp.getBoolean(AppConfig.SP_HOME_NOTICE_JUMP_TO_ARTICLE,true)
        bind.announcementJump.isChecked = sp.getBoolean(AppConfig.SP_HOME_ANNOUNCEMENT_JUMP_TO_LIST,true)
    }

    override fun onDestroy() {
        super.onDestroy()
        sp.edit().apply {
            putBoolean(AppConfig.SP_HOME_BANNER_JUMP_TO_ARTICLE,bind.bannerJump.isChecked)
            putBoolean(AppConfig.SP_HOME_NEAR_ACTIVITY_JUMP_TO_ARTICLE,bind.nearActivityJump.isChecked)
            putBoolean(AppConfig.SP_HOME_NOTICE_JUMP_TO_ARTICLE,bind.homeNoticeJump.isChecked)
            putBoolean(AppConfig.SP_HOME_ANNOUNCEMENT_JUMP_TO_LIST,bind.announcementJump.isChecked)
            apply()
        }
    }
}