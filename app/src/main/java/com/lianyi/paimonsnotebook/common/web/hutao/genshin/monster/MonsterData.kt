package com.lianyi.paimonsnotebook.common.web.hutao.genshin.monster

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.MonsterIconConverter

data class MonsterData(
    @SerializedName("Arkhe")
    val arkhe: Int,
    @SerializedName("BaseValue")
    val baseValue: BaseValue,
    @SerializedName("DescribeId")
    val describeId: Int,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Drops")
    val drops: List<Int>,
    @SerializedName("GrowCurves")
    val growCurves: List<GrowCurve>,
    @SerializedName("Icon")
    val icon: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("MonsterName")
    val monsterName: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("RelationshipId")
    val relationshipId: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: Int
){

    val iconUrl:String
        get() = MonsterIconConverter.iconNameToUrl(icon)

    data class BaseValue(
        val AttackBase: Float,
        val DefenseBase: Int,
        val ElecSubHurt: Float,
        val FireSubHurt: Float,
        val GrassSubHurt: Float,
        val HpBase: Float,
        val IceSubHurt: Float,
        val PhysicalSubHurt: Float,
        val RockSubHurt: Float,
        val WaterSubHurt: Float,
        val WindSubHurt: Float
    )

    data class GrowCurve(
        val Type: Int,
        val Value: Int
    )
}