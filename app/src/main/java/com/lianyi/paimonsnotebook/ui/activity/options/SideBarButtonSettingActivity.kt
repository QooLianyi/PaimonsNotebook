package com.lianyi.paimonsnotebook.ui.activity.options

import android.os.Bundle
import androidx.core.view.get
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.config.SideBarButtonSettings
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivitySideBarButtonSettingBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.MainActivity
import com.lianyi.paimonsnotebook.ui.home.CharacterFragment
import com.lianyi.paimonsnotebook.ui.home.WeaponFragment
import com.lianyi.paimonsnotebook.util.*

class SideBarButtonSettingActivity : BaseActivity() {
    lateinit var bind:ActivitySideBarButtonSettingBinding

    private var sideBarWidth = 0
    private var enable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySideBarButtonSettingBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initConfig()
    }

    private fun initView() {
        //是否启用
        bind.sideBarEnable.click{
            enable = it
            if(it){
                enableOptions()
            }else{
                disabledOptions()
            }
        }

        //样式预览
        bind.stylePreview.click {
            bind.stylePreview.isEnabled = false
            if(it){
                bind.sideBarButtonLeft.show()
                openAndCloseAnimationHor(bind.sideBarButtonLeft,0,sideBarWidth,400L){
                    if(enable){
                        bind.stylePreview.isEnabled = true
                    }
                }
            }else{
                openAndCloseAnimationHor(bind.sideBarButtonLeft,sideBarWidth,0,400L){
                    bind.sideBarButtonLeft.gone()
                    if(enable){
                        bind.stylePreview.isEnabled = true
                    }
                }
            }
        }

        bind.sideBarButtonAreaWidth.onChange {
            openAndCloseAnimationHor(bind.sideBarButtonLeft,sideBarWidth,it,200L)
            bind.sideBarButtonAreaWidthText.text = "${it}dp"
            sideBarWidth = it
        }

        bind.hideDefaultSideBarButton.click {
            if(it){
                MainActivity.bind.menuSwitch.gone()
            }else{
                MainActivity.bind.menuSwitch.show()
            }
        }

        sideBarWidth = SideBarButtonSettings.instance.sideBarAreaWidth
        enable = SideBarButtonSettings.instance.enabled
        setContentMargin(bind.root)
    }

    private fun initConfig() {
        if(SideBarButtonSettings.instance.enabled){
            bind.sideBarEnable.isChecked = SideBarButtonSettings.instance.enabled
            bind.stylePreview.isChecked = SideBarButtonSettings.instance.stylePreview
            bind.hideDefaultSideBarButton.isChecked = SideBarButtonSettings.instance.hideDefaultSideBarButton
            bind.sideBarButtonAreaWidth.progress = SideBarButtonSettings.instance.widthProgress
            bind.sideBarButtonAreaWidthText.text = "${SideBarButtonSettings.instance.sideBarAreaWidth}dp"
            enableOptions()
        }else{
            disabledOptions()
        }
    }

    private fun enableOptions(){
        bind.stylePreview.isEnabled = true
        bind.stylePreview.setTextColor(getColor(R.color.black))
        bind.hideDefaultSideBarButton.isEnabled = true
        bind.hideDefaultSideBarButton.setTextColor(getColor(R.color.black))
        bind.sideBarButtonAreaWidthOptionText.setTextColor(getColor(R.color.black))
        bind.sideBarButtonAreaWidth.isEnabled = true
        openAndCloseAnimationHor(bind.sideBarButtonLeft,0,sideBarWidth,400L)
    }

    private fun disabledOptions(){
        bind.stylePreview.isEnabled = false
        bind.stylePreview.setTextColor(getColor(R.color.black_alpha_50))
        bind.hideDefaultSideBarButton.isEnabled = false
        bind.hideDefaultSideBarButton.setTextColor(getColor(R.color.black_alpha_50))
        bind.sideBarButtonAreaWidthOptionText.setTextColor(getColor(R.color.black_alpha_50))
        bind.sideBarButtonAreaWidth.isEnabled = false

        openAndCloseAnimationHor(bind.sideBarButtonLeft,sideBarWidth,0,400L)
    }

    override fun onDestroy() {
        val setting = SideBarButtonSettings(
            enable,
            bind.stylePreview.isChecked,
            bind.hideDefaultSideBarButton.isChecked,
            bind.sideBarButtonAreaWidth.progress,
            sideBarWidth
        )

        sp.edit().apply {
            putString(AppConfig.SP_SIDE_BAR_BUTTON_SETTINGS,GSON.toJson(setting))
            apply()
        }

        SideBarButtonSettings.instance = setting

        if(setting.enabled){
            MainActivity.bind.menuAreaSwitch.layoutParams.apply {
                width = sideBarWidth.dp.toInt()
            }
            MainActivity.bind.menuAreaSwitch.show()

            if(setting.hideDefaultSideBarButton){
                MainActivity.bind.menuSwitch.gone()
            }else{
                MainActivity.bind.menuSwitch.show()
            }

        }else{
            MainActivity.bind.menuSwitch.show()
            MainActivity.bind.menuAreaSwitch.gone()
        }
        super.onDestroy()
    }
}