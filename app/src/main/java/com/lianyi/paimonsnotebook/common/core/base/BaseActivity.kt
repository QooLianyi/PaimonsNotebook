package com.lianyi.paimonsnotebook.common.core.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.lianyi.paimonsnotebook.common.core.listener.ScreenOrientationEventListener
import com.lianyi.paimonsnotebook.common.extension.activity.setSystemBarAppearance

/*
* Activity基类
*
* enableGesture:是否启用手势退出,默认开启
* enableSensor:是否启用传感器修改屏幕方向,默认开启
* */
open class BaseActivity(
    private val enableGesture: Boolean = true,
) : ComponentActivity() {
    private val storagePermissions by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    //尝试设置window背景为透明
    private fun trySetTranslucent() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setTranslucent(enableGesture)
            }

        } catch (_: Exception) {
        }
    }

    //设置通过传感器控制屏幕旋转方向
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setRequestedOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ScreenOrientationEventListener(this) {
            requestedOrientation = it
        }.enable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation()
        trySetTranslucent()

        super.onCreate(savedInstanceState)
    }

    //注册申请权限生命周期
    protected fun registerRequestPermissionsResult() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            onRequestPermissionsResult(it)
        }

    //注册ActivityResult
    protected fun registerStartActivityForResult() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onStartActivityForResult(it)
        }

    protected fun registerPressedCallback() =
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedCallback()
            }
        })

    //检查存储权限
    protected fun checkStoragePermission() = checkPermissions(storagePermissions)

    //申请存储权限
    protected fun requestStoragePermission() = requestPermissions(storagePermissions)

    //检查权限 true为全部获取
    protected fun checkPermissions(
        permissions: Array<String>
    ) = permissions.count {
        checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    } == permissions.size

    //申请权限
    protected fun requestPermissions(
        permissions: Array<String>
    ) {
        requestPermissions(
            permissions,
            100
        )
    }

    //权限申请结果
    protected open fun onRequestPermissionsResult(result: Boolean) {}

    //Activity返回结果
    protected open fun onStartActivityForResult(result: ActivityResult) {}

    //当点击返回时
    protected open fun onBackPressedCallback() {}
}