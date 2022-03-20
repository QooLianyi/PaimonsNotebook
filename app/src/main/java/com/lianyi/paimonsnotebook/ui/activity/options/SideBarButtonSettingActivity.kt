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
                    if(bind.sideBarEnable.isChecked){
                        bind.stylePreview.isEnabled = true
                    }
                }
            }else{
                openAndCloseAnimationHor(bind.sideBarButtonLeft,sideBarWidth,0,400L){
                    bind.sideBarButtonLeft.gone()
                    if(bind.sideBarEnable.isChecked){
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
        setContentMargin(bind.root)
    }

    private fun initConfig() {
        bind.sideBarEnable.isChecked = SideBarButtonSettings.instance.enabled
        bind.stylePreview.isChecked = SideBarButtonSettings.instance.stylePreview
        bind.hideDefaultSideBarButton.isChecked = SideBarButtonSettings.instance.hideDefaultSideBarButton
        bind.sideBarButtonAreaWidth.progress = SideBarButtonSettings.instance.widthProgress
        bind.sideBarButtonAreaWidthText.text = "${SideBarButtonSettings.instance.sideBarAreaWidth}dp"
        bind.sideBarAutoPickup.isChecked = sp.getBoolean(AppConfig.SP_HOME_SIDE_BAR_AUTO_PICKUP,false)
        if(SideBarButtonSettings.instance.enabled){
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
        super.onDestroy()
        val setting = SideBarButtonSettings(
            bind.sideBarEnable.isChecked,
            bind.stylePreview.isChecked,
            bind.hideDefaultSideBarButton.isChecked,
            bind.sideBarButtonAreaWidth.progress,
            sideBarWidth
        )

        sp.edit().apply {
            putString(AppConfig.SP_SIDE_BAR_BUTTON_SETTINGS,GSON.toJson(setting))
            putBoolean(AppConfig.SP_HOME_SIDE_BAR_AUTO_PICKUP,bind.sideBarAutoPickup.isChecked)
            apply()
        }

        println(setting.toString())

        SideBarButtonSettings.instance = setting

        if(setting.enabled){
            val lp = MainActivity.bind.menuAreaSwitch.layoutParams
            MainActivity.bind.menuAreaSwitch.layoutParams = lp.apply {
                width = sideBarWidth.dp.toInt()
            }
            MainActivity.bind.menuAreaSwitch.show()
            if(setting.hideDefaultSideBarButton){
                MainActivity.bind.menuSwitch.gone()
            }else{
                MainActivity.bind.menuSwitch.show()
            }
            if(setting.stylePreview){
                MainActivity.bind.menuAreaSwitch.setBackgroundColor(getColor(R.color.black_alpha_50))
            }else{
                MainActivity.bind.menuAreaSwitch.setBackgroundColor(getColor(R.color.transparent))
            }
        }else{
            MainActivity.bind.menuSwitch.show()
            MainActivity.bind.menuAreaSwitch.gone()
            MainActivity.bind.menuAreaSwitch.setBackgroundColor(getColor(R.color.transparent))
        }
    }
}