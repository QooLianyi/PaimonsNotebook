package com.lianyi.paimonsnotebook.ui.activity.options

import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.config.ContentMarginSettings
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityContentMarginBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.util.*

class ContentMarginActivity : BaseActivity() {
    lateinit var bind:ActivityContentMarginBinding

    private var horizontalMargin = 0
    private var topMargin = 0
    private var enable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityContentMarginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initConfig()
    }

    private fun initView() {
        bind.contentMarginEnable.click {
            enable = it
            if(it){
                enableOptions()
            }else{
                disableOptions()
            }
        }

        bind.contentMarginPreview.click {
            bind.contentMarginPreview.isEnabled = false
            if(it){
               showPreview()
            }else{
                hidePreview()
            }
        }

        bind.horizontalContentMargin.onChange {
            bind.horizontalContentMarginText.text = "${it}dp"
            openAndCloseAnimationHor(bind.marginPreviewLeft,horizontalMargin,it,400)
            openAndCloseAnimationHor(bind.marginPreviewRight,horizontalMargin,it,400)
            horizontalMargin = it
        }

        bind.topContentMargin.onChange {
            bind.topContentMarginText.text = "${it}dp"
            openAndCloseAnimationVer(bind.marginPreviewTop,topMargin,it,400)
            topMargin = it
        }

        bind.goTest.setOnClickListener {

        }

        horizontalMargin = ContentMarginSettings.instance.horizontalMargin
        topMargin = ContentMarginSettings.instance.topMargin
        enable = ContentMarginSettings.instance.enable
        setContentMargin(bind.root)
    }

    private fun initConfig() {
        bind.contentMarginEnable.isChecked = ContentMarginSettings.instance.enable
        bind.contentMarginPreview.isChecked = ContentMarginSettings.instance.marginPreview
        bind.topContentMarginText.text = "${ContentMarginSettings.instance.topMargin}dp"
        bind.horizontalContentMarginText.text = "${ContentMarginSettings.instance.horizontalMargin}dp"
        bind.topContentMargin.progress = ContentMarginSettings.instance.topProgress
        bind.horizontalContentMargin.progress = ContentMarginSettings.instance.horizontalProgress
        if(enable){
            enableOptions()
        }else{
            disableOptions()
        }
    }

    private fun enableOptions(){
        bind.contentMarginPreview.setTextColor(getColor(R.color.black))
        bind.contentMarginPreview.isEnabled = true
        bind.horizontalContentMarginOptionText.setTextColor(getColor(R.color.black))
        bind.horizontalContentMargin.isEnabled = true
        bind.topContentMarginText.setTextColor(getColor(R.color.black))
        bind.topContentMargin.isEnabled = true
        bind.goTest.isEnabled = true

        if(bind.contentMarginPreview.isChecked){
            showPreview()
        }
    }

    private fun disableOptions(){
        bind.contentMarginPreview.setTextColor(getColor(R.color.black_alpha_50))
        bind.contentMarginPreview.isEnabled = false
        bind.horizontalContentMarginOptionText.setTextColor(getColor(R.color.black_alpha_50))
        bind.horizontalContentMargin.isEnabled = false
        bind.topContentMarginOptionText.setTextColor(getColor(R.color.black_alpha_50))
        bind.topContentMargin.isEnabled = false
        bind.goTest.isEnabled = false
        hidePreview(false)
    }

    private fun showPreview(){
        bind.marginPreviewLeft.show()
        bind.marginPreviewRight.show()
        bind.marginPreviewTop.show()
        openAndCloseAnimationVer(bind.marginPreviewTop,0,topMargin,400)
        openAndCloseAnimationHor(bind.marginPreviewLeft,0,horizontalMargin,400)
        openAndCloseAnimationHor(bind.marginPreviewRight,0,horizontalMargin,400){
            if(enable){
                bind.contentMarginPreview.isEnabled = true
            }
        }
    }

    private fun hidePreview(switchRest:Boolean = true){
        openAndCloseAnimationVer(bind.marginPreviewTop,topMargin,0,400)
        openAndCloseAnimationHor(bind.marginPreviewLeft,horizontalMargin,0,400)
        openAndCloseAnimationHor(bind.marginPreviewRight,horizontalMargin,0,400){
            if(switchRest){
                bind.marginPreviewLeft.gone()
                bind.marginPreviewRight.gone()
                bind.marginPreviewTop.gone()
                if(enable){
                    bind.contentMarginPreview.isEnabled = true
                }
            }
        }
    }

    override fun onDestroy() {
        val setting = ContentMarginSettings(
            bind.contentMarginEnable.isChecked,
            bind.contentMarginPreview.isChecked,
            bind.horizontalContentMargin.progress,
            horizontalMargin,
            bind.topContentMargin.progress,
            topMargin
        )

        sp.edit().apply{
            putString(AppConfig.SP_CONTENT_MARGIN_SETTINGS,GSON.toJson(setting))
            apply()
        }

        "重启应用以应用设置".showLong()
        ContentMarginSettings.instance = setting

        super.onDestroy()
    }
}