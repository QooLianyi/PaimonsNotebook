package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.ItemMaterialBinding
import com.lianyi.paimonsnotebook.ui.activity.WeaponDetailActivity
import com.lianyi.paimonsnotebook.util.loadImage

class Star {
    companion object{
        fun getStarResourcesByStarNum(star:Int, small:Boolean):Int{
            return if(small){
                when(star){
                    5-> R.drawable.icon_star_5s
                    4-> R.drawable.icon_star_4s
                    3-> R.drawable.icon_star_3s
                    2-> R.drawable.icon_star_2s
                    else-> R.drawable.icon_star_1s
                }
            }else{
                when(star){
                    5-> R.drawable.icon_star_5
                    4-> R.drawable.icon_star_4
                    3-> R.drawable.icon_star_3
                    2-> R.drawable.icon_star_2
                    else-> R.drawable.icon_star_1
                }
            }
        }

        fun getStarSymbolByStarNum(star: Int):String{
            return when(WeaponDetailActivity.weapon.star){
                5->"★★★★★"
                4->"★★★★"
                3->"★★★"
                2->"★★"
                else->"★"
            }
        }

        fun setStarBackgroundAndIcon(bind:ItemMaterialBinding,icon:String,star:Int){
            bind.starBackground.setImageResource(Star.getStarResourcesByStarNum(star,false))
            loadImage(bind.icon,icon)
        }

    }
}