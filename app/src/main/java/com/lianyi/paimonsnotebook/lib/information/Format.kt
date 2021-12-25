package com.lianyi.paimonsnotebook.lib.information

import java.text.SimpleDateFormat

class Format {
    companion object{
        val TIME = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val TIME_FULL = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val TIME_WEEK = SimpleDateFormat("E")
        val TIME_DAY = SimpleDateFormat("d")
        val TIME_DAY_FULL = SimpleDateFormat("yyyy-MM-dd")
        val TIME_HOUR_MINUTE = SimpleDateFormat("HH:mm")

        fun getResinRecoverTime(resinRecoverTime:Long):String{
            val todayLimitTime = TIME_DAY_FULL.format(System.currentTimeMillis())+" 23:59:59"
            val todayLimit = TIME_FULL.parse(todayLimitTime).time

            val day = if(System.currentTimeMillis()+resinRecoverTime*1000L>todayLimit){
                "明天"
            }else{
                "今天"
            }
            val recoverFullTime = TIME_HOUR_MINUTE.format((System.currentTimeMillis()+resinRecoverTime*1000L))
            return day+recoverFullTime
        }

        fun getWeekByName(name:String):Int{
            return when(name){
                "周一"->0
                "周二"->1
                "周三"->2
                "周四"->3
                "周五"->4
                "周六"->5
                else-> 6
            }
        }
    }
}