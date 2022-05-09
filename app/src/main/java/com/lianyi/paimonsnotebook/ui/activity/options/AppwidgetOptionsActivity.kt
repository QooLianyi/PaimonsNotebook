package com.lianyi.paimonsnotebook.ui.activity.options

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.appwidget.CardResinType2Widget
import com.lianyi.paimonsnotebook.databinding.ActivityAppwidgetOptionsBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity

class AppwidgetOptionsActivity : BaseActivity() {
    lateinit var bind:ActivityAppwidgetOptionsBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityAppwidgetOptionsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.apply {
            create.setOnClickListener {
                val appWidgetManager = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
                val componentName = ComponentName(baseContext,CardResinType2Widget::class.java)
                if(appWidgetManager.isRequestPinAppWidgetSupported){
                    val intent = Intent(baseContext,CardResinType2Widget::class.java)
                }
            }
        }

    }
}