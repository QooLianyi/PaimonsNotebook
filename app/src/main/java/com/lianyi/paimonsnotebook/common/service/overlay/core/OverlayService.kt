package com.lianyi.paimonsnotebook.common.service.overlay.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.service.util.ServiceHelper

/*
* 悬浮窗服务
* */
open class OverlayService(
    private val serviceId: Int,
    private val channelId: String,
    private val channelName: String,
    private val notification: Notification,
    private val channelDescription: String = "",
    private val controllerContentSizeDp: Int = 100,
    private val offset: IntOffset = IntOffset(0, 600),
    private val controllerContent: @Composable () -> Unit,
    private val content: @Composable (overlayState: OverlayState) -> Unit,
    private val onStartCommandBlock: (overlayState: OverlayState, command: String?) -> Unit,
) : Service() {

    private var isStarted = false

    private val density get() = resources.displayMetrics.density
    private val controllerContentSizePx get() = (controllerContentSizeDp * density).toInt()

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private val state by lazy {
        OverlayState(offset)
    }

    private val layoutFlag by lazy {
        //应用程序顶层 需要申请权限
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }

    private val contentViewHolder by lazy {
        OverlayViewHolder(
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ),
            this
        )
    }

    private val controllerViewHolder by lazy {
        OverlayViewHolder(
            WindowManager.LayoutParams(
                controllerContentSizePx,
                controllerContentSizePx,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ),
            this
        )
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_PaimonsNotebookAppCompat)

        initView()
    }

    private fun initView() {
        contentViewHolder.view.setContent {
            val contentAlphaAnim by animateFloatAsState(
                targetValue = if (state.showContent) 1f else 0f,
                tween(300)
            )

            //监测内容显示状态
            LaunchedEffect(state.showContent) {
                //根据状态设置是否能够获取触摸与焦点事件
                contentViewHolder.params.flags = if (state.showContent) {
                    contentViewHolder.view.visibility = View.VISIBLE
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND
                } else {
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                }
                windowManager.updateViewLayout(contentViewHolder.view, contentViewHolder.params)
            }
            //监测动画进度并从窗体中移除
            LaunchedEffect(contentAlphaAnim) {
                if (contentAlphaAnim == 0f) {
                    contentViewHolder.view.visibility = View.GONE
                    windowManager.updateViewLayout(contentViewHolder.view, contentViewHolder.params)
                }
            }

            Box(modifier = Modifier.alpha(contentAlphaAnim)) {
                content(state)
            }
        }
        windowManager.addView(contentViewHolder.view, contentViewHolder.params)

        controllerViewHolder.view.setContent {
            OverlayTouchTarget(
                viewHolder = controllerViewHolder,
                state = state,
                manager = windowManager,
                contentSize = controllerContentSizePx,
                contentSlot = {
                    controllerContent()
                }
            ) {
                state.showContent = !state.showContent
            }
        }
        windowManager.addView(controllerViewHolder.view, controllerViewHolder.params)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        onStartSendNotification()

        //此处只处理退出的事件
        intent?.let {
            when (val command = it.getStringExtra(ServiceHelper.Command)) {
                ServiceHelper.Command_Exit -> {
                    windowManager.removeView(contentViewHolder.view)
                    windowManager.removeView(controllerViewHolder.view)

                    contentViewHolder.view.disposeComposition()
                    controllerViewHolder.view.disposeComposition()

                    stopSelf()
                    return START_NOT_STICKY
                }

                else -> {
                    onStartCommandBlock(state, command)
                }
            }
        }

        return START_STICKY
    }

    private fun onStartSendNotification() {
        if (!isStarted) {
            isStarted = true
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    description = channelDescription
                }

            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
            startForeground(serviceId, notification)
        }
    }
}