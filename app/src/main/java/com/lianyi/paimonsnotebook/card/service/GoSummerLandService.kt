package com.lianyi.paimonsnotebook.card.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.lianyi.paimonsnotebook.ui.summerland.SummerLandActivity

class GoSummerLandService:Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        println("跳转海岛")
        startActivity(Intent(this,SummerLandActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        stopSelf()
    }

}