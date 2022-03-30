package com.lianyi.paimonsnotebook.card.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.*

class ResinService:Service() {
    private val UPDATE_ACTION = "com.lianyi.widget.UPDATE_ACTION"

    private val UPDATE_TIME = 1000L

    lateinit var mTimer:Timer
    lateinit var mTimeTask:TimerTask

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mTimer = Timer()
        mTimeTask = object :TimerTask(){
            override fun run() {
                val intent = Intent()
                intent.action = UPDATE_ACTION
                intent.component = ComponentName(packageName,"com.lianyi.paimonsnotebook.card.ResinProgressBar")
                sendBroadcast(intent)
                Log.e("", "run: 发送消息${System.currentTimeMillis()}")
            }
        }
        mTimer.schedule(mTimeTask,1000,UPDATE_TIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.cancel()
        mTimeTask.cancel()
    }

    /*
        *  服务开始时，即调用startService()时，onStartCommand()被执行。
        *
        *  这个整形可以有四个返回值：start_sticky、start_no_sticky、START_REDELIVER_INTENT、START_STICKY_COMPATIBILITY。
        *  它们的含义分别是：
        *  1):START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，
        *     由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null;
        *  2):START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务;
        *  3):START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入;
        *  4):START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
        */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("服务启动")
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
}