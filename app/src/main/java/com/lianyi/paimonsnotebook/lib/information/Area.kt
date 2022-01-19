package com.lianyi.paimonsnotebook.lib.information

class Area {
    companion object{
        const val MONDSTADT = "Mondstadt"
        const val LIYUE = "Liyue"
        const val INAZUMA = "Inazuma"

        fun getNameByArea(area:String):String{
            return when(area){
                MONDSTADT ->"蒙德"
                LIYUE->"璃月"
                INAZUMA->"稻妻"
                else->"*ERROR*"
            }
        }

    }
}
