package com.lianyi.paimonsnotebook.util

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import com.lianyi.paimonsnotebook.bean.gacha.UIGFJSON
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class FileUtil {
    companion object{
        fun writeUIGFExcel(workBook: SXSSFWorkbook, name: String){
            val path = PaiMonsNoteBook.context.getExternalFilesDir(null)
            val tableDir = File(path, "table")
            var out: FileOutputStream? = null

            if(!tableDir.exists()){
                tableDir.mkdirs()
            }

            val table = File(tableDir, "${name}.xlsx")
            try {
                out = FileOutputStream(table)
                workBook.write(out)
                workBook.dispose()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                out?.close()
                workBook.close()
            }
        }

        fun writeUIGFJSON(uigfJson: UIGFJSON){
            val path = PaiMonsNoteBook.context.getExternalFilesDir(null)
            val tableDir = File(path, "json")

            if(!tableDir.exists()){
                tableDir.mkdirs()
            }

            val jsonFile = File(tableDir, "${uigfJson.info.uid}_${System.currentTimeMillis()/1000}.json")
            try {
                jsonFile.createNewFile()
                val fileWriter = FileWriter(jsonFile,true)
                val buffWriter = BufferedWriter(fileWriter)
                val data = GSON.toJson(uigfJson)
                buffWriter.write(data)
                buffWriter.flush()
                buffWriter.close()
                fileWriter.close()
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        fun getTablePath(uid: String):File?{
            val path = PaiMonsNoteBook.context.getExternalFilesDir(null)
            val tableDir = File(path, "table")

            if(!tableDir.exists()){
                tableDir.mkdirs()
            }
            return File(tableDir, "${uid}_${System.currentTimeMillis()}.xlsx")
        }

        fun uriToFile(uri: Uri):File? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return uriToFileInAndroidQ(uri)
            }else{
                if(isExternalStorageDocument(uri)){
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    if("primary" == split[0]){
                     return File(Environment.getExternalStorageDirectory().absolutePath + "/" + split[1])
                    }
                }else if(isDownloadsDocument(uri)) {
                    val path = getDataColumn(uri)
                    if(path!=null){
                        return File(path)
                    }
                    val docId = DocumentsContract.getDocumentId(uri)
                    if(docId.isNotEmpty()){
                        if(docId.startsWith("raw:")){
                            docId.replaceFirst("raw:", "")
                        }
                    }
                    val contentUriParseTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )
                    contentUriParseTry.forEach {
                        try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                docId.toLong()
                            )
                            return File(getDataColumn(contentUri) ?: "")
                        }catch (e: NumberFormatException){
                            //在安卓8 和安卓9 ID不是数字
                            return File(uri.path!!
                                .replaceFirst("^/document/raw:".toRegex(), "")
                                .replaceFirst("^raw:".toRegex(), ""))
                        }
                    }
                }else if("content" == uri.scheme) {
                    return File(getDataColumn(uri) ?: "")
                } else if ("file" == uri.scheme) {
                    return File(uri.path ?: "")
                }
                return null
            }
        }

        //安卓10及以上uri转file
        private fun uriToFileInAndroidQ(uri: Uri):File? =
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                if(uri.scheme==ContentResolver.SCHEME_FILE){
                    File(uri.path!!)
                }else if(uri.scheme==ContentResolver.SCHEME_CONTENT){
                    //将目标文件复制到本地沙盒环境后进行操作
                    val contentResolver = PaiMonsNoteBook.context.contentResolver
                    val cacheName = "${System.currentTimeMillis()}.${MimeTypeMap.getSingleton().getExtensionFromMimeType(
                        contentResolver.getType(
                            uri
                        )
                    )}"
                    val ios = contentResolver.openInputStream(uri)
                    if(ios!=null){
                        File("${PaiMonsNoteBook.context.cacheDir}/${cacheName}").apply {
                            val fos = FileOutputStream(this)
                            FileUtils.copy(ios, fos)
                            ios.close()
                            fos.close()
                        }
                    }else null
                }else null
            }else null

        private fun getDataColumn(uri: Uri):String?{
            val column = "_data"
            val projection = arrayOf(column)
            try {
                val cursor = PaiMonsNoteBook.context.contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    null
                )
                if(cursor!=null&&cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(columnIndex)
                }
            }catch (e: Exception){

            }
            return null
        }

        private fun isExternalStorageDocument(uri: Uri):Boolean{
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri):Boolean{
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isMediaDocument(uri: Uri):Boolean{
            return "com.android.providers.media.documents" == uri.authority
        }

    }

}