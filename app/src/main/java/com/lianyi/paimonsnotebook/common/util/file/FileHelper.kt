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
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.util.zip.ZipFile

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

    //存储元数据图片的路径
    val metadataImagesPath by lazy {
        saveFileMetadataPath.resolve("images")
    }

    val saveFileUIGFPath by lazy {
        privateStoragePath?.resolve("uigf")!!
    }

    //存储祈愿记录的路径
    val saveFileGachaItemsPath by lazy {
        rootPath?.resolve("gacha")!!
    }

    /*
    * 从coil本地磁盘缓存中保存图片
    * url:图片url
    * */
    fun saveImage(
        url: String,
        enabledMediaScanner: Boolean = true,
        onSuccess: (String) -> Unit,
        onFail: () -> Unit
    ) {
        val cacheImage =
            PaimonsNotebookImageLoader.getCacheImageFileByUrl(url)

        if (cacheImage == null) {
            onFail.invoke()
            return
        }

        if (!saveImagePath.exists()) {
            saveImagePath.mkdirs()
        }

        val name = url.split("/").last()

        val file = File(saveImagePath, name)

        if (file.exists()) {
            file.delete()
            file.mkdirs()
        }

        FileOutputStream(file).use {
            it.write(cacheImage.readBytes())
        }

        if (enabledMediaScanner) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.path),
                arrayOf("*/*")
            ) { _, _ ->

            }
        }

        onSuccess.invoke(file.absolutePath)
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

    /*
    * 以流的方式保存文件
    *
    * callback:返回已读字节数
    * */
    fun saveFile(file: File, inputStream: InputStream, callback: (Long) -> Unit) {
        val outputStream = FileOutputStream(file)
        val bufferSize = 1024 * 8
        val byteArray = ByteArray(bufferSize)
        val buffer = BufferedInputStream(inputStream, bufferSize)

        var readLength: Int
        var totalReadLength = 0L
        while (buffer.read(byteArray, 0, bufferSize).also {
                readLength = it
            } != -1) {

            outputStream.write(byteArray, 0, readLength)
            totalReadLength += readLength
            callback.invoke(totalReadLength)
        }
    }

    //解压压缩文件
    fun extractZipFile(file: File, target: String) {
        val zipFile = ZipFile(file.absoluteFile)

        for (entry in zipFile.entries()) {
            val item = File(target, entry.name)
            if (entry.isDirectory) {
                item.mkdirs()
            } else {
                val arr = zipFile.getInputStream(entry).use {
                    it.readBytes()
                }

                item.writeBytes(arr)
            }
        }
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

    //检查所需的权限
    private fun checkPermission(): Boolean {
        return SystemService.checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun requestPermission(activity: Activity) = SystemService.requestPermission(
        activity,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val df
        get() =
            context.getExternalFilesDir(null)?.resolve("debug")

    val debug: Boolean
        get() =
            df?.exists() == true
}