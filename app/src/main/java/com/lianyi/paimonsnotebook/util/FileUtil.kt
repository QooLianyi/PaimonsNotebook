package com.lianyi.paimonsnotebook.util

import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class FileUtil {
    companion object{
        fun writeTable(workBook: Workbook, name:String){
            val path = PaiMonsNoteBook.context.getExternalFilesDir(null)
            val tableDir = File(path,"table")
            var out: FileOutputStream? = null

            if(!tableDir.exists()){
                tableDir.mkdirs()
            }
            val table = File(tableDir,"${name}.xlsx")
            try {
                out = FileOutputStream(table)
                workBook.write(out)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                out?.close()
            }
        }
    }
}