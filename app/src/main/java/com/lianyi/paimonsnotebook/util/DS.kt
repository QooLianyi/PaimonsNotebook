package com.lianyi.paimonsnotebook.util

import com.lianyi.paimonsnotebook.config.Settings
import java.security.MessageDigest


fun getDS(query:String,body:String =""):String{
    val time: Long = System.currentTimeMillis()/1000L
    val rs: String = getRS()
    val cs: String = md5("salt=${Settings.API_SALT}&t=${time}&r=${rs}&b=$body&q=$query")
    return "${time},${rs},${cs}"
}

fun getSignDS(salt:String):String{
    val time: Long = System.currentTimeMillis()/1000L
    val rs: String = getRS()
    val cs: String = md5("salt=$salt&t=${time}&r=${rs}")
    return "${time},${rs},${cs}"
}


fun getRS():String{
    val rsKey = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val rs = java.lang.StringBuilder()

    (0..5).forEach {
        rs.append(rsKey.random())
    }

    return rs.toString()
}


fun md5(input:String):String{
    val md =MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())

    with(StringBuilder()){
        digest.forEach {
            val hex = it.toInt() and  (0xFF)
            val toHexString = Integer.toHexString(hex)
            if (toHexString.length ==1){
                this.append("0$toHexString")
            }else{
                this.append(toHexString)
            }
        }
        return this.toString()
    }

}


fun getQ1(uid:String,server:String):String{
    return "role_id=$uid&server=$server"
}


