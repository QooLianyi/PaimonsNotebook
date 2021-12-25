package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.R

class Element {
    companion object{
        const val FIRE = 1001
        const val WATER = 1002
        const val ELECT = 1003
        const val GRASS = 1004
        const val ICE = 1005
        const val ROCK = 1006
        const val WIND = 1007

        fun getTypeByName(name: String):Int{
            return when(name){
                "火"-> FIRE
                "水"-> WATER
                "雷"-> ELECT
                "草"-> GRASS
                "冰"-> ICE
                "岩"->ROCK
                "风"-> WIND
                else-> -100
            }
        }

        fun getNameByType(type: Int):String{
            return when(type){
                FIRE-> "火"
                WATER-> "水"
                ELECT-> "雷"
                GRASS-> "草"
                ICE-> "冰"
                ROCK-> "岩"
                WIND-> "风"
                else-> "*ERROR*"
            }
        }

        fun getImageResourceByType(type: Int):Int{
            return when(type){
                FIRE-> R.drawable.icon_element_fire
                WATER-> R.drawable.icon_element_water
                ELECT-> R.drawable.icon_element_elect
                GRASS-> R.drawable.icon_element_grass
                ICE->  R.drawable.icon_element_ice
                ROCK-> R.drawable.icon_element_rock
                WIND-> R.drawable.icon_element_wind
                else-> 0
            }
        }

    }
}