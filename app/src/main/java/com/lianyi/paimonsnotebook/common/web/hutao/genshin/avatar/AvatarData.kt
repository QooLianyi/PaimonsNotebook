package com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AssociationIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AvatarIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.GachaAvatarIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.GachaAvatarImgConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.SkillIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.WeaponTypeIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.WeaponType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AvatarData(
    @SerializedName("BaseValue")
    val baseValue: BaseValue,
    @SerializedName("BeginTime")
    val beginTime: String,
    @SerializedName("Body")
    val body: Int,
    @SerializedName("Costumes")
    val costumes: List<Costume>,
    @SerializedName("CultivationItems")
    val cultivationItems: List<Int>,
    @SerializedName("Description")
    val description: String,
    @SerializedName("FetterInfo")
    val fetterInfo: FetterInfo,
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
    @SerializedName("Quality")
    val quality: Int,
    @SerializedName("SideIcon")
    val sideIcon: String,
    @SerializedName("SkillDepot")
    val skillDepot: SkillDepot,
    @SerializedName("Sort")
    val sort: Int,
    @SerializedName("Weapon")
    val weapon: Int
) {

    //图标url
    val iconUrl: String
        get() = AvatarIconConverter.iconNameToUrl(icon)

    //角色祈愿立绘
    val gachaAvatarImg: String
        get() = GachaAvatarImgConverter.iconNameToUrl(icon)

    //角色祈愿图标图
    val gachaAvatarIcon: String
        get() = GachaAvatarIconConverter.iconNameToUrl(icon)

    //武器图标url
    val weaponIconUrl: String
        get() = WeaponTypeIconConverter.weaponTypeToIconUrl(weapon)

    //武器名称
    val weaponTypeName: String
        get() = WeaponType.getWeaponTypeName(weapon)

    val qualityBgResId: Int
        get() = QualityType.getQualityBgByType(quality)

    //星数
    val starCount: Int
        get() = QualityType.getStarCountByType(quality)

    //实装日期
    val beginTimeFormat: String
        get() = LocalDateTime.parse(beginTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME).let {
            "${it.year}年${it.monthValue}月${it.dayOfMonth}日"
        }

    data class BaseValue(
        val AttackBase: Float,
        val DefenseBase: Float,
        val HpBase: Float
    )

    data class Costume(
        val Description: String,
        val FrontIcon: String,
        val Id: Int,
        val IsDefault: Boolean,
        val Name: String,
        val SideIcon: String
    )

    data class FetterInfo(
        val Association: Int,
        val BirthDay: Int,
        val BirthMonth: Int,
        val ConstellationAfter: String,
        val ConstellationBefore: String,
        val CookBonus: CookBonus,
        val CvChinese: String,
        val CvEnglish: String,
        val CvJapanese: String,
        val CvKorean: String,
        val Detail: String,
        val FetterStories: List<FetterStory>,
        val Fetters: List<Fetter>,
        val Native: String,
        val Title: String,
        val VisionAfter: String,
        val VisionBefore: String
    ) {
        //元素属性图标资源id
        val elementIconResId: Int
            get() = ElementType.getElementResourceIdByName(VisionBefore)

        //元素颜色
        val elementColor
            get() = ElementType.getElementColorByName(VisionBefore)

        //地区图标url
        val associationIconUrl: String
            get() = AssociationIconConverter.avatarAssociationToUrl(Association)

        val elementType: Int
            get() = ElementType.getElementTypeByName(VisionBefore)
    }

    data class GrowCurve(
        val Type: Int,
        val Value: Int
    )

    data class SkillDepot(
        val EnergySkill: Skill,
        val Inherents: List<Inherent>,
        val Skills: List<Skill>,
        val Talents: List<Talent>
    ) {
        val skillIds: List<Int>
            get() = Skills.map { it.GroupId } + EnergySkill.GroupId

        val skillIdMap: Map<Int, Skill>
            get() = (Skills + EnergySkill).associateBy {
                it.GroupId
            }
    }

    data class CookBonus(
        val InputList: List<Int>,
        val ItemId: Int,
        val OriginItemId: Int
    )

    data class FetterStory(
        val Context: String,
        val Title: String
    )

    data class Fetter(
        val Context: String,
        val Title: String
    )

    data class Inherent(
        val Description: String,
        val GroupId: Int,
        val Icon: String,
        val Id: Int,
        val Name: String,
        val Proud: Proud
    )

    data class Skill(
        val Description: String,
        val GroupId: Int,
        val Icon: String,
        val Id: Int,
        val Name: String,
        val Proud: Proud
    ) {
        val iconUrl: String
            get() = SkillIconConverter.iconNameToUrl(name = Icon)
    }

    data class Talent(
        val Description: String,
        val Icon: String,
        val Id: Int,
        val Name: String
    )

    data class Proud(
        val Descriptions: List<String>,
        val Parameters: List<Parameter>
    ) {

        private val parametersMap
            get() = Parameters.associateBy { it.Level }

        companion object {
            private val regex by lazy {
                Regex("\\{([^{}]*)\\}")
            }

            private fun getFormatStringByType(
                value: Float,
                letter: String,
                number: Int = -1
            ): String {

                val count = if (number != -1) {
                    number
                } else {
                    val regex = Regex("\\d+")
                    val result = regex.find(letter)
                    result?.value?.toInt() ?: 0
                }

                return when (letter[0]) {
                    'P', 'p' -> {
                        "${"%.${count}f".format(value * 100f)}%"
                    }

                    'F', 'f' -> {
                        if (letter.last() == 'P' || letter.last() == 'p') {
                            getFormatStringByType(value, "P", number = count)
                        } else {
                            "%.${count}f".format(value)
                        }
                    }

                    'I', 'i' -> {
                        "${value.toInt()}"
                    }

                    else -> {
                        "$value"
                    }
                }
            }
        }

        /*
        * 获得描述
        *
        * level:技能等级
        * */
        fun descriptions(level: Int): List<Pair<String, String>> = try {
            val regex1 = Regex("\\d+")
            Descriptions.mapIndexed { index, s ->
                val result = regex.findAll(s)

                var res = s
                result.forEach { findResult ->
                    val findResultValue = findResult.value
                    val split = findResultValue.split(":")
                    val sp1 = split.first().removePrefix("{")
                    val sp2 = split.last().removeSuffix("}")

                    val paramIndex = regex1.find(sp1)?.value?.toInt()

                    val value =
                        if (paramIndex != null && parametersMap[level] != null) parametersMap[level]!!.Parameters[paramIndex - 1] else Parameters[level].Parameters[index]

                    val formatString = getFormatStringByType(value, sp2)

                    res = res.replace(findResultValue, formatString)
                }
                res.split("|").let {
                    it.first() to it.last()
                }
            }
        } catch (_: Exception) {
            listOf()
        }
    }

    data class Parameter(
        val Level: Int,
        val Parameters: List<Float>
    )
}
