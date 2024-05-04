package com.lianyi.paimonsnotebook.common.core.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.lianyi.paimonsnotebook.common.core.listener.ScreenOrientationEventListener


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
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
            val intent = it.data
            onStartActivityForResult(ActivityResult(it.resultCode,intent))
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

    //检查安装权限
    //true为可以安装
    protected fun checkInstallPermission() = packageManager.canRequestPackageInstalls()

    //申请安装权限
    protected fun requestInstallPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.setData(Uri.fromParts("package", packageName, null))
        startActivity(intent)
    }

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