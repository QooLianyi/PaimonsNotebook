package com.lianyi.paimonsnotebook.util

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lianyi.paimonsnotebook.bean.gacha.UIGFExcelBean
import com.lianyi.paimonsnotebook.bean.gacha.UIGFJsonBean
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import org.json.JSONArray

class PaiMonsNotebookDataBase private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    companion object{
        val INSTANCE by lazy {
            PaiMonsNotebookDataBase(PaiMonsNoteBook.context)
        }
        private const val DB_NAME = "PaimonsNotebookDataBase"
        private const val DB_VERSION =100
        private const val TABLE_NAME = "gacha_history_"

        //列名常量
       const val COLUMN_NAME_COUNT = "count"
       const val COLUMN_NAME_GACHA_TYPE = "gacha_type"
       const val COLUMN_NAME_ID = "id"
       const val COLUMN_NAME_ITEM_ID = "item_id"
       const val COLUMN_NAME_ITEM_TYPE = "item_type"
       const val COLUMN_NAME_LANG = "lang"
       const val COLUMN_NAME_NAME = "name"
       const val COLUMN_NAME_RANK_TYPE = "rank_type"
       const val COLUMN_NAME_TIME = "time"
       const val COLUMN_NAME_UID = "uid"
       const val COLUMN_NAME_UIGF_GACHA_TYPE = "uigf_gacha_type"


        private fun getCreateTableSql(uid:String):String{
            return "create table if not exists ${getGachaHistoryTableName(uid)}($COLUMN_NAME_COUNT text,$COLUMN_NAME_GACHA_TYPE text,$COLUMN_NAME_ID text primary key,$COLUMN_NAME_ITEM_ID text,$COLUMN_NAME_ITEM_TYPE text,$COLUMN_NAME_LANG text,$COLUMN_NAME_NAME text,$COLUMN_NAME_RANK_TYPE text,$COLUMN_NAME_TIME text,$COLUMN_NAME_UID text,$COLUMN_NAME_UIGF_GACHA_TYPE text)"
        }

        private fun getGachaHistoryTableName(uid: String):String{
            return "$TABLE_NAME$uid"
        }

    }

    override fun onCreate(p0: SQLiteDatabase?) {
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    //判断账号为uid的祈愿历史表是否存在
    fun checkGachaHistoryTableExists(uid: String):Boolean{
        try {
            val rawQuery = readableDatabase.rawQuery("select * from ${getGachaHistoryTableName(uid)}",null)
            if(rawQuery.moveToNext()){
                println("move = ${System.currentTimeMillis()}")
                return true
            }
            rawQuery.close()
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
        return false
    }

    //获得祈愿记录表(全部)
    fun getGachaHistoryForExcel(uid: String):List<UIGFExcelBean>{
        val list = mutableListOf<UIGFExcelBean>()
        val exists = checkGachaHistoryTableExists(uid)

        if(exists){
            val cursor = readableDatabase.query(getGachaHistoryTableName(uid),null,null,null,null,null,null)
            while (cursor.moveToNext()){
                list += UIGFExcelBean(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COUNT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GACHA_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LANG)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_RANK_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UIGF_GACHA_TYPE))
                )
            }
            cursor.close()
        }
        //获取时再次根据ID排序 降序
        list.sortBy { it.id.toLong() }
        return list
    }

    fun getGachaHistoryForJson(uid: String):List<UIGFJsonBean>{
        val list = mutableListOf<UIGFJsonBean>()
        val exists = checkGachaHistoryTableExists(uid)

        if(exists){
            val cursor = readableDatabase.query(getGachaHistoryTableName(uid),null,null,null,null,null,null)
            while (cursor.moveToNext()){
                list += UIGFJsonBean(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COUNT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GACHA_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_RANK_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UIGF_GACHA_TYPE))
                )
            }
            cursor.close()
        }
        //获取时再次根据ID排序 降序
        list.sortBy { it.id.toLong() }
        return list
    }

    //传入一个集合将数据追加到集合中
    fun getGachaHistoryForExcel(uid: String, list: MutableList<UIGFExcelBean>){
        list.clear()
        getGachaHistoryForExcel(uid).forEach {
            list +=it
        }
    }

    //写入祈愿记录 返回成功写入的个数
    fun writeGachaHistoryForExcel(uid: String, list: List<UIGFExcelBean>):Int{
        val exists = checkGachaHistoryTableExists(uid)
        var writeCount = 0
        if(!exists){
            writableDatabase.execSQL(getCreateTableSql(uid))
        }

        list.forEach {
            if(insertDataForExcel(it,list.first().uid)) writeCount++
        }
        addGachaHistoryAccount(uid)
        return writeCount
    }

    fun writeGachaHistoryForJson(uid: String,lang:String, list: List<UIGFJsonBean>):Int{
        val exists = checkGachaHistoryTableExists(uid)
        var writeCount = 0
        if(!exists){
            writableDatabase.execSQL(getCreateTableSql(uid))
        }

        list.forEach {
            if(insertDataForJson(it,uid,lang)) writeCount++
        }
        addGachaHistoryAccount(uid)
        return writeCount
    }

    fun getCount(uid:String):Long{
        val sql = "select count(*) from ${getGachaHistoryTableName(uid)}"
        val cursor  = readableDatabase.rawQuery(sql,null)
        cursor .moveToFirst()
        val count = cursor .getLong(0)
        cursor .close()
        return count
    }

    //插入数据
    fun insertDataForExcel(uigfExcelBean: UIGFExcelBean, uid: String):Boolean{
        writableDatabase.execSQL(getCreateTableSql(uid))

        //查找是否有相同的ID
        val cursor = writableDatabase.query(getGachaHistoryTableName(uid), arrayOf(COLUMN_NAME_ID),"$COLUMN_NAME_ID = ${uigfExcelBean.id}",null,null,null,null,null)
        if(!cursor.moveToNext()){
            val values = ContentValues()
            values.put(COLUMN_NAME_COUNT,uigfExcelBean.count)
            values.put(COLUMN_NAME_GACHA_TYPE,uigfExcelBean.gacha_type)
            values.put(COLUMN_NAME_ID,uigfExcelBean.id)
            values.put(COLUMN_NAME_ITEM_ID,uigfExcelBean.item_id)
            values.put(COLUMN_NAME_ITEM_TYPE,uigfExcelBean.item_type)
            values.put(COLUMN_NAME_LANG,uigfExcelBean.lang)
            values.put(COLUMN_NAME_NAME,uigfExcelBean.name)
            values.put(COLUMN_NAME_RANK_TYPE,uigfExcelBean.rank_type)
            values.put(COLUMN_NAME_TIME,uigfExcelBean.time)
            values.put(COLUMN_NAME_UID,uigfExcelBean.uid)
            values.put(COLUMN_NAME_UIGF_GACHA_TYPE,uigfExcelBean.uigf_gacha_type)
            writableDatabase.insert(getGachaHistoryTableName(uigfExcelBean.uid),null,values)
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }

    fun insertDataForJson(uigfExcelBean: UIGFJsonBean, uid: String ,lang: String):Boolean{
        writableDatabase.execSQL(getCreateTableSql(uid))

        //查找是否有相同的ID
        val cursor = writableDatabase.query(getGachaHistoryTableName(uid), arrayOf(COLUMN_NAME_ID),"$COLUMN_NAME_ID = $uid",null,null,null,null,null)
        if(!cursor.moveToNext()){
            val values = ContentValues()
            values.put(COLUMN_NAME_COUNT,uigfExcelBean.count)
            values.put(COLUMN_NAME_GACHA_TYPE,uigfExcelBean.gacha_type)
            values.put(COLUMN_NAME_ID,uigfExcelBean.id)
            values.put(COLUMN_NAME_ITEM_ID,uigfExcelBean.item_id)
            values.put(COLUMN_NAME_ITEM_TYPE,uigfExcelBean.item_type)
            values.put(COLUMN_NAME_LANG,lang)
            values.put(COLUMN_NAME_NAME,uigfExcelBean.name)
            values.put(COLUMN_NAME_RANK_TYPE,uigfExcelBean.rank_type)
            values.put(COLUMN_NAME_TIME,uigfExcelBean.time)
            values.put(COLUMN_NAME_UID,uid)
            values.put(COLUMN_NAME_UIGF_GACHA_TYPE,uigfExcelBean.uigf_gacha_type)
            writableDatabase.insert(getGachaHistoryTableName(uid),null,values)
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }


    //获得某个蛋池里最后的记录ID
    fun getLastGachaHistoryId(gachaType:String,uid: String):String{
        val exists = checkGachaHistoryTableExists(uid)
        if(!exists){
            return ""
        }

        val list = getGachaHistoryForExcel(uid,gachaType)
        list.sortedBy { it.id.toLong() }

        return list.last().id
    }

    //获得祈愿记录表(根据蛋池)
    private fun getGachaHistoryForExcel(uid: String, gachaType: String):List<UIGFExcelBean>{
        val list = mutableListOf<UIGFExcelBean>()
        val exists = checkGachaHistoryTableExists(uid)

        if(exists){
            val cursor = readableDatabase.query(getGachaHistoryTableName(uid),null,"$COLUMN_NAME_GACHA_TYPE = $gachaType",null,null,null,null)
            while (cursor.moveToNext()){
                list += UIGFExcelBean(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COUNT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GACHA_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ITEM_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LANG)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_RANK_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_UIGF_GACHA_TYPE))
                )
            }
            cursor.close()
        }
        return list
    }

    fun getMinGachaHistoryId(uid:String):Long{
        val maxIdNum = 1612303200000000000 //生成的 id 值不应大于 1612303200000000000
        var minIdNum = 0L

        val exists = checkGachaHistoryTableExists(uid)
        if(!exists){
            return minIdNum
        }

        val cursor = readableDatabase.query(getGachaHistoryTableName(uid),null,null,null,null,null,"$COLUMN_NAME_ID desc","0,1")
        if(cursor.moveToNext()){
            val minId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID)).toLong()
            if(minId<maxIdNum){
                minIdNum = minId
            }
        }
        cursor.close()
        return minIdNum
    }

    //添加历史记录账号
    fun addGachaHistoryAccount(uid:String){
        val gachaHistoryAccountList = mutableListOf<String>()
        JSONArray(sp.getString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST,"[]")).toList(gachaHistoryAccountList)

        if(gachaHistoryAccountList.indexOf(uid)==-1){
            gachaHistoryAccountList+= uid
        }

        sp.edit().apply{
            putString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST, GSON.toJson(gachaHistoryAccountList))
            apply()
        }
    }

    fun deleteGachaHistory(uid:String){
        val gachaHistoryAccountList = mutableListOf<String>()
        JSONArray(sp.getString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST,"[]")).toList(gachaHistoryAccountList)
        gachaHistoryAccountList.remove(uid)
        sp.edit().apply {
            putString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST, GSON.toJson(gachaHistoryAccountList))
            apply()
        }

        val sql = "drop table if exists ${getGachaHistoryTableName(uid)}"
        writableDatabase.execSQL(sql)
    }
}