package com.lianyi.paimonsnotebook.common.util.file

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.webkit.MimeTypeMap
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import java.io.File
import java.io.FileOutputStream
import java.net.URI

object FileHelper {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    val privateStoragePath by lazy {
        context.getExternalFilesDir(null)
    }

    //如果有外部存储权限就存储到外部的downloads文件夹
    private val rootPath by lazy {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            ?.resolve("PaimonsNotebook") ?: privateStoragePath
    }

    //存储图片的路径
    val saveImagePath by lazy {
        rootPath?.resolve("image")!!
    }

    //存储元数据的路径
    val saveFileMetadataPath by lazy {
        privateStoragePath?.resolve("metadata")!!
    }

    val saveFileUIGFPath by lazy {
        privateStoragePath?.resolve("uigf")!!
    }

    //存储祈愿记录的路径
    val saveFileGachaItemsPath by lazy {
        rootPath?.resolve("gacha")!!
    }

    private const val PERMISSION_REQUEST_CODE = 40000

    /*
    * 从coil本地磁盘缓存中保存图片
    * url:图片url
    * */
    fun saveImage(
        url: String,
        toastText: String = "图片已保存",
        toast: Boolean = true,
        notify: Boolean = true,
        enabledMediaScanner: Boolean = true,
    ) {
        val cacheImage =
            PaimonsNotebookImageLoader.getCacheImageFileByUrl(url)

        if (!saveImagePath.exists()) {
            saveImagePath.mkdirs()
        }

        val name = url.split("/").last()

        val file = File(saveImagePath, name)

        if (file.exists()) {
            file.delete()
        }

        FileOutputStream(file).use {
            it.write(cacheImage?.readBytes())
        }

        if (notify) {
            "图片保存到以下路径:${file.path}".notify()
        }

        if (toast) {
            toastText.show()
        }

        if (enabledMediaScanner) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.path),
                arrayOf("*/*")
            ) { _, _ ->

            }
        }
    }

    private fun uriToFileInAndroidQ(uri: Uri): File? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (uri.scheme == ContentResolver.SCHEME_FILE) {
                File(uri.path!!)
            } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                //将目标文件复制到本地沙盒环境后进行操作
                val contentResolver = context.contentResolver
                val cacheName = "${System.currentTimeMillis()}.${
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(
                        contentResolver.getType(
                            uri
                        )
                    )
                }"
                val ios = contentResolver.openInputStream(uri)
                if (ios != null) {
                    File("${context.cacheDir}/${cacheName}").apply {
                        val fos = FileOutputStream(this)
                        FileUtils.copy(ios, fos)
                        ios.close()
                        fos.close()
                    }
                } else null
            } else null
        } else null

    fun uriToFile(uri: Uri): File? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            uriToFileInAndroidQ(uri)
        } else {
            File(URI(uri.toString()))
        }
    }

    //获取元数据文件
    fun getMetadata(fileName: String): File? {
        val file = getMetadataSaveFile(fileName)
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    fun getFile(rootPath: File, fileName: String): File? {
        val file = File(rootPath, fileName)
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    fun getSaveFile(rootPath: File, fileName: String): File {
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }

        return File(rootPath, fileName)
    }


    //获取元数据存储文件
    fun getMetadataSaveFile(fileName: String): File {
        if (!saveFileMetadataPath.exists()) {
            saveFileMetadataPath.mkdirs()
        }

        return File(saveFileMetadataPath, "${fileName}.json")
    }


    //获取uigf存储文件
    fun getUIGFJsonSaveFile(name: String): File {
        if (!saveFileGachaItemsPath.exists()) {
            saveFileGachaItemsPath.mkdirs()
        }

        val file = File(saveFileGachaItemsPath, "${name}.json")

        if (file.exists()) {
            file.delete()
        }

        return file
    }

    fun saveImage(
        url: String,
        activity: Activity,
        toastText: String = "图片已保存",
        toast: Boolean = true,
        notify: Boolean = true,
        enabledMediaScanner: Boolean = true,
    ) {
        if (checkPermission()) {
            saveImage(url, toastText, toast, notify, enabledMediaScanner)
        } else {
            requestPermission(activity)
        }
    }

    //检查所需的权限
    private fun checkPermission(): Boolean {
        return SystemService.checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun checkPermissionAndTryRequest(activity: Activity): Boolean {
        val ok = checkPermission()
        if (!ok) {
            requestPermission(activity)
        }
        return ok
    }

    private fun requestPermission(activity: Activity) = SystemService.requestPermission(
        activity,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val df by lazy {
        context.getExternalFilesDir(null)?.resolve("debug")
    }

    val debug:Boolean
        get() =
            df?.exists() == true
}