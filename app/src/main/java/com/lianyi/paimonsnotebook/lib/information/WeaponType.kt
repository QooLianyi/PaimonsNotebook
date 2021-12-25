package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.R

class WeaponType {
    companion object{
        const val ONE_HAND_SWORD = 2001
        const val BOTH_HAND_SWORD = 2002
        const val BOW_AND_ARROW = 2003
        const val MAGIC_ARTS = 2004
        const val SPEAR = 2005

        fun getNameByType(type:Int):String{
            return when(type){
                ONE_HAND_SWORD->"单手剑"
                BOTH_HAND_SWORD->"双手剑"
                BOW_AND_ARROW->"弓"
                MAGIC_ARTS->"法器"
                SPEAR->"长柄武器"
                else->"*ERROR*"
            }
        }

        fun getTypeByName(name:String):Int{
            return when(name){
                "单手剑"->ONE_HAND_SWORD
                "双手剑"-> BOTH_HAND_SWORD
                "弓"->BOW_AND_ARROW
                "法器"->MAGIC_ARTS
                "长柄武器"->SPEAR
                else->-100
            }
        }

        fun getResourceByType(type:Int):Int{
            return when(type){
                ONE_HAND_SWORD-> R.drawable.icon_one_hand_sword
                BOTH_HAND_SWORD-> R.drawable.icon_both_hand_sword
                BOW_AND_ARROW-> R.drawable.icon_bow_and_arrow
                MAGIC_ARTS-> R.drawable.icon_magic_arts
                SPEAR-> R.drawable.icon_spear
                else ->0
            }
        }

    }
}