package com.lianyi.paimonsnotebook.lib.information

class GachaType {
    companion object{
        const val NEW_PLAYERS = 100
        const val PERMANENT = 200
        const val CHARACTER_ACTIVITY_01 = 301
        const val CHARACTER_ACTIVITY_02 = 400

        const val WEAPON = 302


        const val ERROR = "*ERROR*"

        val gachaList = listOf(
            NEW_PLAYERS,
            PERMANENT,
            CHARACTER_ACTIVITY_01,
            WEAPON
        )

        fun getNameByType(type:Int):String{
            return when(type){
                NEW_PLAYERS->"新手祈愿"
                PERMANENT->"常驻祈愿"
                CHARACTER_ACTIVITY_01,CHARACTER_ACTIVITY_02->"角色活动祈愿"
                WEAPON->"武器活动祈愿"
                else-> ERROR
            }
        }

        fun getUIGFType(type: Int):String{
            return when(type){
                NEW_PLAYERS->"100"
                PERMANENT->"200"
                CHARACTER_ACTIVITY_01,CHARACTER_ACTIVITY_02-> "301"
                WEAPON-> "302"
                else-> "1000"
            }
        }

        fun getUIGFType(type: String):String{
            return when(type){
                "301","400"-> "301"
                else-> type
            }
        }

    }
}