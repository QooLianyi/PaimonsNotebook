package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.AssociationType
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object AssociationIconConverter {

    //角色所属地区图标
    fun avatarAssociationToUrl(association:Int):String {
        val icon = when(association){
            AssociationType.ASSOC_TYPE_INAZUMA-> "Inazuma"
            AssociationType.ASSOC_TYPE_MONDSTADT-> "MengDe"
            AssociationType.ASSOC_TYPE_LIYUE-> "LiYue"
            AssociationType.ASSOC_TYPE_SUMERU-> "Sumeru"
            AssociationType.ASSOC_TYPE_FONTAINE-> "Fontaine"
            else-> "GoldenAppleIsles"
        }

        return HutaoEndpoints.staticFile("LoadingPic","UI_LoadingPic_${icon}.png")
    }
}