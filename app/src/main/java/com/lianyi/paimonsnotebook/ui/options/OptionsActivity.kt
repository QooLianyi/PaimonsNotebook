package com.lianyi.paimonsnotebook.ui.options

import android.os.Bundle
import android.widget.SeekBar
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.config.Format
import com.lianyi.paimonsnotebook.databinding.ActivityOptionsBinding
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread

class OptionsActivity : BaseActivity() {
    lateinit var bind: ActivityOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //更新JSON数据
        bind.getJson.setOnClickListener {
            showLoading(bind.root.context)
            RefreshData.getJsonData {
                runOnUiThread {
                    if(it){
                        "更新成功~".show()
                        bind.getJsonTime.text = Format.TIME_FULL.format(System.currentTimeMillis())
                    }else{
                        "更新失败,请稍后再试试吧".show()
                    }
                    loadingWindowDismiss()
                }
            }
        }

        bind.optionsData.select {
            if(it){
                openAndCloseAnimationVer(bind.optionsDataSpan,35,150,500)
                bind.optionsDataDropDown.rotation = 0f
            }else{
                openAndCloseAnimationVer(bind.optionsDataSpan,150,35,500)
                bind.optionsDataDropDown.rotation = 180f
            }
            bind.optionsDataDropDown.animate().rotationBy(180f).duration = 500
        }

        bind.optionsTestShowLoadingWindow.select {
            if(it){
                openAndCloseAnimationVer(bind.optionsTestShowLoadingWindowSpan,35,150,500)
                bind.optionsTestShowLoadingWindowDropDown.rotation = 0f
            }else{
                openAndCloseAnimationVer(bind.optionsTestShowLoadingWindowSpan,150,35,500)
                bind.optionsTestShowLoadingWindowDropDown.rotation = 180f
            }
            bind.optionsTestShowLoadingWindowDropDown.animate().rotationBy(180f).duration = 500
        }

        bind.showLoadingWindowTimeProgressBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                bind.showLoadingWindowTime.text = "${p1}秒"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        bind.showLoadingWindow.setOnClickListener {
            var time = bind.showLoadingWindowTimeProgressBar.progress
            showLoading(bind.root.context)
            bind.showLoadingWindow.isEnabled = false
            thread {
                do{
                    time--
                    Thread.sleep(1000)
                    if(time<=0){
                        runOnUiThread {
                            loadingWindowDismiss()
                            bind.showLoadingWindow.isEnabled = true
                        }
                    }
                }while (time>0)
            }
        }

    }
}