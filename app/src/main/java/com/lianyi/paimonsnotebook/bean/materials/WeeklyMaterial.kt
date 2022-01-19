package com.lianyi.paimonsnotebook.bean.materials

class WeeklyMaterial(val area:String,val name:String,val star:Int,val key:String,val icon:String) {
    companion object{
        fun getGroupNameByKey(key: String):String{
            return when(key){
                "Weekly_DvalinsPlume","Weekly_DvalinsClaw","Weekly_DvalinsSigh"->"风魔龙"
                "Weekly_RingofBoreas","Weekly_SpiritLocketofBoreas","Weekly_TailofBoreas"->"北风的王狼"
                "Weekly_GildedScale","Weekly_BloodjadeBranch","Weekly_DragonLordsCrown"->"若托龙王"
                "Weekly_ShadowoftheWarrior","Weekly_ShardofaFoulLegacy","Weekly_TuskofMonocerosCaeli"->"公子"
                "Weekly_MoltenMoment","Weekly_HellfireButterfly","Weekly_AshenHeart"->"女士"
                else ->"*ERROR*"
            }
        }
    }
}