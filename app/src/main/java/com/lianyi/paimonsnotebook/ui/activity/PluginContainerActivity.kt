package com.lianyi.paimonsnotebook.ui.activity

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.util.PluginManagerImpl

class PluginContainerActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val xml = resources.getLayout(R.layout.activity_main)
        setContentView(R.layout.activity_main)
        println("插件activity")
    }

    override fun getResources(): Resources {
        return PluginManagerImpl.resources
    }

    override fun getClassLoader(): ClassLoader {
        return PluginManagerImpl.dexClassLoader
    }
}