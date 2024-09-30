package com.lianyi.paimonsnotebook.common.util.file

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.lianyi.paimonsnotebook.BuildConfig
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.number.decimal.format.format
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipFile


object FileHelper {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val contentResolver: ContentResolver by lazy {
        context.contentResolver
    }

    val provider by lazy {
        "${BuildConfig.APPLICATION_ID}.provider"
    }

    private val privateStoragePath by lazy {
        context.getExternalFilesDir(null)
    }

    //如果有外部存储权限就存储到外部的documents文件夹
    private val rootPath: File?
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            ?.resolve("PaimonsNotebook") ?: privateStoragePath

    private const val SAVE_FILE_ROOT_PATH = "PaimonsNotebook"

    private const val SAVE_IMAGE_RELATIVE_PATH = "${SAVE_FILE_ROOT_PATH}/images"

    private const val SAVE_TEMP_FILE_RELATIVE_PATH = "${SAVE_FILE_ROOT_PATH}/temp"

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
    val saveFileMetadataPath
        get() =
            privateStoragePath?.resolve("metadata")!!

    //存储安装包的路径
    private val saveFilePackagePath
        get() =
            privateStoragePath?.resolve("package")!!

    //存储祈愿记录的路径
    val saveFileGachaItemsPath
        get() =
            privateStoragePath?.resolve("gacha")!!

    //存储成就记录的路径
    val saveFileAchievementsPath
        get() =
            privateStoragePath?.resolve("achievements")!!

    //写外部存储文件的权限
    val hasWriteExternalStorage
        get() = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    //读外部存储文件的权限
    val hasReadExternalStorage
        get() = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    //扫描图片,使其出现在相册中
    private fun scanImage(path: String) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(path),
            arrayOf("*/*")
        ) { _, _ ->
        }
    }

    /*
    * 保存临时图片
    * */
    fun saveTempImage(bitmap: Bitmap, enabledMediaScanner: Boolean = true) {
        val fos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

        saveFileToPublicFolder(
            byteArray = fos.toByteArray(),
            name = "tmp_img_${System.currentTimeMillis()}",
            extension = "png",
            relativePath = SAVE_TEMP_FILE_RELATIVE_PATH,
            onSuccess = {
                if (it != null && enabledMediaScanner) {
                    scanImage(it)
                }
            }
        )

        fos.close()
    }

    /*
    * 从coil本地磁盘缓存中保存图片
    * url:图片url
    * */
    fun saveImageFromLocalCache(
        url: String,
        enabledMediaScanner: Boolean = true,
        onSuccess: (String) -> Unit,
        onFail: (String?) -> Unit
    ) {
        val cacheImage =
            PaimonsNotebookImageLoader.getCacheImageFileByUrl(url)

        if (cacheImage == null) {
            onFail.invoke("没有找到缓存的图片")
            return
        }

        saveFileToPublicFolder(
            byteArray = cacheImage.readBytes(),
            name = url,
            extension = "",
            onFail = onFail,
            relativePath = SAVE_IMAGE_RELATIVE_PATH,
            onSuccess = {
                if (enabledMediaScanner && !it.isNullOrEmpty()) {
                    scanImage(it)
                }

                onSuccess.invoke(it ?: "")
            }
        )
    }

    fun uriToFile(uri: Uri): File? {
        return try {
            val scheme = uri.scheme
            val uriPath = uri.path

            if (scheme == null && uriPath != null) {
                return File(uriPath)
            }

            if (ContentResolver.SCHEME_FILE == scheme && uriPath != null) {
                return File(uriPath)
            }

            //复制到externalCacheDir路径下
            val tempFileName = "temp_${System.currentTimeMillis()}"
            val file = File(context.externalCacheDir, tempFileName)

            val fos = FileOutputStream(file)
            contentResolver.openInputStream(uri)?.use {
                it.copyTo(fos)
            }
            fos.flush()
            fos.close()

            return file
        } catch (e: Exception) {
            "发生错误:${e.message}".warnNotify()
            null
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

    fun getMetadataDir(dirName: String): File = saveFileMetadataPath.resolve(dirName)

    /*
    * 保存文件到公共存储路径下
    *
    * */
    private fun saveFileToPublicFolder(
        byteArray: ByteArray,
        name: String,
        extension: String,
        directoryType: String = Environment.DIRECTORY_DOCUMENTS,
        relativePath: String = "",
        onFail: (String?) -> Unit = {},
        onSuccess: (String?) -> Unit = {}
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val savePath = "${directoryType}/${relativePath}"

                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, savePath)
                }

                val uri =
                    contentResolver.insert(
                        MediaStore.Files.getContentUri("external"),
                        contentValues
                    )

                if (uri == null) {
                    onFail.invoke("contentResolver.insert is null")
                    return
                }

                val os = contentResolver.openOutputStream(uri)

                if (os == null) {
                    onFail.invoke("contentResolver.openOutputStream is null")
                    return
                }

                os.use {
                    it.write(byteArray)
                }

                val path = if (extension.isEmpty()) {
                    "${savePath}${name}"
                } else {
                    "${savePath}${name}.${extension}"
                }

                onSuccess.invoke(path)
            } else {
                if (!hasWriteExternalStorage) {
                    onFail.invoke("没有外部存储权限")
                    return
                }

                val dir =
                    File(Environment.getExternalStoragePublicDirectory(directoryType), relativePath)

                if (!dir.exists()) {
                    dir.mkdirs()
                }

                //文件名替换
                val file = File(dir, name.replace("/", "_"))

                FileOutputStream(file).use {
                    it.write(byteArray)
                }

                onSuccess.invoke(file.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onFail.invoke(e.message)
        }
    }

    /*
    * 获取文件uri
    * scheme = content
    * */
    fun getContentUriForFile(file: File): Uri =
        FileProvider.getUriForFile(context, provider, file)

    /*
    * 从content uri获取file out put stream
    * */
    fun getFileOutputStreamByContentUri(uri: Uri, mode: String = "r") =
        contentResolver.openFileDescriptor(uri, mode)?.use { fileDescriptor ->
            FileOutputStream(fileDescriptor.fileDescriptor)
        }

    fun getFileInputStreamByContentUri(uri: Uri, mode: String = "r") =
        contentResolver.openFileDescriptor(uri, mode)?.use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor)
        }

    /*
    * 以流的方式保存文件
    *
    * callback:返回已读字节数
    * */
    suspend fun saveFile(file: File, inputStream: InputStream, callback: (Long) -> Unit) {
        withContext(Dispatchers.IO) {
            val outputStream = FileOutputStream(file)

            val bufferSize = 1024 * 8
            val byteArray = ByteArray(bufferSize)
            val buffer = BufferedInputStream(inputStream, bufferSize)

            var readLength = 0
            var totalReadLength = 0L

            while (coroutineContext.isActive &&
                buffer.read(byteArray, 0, bufferSize).also {
                    readLength = it
                } != -1
            ) {
                outputStream.write(byteArray, 0, readLength)
                totalReadLength += readLength
                callback.invoke(totalReadLength)
            }
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
            val uri = getContentUriForFile(file)

            val deleteRow = contentResolver.delete(uri, null, null)
            if (deleteRow == 0) {
                file.delete()
            }
        }

        return file
    }

    /*
    * 获取元数据存储文件
    * 验证元数据时也会用到该方法
    * */
    fun getMetadataSaveFile(fileName: String): File {
        val index = fileName.indexOfLast { it == '/' }

        val saveFileName = (fileName.takeLast(fileName.length - index - 1))

        val path = if (index != -1) {
            saveFileMetadataPath.resolve(fileName.take(index))
        } else {
            saveFileMetadataPath
        }

        return getSaveFile(path, "${saveFileName}.json", false)
    }


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
    * */
    fun clearTempFile() {
        if (tempFilePath.exists()) {
            tempFilePath.listFiles()?.forEach {
                it.delete()
            }
        }

        //删除外部cache目录(/data/data/package_name/cache)
        context.externalCacheDir?.listFiles()?.forEach {
            it.delete()
        }
    }


    /*
    * 获得文件尺寸描述文本
    * */
    fun getFileSizeDescribeString(length: Long): String {
        val kb = byteToKB(length)

        if (kb < 1024) {
            return "${kb.format()}KB"
        }

        val mb = byteToMB(length)

        if (mb < 1024) {
            return "${mb.format()}MB"
        }

        return "${byteToGB(length).format()}GB"
    }

    private fun byteToKB(length: Long) = length / 1024f
    private fun byteToMB(length: Long) = length / (1024f * 1024f)
    private fun byteToGB(length: Long) = length / (1024f * 1024f * 1024)

    private val df
        get() =
            context.getExternalFilesDir(null)?.resolve("debug")

    val debug: Boolean
        get() =
            df?.exists() == true
}