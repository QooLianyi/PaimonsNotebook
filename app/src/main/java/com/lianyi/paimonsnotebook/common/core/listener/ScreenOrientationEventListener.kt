package com.lianyi.paimonsnotebook.common.core.listener

import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.view.OrientationEventListener


//监听重力传感器设置屏幕方向
class ScreenOrientationEventListener(
    private val context: Context,
    private val callback: (newOrientation: Int) -> Unit
) : OrientationEventListener(context) {
    override fun onOrientationChanged(orientation: Int) {
        val rotationStatus = Settings.System.getInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0
        )

        //系统自动旋转禁用时，使用之前的设置，不再判断重力感应
        if (rotationStatus == 0) {
            return
        }

        //设备躺平，使用之前的设置，不再判断重力感应
        if (orientation == ORIENTATION_UNKNOWN) {
            return
        }

        //将所有旋转角度归结为四个方向
        val newOrientation: Int = if (orientation > 350 || orientation < 10) {
            //用户竖直拿着手机
            0
        } else if (orientation in 81..99) {
            //90度，用户右侧（正向）横屏拿着手机
            90
        } else if (orientation in 171..189) {
            //180度，用户反向竖直拿着手机
            180
        } else if (orientation in 261..279) {
            //270度，用户左侧（反向）横屏拿着手机
            270
        } else {
            return
        }

        val requestedOrientation =
            when (newOrientation) {
                0 -> {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }

                90 -> {
                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                }

                180 -> {
                    ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                }

                270 -> {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                else -> {
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }
        callback.invoke(requestedOrientation)
    }
}