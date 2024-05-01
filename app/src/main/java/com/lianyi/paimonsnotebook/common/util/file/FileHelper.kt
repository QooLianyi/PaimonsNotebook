package com.lianyi.paimonsnotebook.common.util.file

import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.webkit.MimeTypeMap
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
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

    private val privateStoragePath by lazy {
        context.getExternalFilesDir(null)
    }

    //如果有外部存储权限就存储到外部的documents文件夹
    private val rootPath: File?
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            ?.resolve("PaimonsNotebook") ?: privateStoragePath

    /*
    * 临时存储文件夹
    * 此文件夹不能存储任何持久文件,每次启动时会删除该文件夹
    * */
    private val tempFilePath
        get() = rootPath?.resolve("temp")!!

    //存储图片的路径
    private val saveImagePath
        get() =
            rootPath?.resolve("image")!!

    //存储元数据的路径
    private val saveFileMetadataPath
        get() =
            privateStoragePath?.resolve("metadata")!!

    //存储安装包的路径
    private val saveFilePackagePath
        get() =
            privateStoragePath?.resolve("package")!!

    //存储祈愿记录的路径
    private val saveFileGachaItemsPath
        get() =
            rootPath?.resolve("gacha")!!

    //存储成就记录的路径
    private val saveFileAchievementsPath
        get() =
            rootPath?.resolve("achievements")!!


    //扫描图片,使其出现在相册中
    private fun scanImage(file: File) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("*/*")
        ) { _, _ ->

        }
    }

    /*
    * 保存临时图片
    * */
    fun saveTempImage(bitmap: Bitmap, enabledMediaScanner: Boolean = true) {
        val file = File(tempFilePath, "tmp_img_${System.currentTimeMillis()}.png")

        if (!tempFilePath.exists()) {
            tempFilePath.mkdirs()
        }

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()

        if (enabledMediaScanner) {
            scanImage(file)
        }
    }

    /*
    * 从coil本地磁盘缓存中保存图片
    * url:图片url
    * */
    fun saveImageFromLocalCache(
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
            scanImage(file)
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

    fun getFile(rootPath: File, fileName: String): File {
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

    //获取存储的文件
    private fun getSaveFile(
        rootPath: File,
        fileName: String,
        onExistsDelete: Boolean = true
    ): File {
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }

        val file = File(rootPath, fileName)

        if (file.exists() && onExistsDelete) {
            file.delete()
        }

        return file
    }

    /*
    * 获取元数据存储文件
    * 验证元数据时也会用到该方法,onExistsDelete必须为false,否则会导致重复下载导致无法进入程序
    * */
    fun getMetadataSaveFile(fileName: String) =
        getSaveFile(saveFileMetadataPath, "${fileName}.json", false)


    //获取uigf存储文件
    fun getUIGFJsonSaveFile(name: String) =
        getSaveFile(saveFileGachaItemsPath, "${name}.json")

    //获取uiaf存储文件
    fun getUIAFJsonSaveFile(name: String) =
        getSaveFile(saveFileAchievementsPath, "${name}.json")

    //获取安装包存储文件
    fun getPackageSaveFile(name: String) =
        getSaveFile(saveFilePackagePath, "${name}.apk")

    /*
    * 清理临时文件
    * 当前版本是直接删除整个文件夹,后续有特殊需求再改
    * */
    fun clearTempFile() {
        if (tempFilePath.exists()) {
            tempFilePath.delete()
        }
    }

    private val df
        get() =
            context.getExternalFilesDir(null)?.resolve("debug")

    val debug: Boolean
        get() =
            df?.exists() == true
}