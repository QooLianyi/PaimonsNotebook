package com.lianyi.paimonsnotebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import me.jessyan.autosize.internal.CustomAdapt

class LoadingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

}