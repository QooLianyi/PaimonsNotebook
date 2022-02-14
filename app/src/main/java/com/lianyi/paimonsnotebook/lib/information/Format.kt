package com.lianyi.paimonsnotebook.lib.information

import java.text.DecimalFormat
import java.text.SimpleDateFormat

class Format {
    companion object{
        val TIME = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val TIME_FULL = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val TIME_WEEK = SimpleDateFormat("E")
        val TIME_DAY = SimpleDateFormat("d")
        val TIME_MONTH = SimpleDateFormat("M")
        val TIME_DAY_FULL = SimpleDateFormat("yyyy-MM-dd")
        val TIME_HOUR_MINUTE = SimpleDateFormat("HH:mm")

        val DECIMALS_FORMAT = DecimalFormat("0.##")


        fun getRecoverTimeDayText(recoverTime:Long):String{
            val todayLimitTime = TIME_DAY_FULL.format(System.currentTimeMillis())+" 23:59:59"
            val todayLimit = TIME_FULL.parse(todayLimitTime).time

            val day = if(System.currentTimeMillis()+recoverTime*1000L>todayLimit){
                "明天"
            }else{
                "今天"
            }
            val recoverFullTime = TIME_HOUR_MINUTE.format((System.currentTimeMillis()+recoverTime*1000L))
            return day+recoverFullTime
        }

        fun getRecoverTimeHourText(recoverTime:Long):String{
            return if(recoverTime>3600){
                "${recoverTime/3600}小时${recoverTime%60}分钟"
            }else{
                "${recoverTime%60}分钟"
            }
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