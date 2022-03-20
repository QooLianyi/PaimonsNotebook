package com.lianyi.paimonsnotebook.bean.materials

data class DailyMaterial(val area:String,val name:String,val star:Int,val key:String,val icon:String) {
    companion object{
        private val dropDay1 = listOf(1,4,7)
        private val dropDay2 = listOf(2,5,7)
        private val dropDay3 = listOf(3,6,7)
        private val dropDayError = listOf(0)
        fun getDropDayByKey(name: String):List<Int>{
            return when(name){
                //繁荣 自由 浮世 孤云 高塔 远海
                "Talent_Prosperity","Talent_Freedom","Talent_Transience","Weapon_Guyun","Weapon_Decarabian","Weapon_DistantSea"-> dropDay1
                //抗争 勤劳 风雅 奔狼 雾海 鸣神
                "Talent_Resistance","Talent_Diligence","Talent_Elegance","Weapon_BorealWolf","Weapon_MistVeiled","Weapon_Narukami"-> dropDay2
                //诗文 黄金 天光 狮牙 陨铁 面具
                "Talent_Ballad","Talent_Gold","Talent_Light","Weapon_DandelionGladiator","Weapon_Mask","Weapon_Aerosiderite"-> dropDay3
                else-> dropDayError
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyMaterial

        if (area != other.area) return false
        if (name != other.name) return false
        if (star != other.star) return false
        if (key != other.key) return false
        if (icon != other.icon) return false

        return true
    }


}