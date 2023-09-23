package com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.EquipIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.GachaEquipImgConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.WeaponTypeIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.WeaponType

data class WeaponData(
    @SerializedName("Affix")
    val affix: Affix?,
    @SerializedName("AwakenIcon")
    val awakenIcon: String,
    @SerializedName("CultivationItems")
    val cultivationItems: List<Int>,
    @SerializedName("Description")
    val description: String,
    @SerializedName("GrowCurves")
    val growCurves: List<GrowCurve>,
    @SerializedName("Icon")
    val icon: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PromoteId")
    val promoteId: Int,
    @SerializedName("RankLevel")
    val rankLevel: Int,
    @SerializedName("WeaponType")
    val weaponType: Int
){

    val iconUrl:String
        get() = EquipIconConverter.iconNameToUrl(icon)

    val awakenIconUrl:String
        get() = EquipIconConverter.iconNameToUrl(awakenIcon)

    val weaponGachaTypeBgResId:Int
        get() = WeaponType.getWeaponGachaTypeBgResIdByType(weaponType)

    val weaponIconUrl:String
        get() = WeaponTypeIconConverter.weaponTypeToIconUrl(weaponType)

    val weaponTypeName:String
        get() = WeaponType.getWeaponTypeName(weaponType)

    val gachaEquipImg:String
        get() = GachaEquipImgConverter.iconNameToUrl(icon)

    val qualityBgResId:Int
        get() = QualityType.getQualityBgByType(rankLevel)

    val maxLevel:Int
        get() = if(rankLevel >= 3) 90 else 70

    data class Affix(
        val Descriptions: List<Description>,
        val Name: String
    )

    data class GrowCurve(
        val InitValue: Float,
        val Type: Int,
        val Value: Int
    )

    data class Description(
        val Description: String,
        val Level: Int
    )
}