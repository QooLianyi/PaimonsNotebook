package com.lianyi.paimonsnotebook.util

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import com.lianyi.paimonsnotebook.bean.gacha.UIGFJSON
import com.lianyi.paimonsnotebook.util.PaiMonsNoteBook.Companion.context
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun writeUIGFExcel(workBook: SXSSFWorkbook, name: String) {
        val path = context.getExternalFilesDir(null)
        val tableDir = File(path, "table").createDirIfNotExist()
        runCatching {
            File(tableDir, "${name}.xlsx").createFileIfNotExist().outputStream().use {
                workBook.write(it)
                workBook.dispose()
            }
        }.onFailure { it.printStackTrace() }
        workBook.close()
    }

    fun writeUIGFJSON(uigfJson: UIGFJSON) {
        val path = context.getExternalFilesDir(null)
        val tableDir = File(path, "json").createDirIfNotExist()
        File(tableDir, "${uigfJson.info.uid}_${System.currentTimeMillis() / 1000}.json")
            .createFileIfNotExist()
            .writeText(GSON.toJson(uigfJson))
    }

    fun getTablePath(uid: String): File {
        val path = context.getExternalFilesDir(null)
        val tableDir = File(path, "table").createDirIfNotExist()
        return File(tableDir, "${uid}_${System.currentTimeMillis()}.xlsx")
    }

    fun File.createDirIfNotExist() = apply {
        if (!exists()) mkdirs()
    }

    fun File.createFileIfNotExist() = apply {
        if (!exists()) createNewFile()
    }

    fun uriToFile(uri: Uri): File? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return uriToFileInAndroidQ(uri)
        } else {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                if ("primary" == split[0]) {
                    return File(Environment.getExternalStorageDirectory().absolutePath + "/" + split[1])
                }
            } else if (isDownloadsDocument(uri)) {
                val path = getDataColumn(uri)
                if (path != null) {
                    return File(path)
                }
                val docId = DocumentsContract.getDocumentId(uri)
                if (docId.isNotEmpty()) {
                    if (docId.startsWith("raw:")) {
                        docId.replaceFirst("raw:", "")
                    }
                }
                val contentUriParseTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads"
                )
                contentUriParseTry.forEach { _ ->
                    return try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            docId.toLong()
                        )
                        File(getDataColumn(contentUri) ?: "")
                    } catch (e: NumberFormatException) {
                        //在安卓8 和安卓9 ID不是数字
                        File(
                            uri.path!!
                                .replaceFirst("^/document/raw:".toRegex(), "")
                                .replaceFirst("^raw:".toRegex(), "")
                        )
                    }
                }
            } else if ("content" == uri.scheme) {
                return File(getDataColumn(uri) ?: "")
            } else if ("file" == uri.scheme) {
                return File(uri.path ?: "")
            }
            return null
        }
    }

    //安卓10及以上uri转file
    private fun uriToFileInAndroidQ(uri: Uri): File? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (uri.scheme == ContentResolver.SCHEME_FILE) {
                File(uri.path!!)
            } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                //将目标文件复制到本地沙盒环境后进行操作
                val contentResolver = context.contentResolver
                val cacheName = "${System.currentTimeMillis()}.${
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(
                        contentResolver.getType(uri)
                    )
                }"
                contentResolver.openInputStream(uri)?.use { ios ->
                    File("${context.cacheDir}/${cacheName}").apply {
                        FileOutputStream(this).use { fos ->
                            ios.copyTo(fos)
                        }
                    }
                }
            } else null
        } else null

    private fun getDataColumn(uri: Uri): String? {
        val column = "_data"
        val projection = arrayOf(column)
        try {
            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {

        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}