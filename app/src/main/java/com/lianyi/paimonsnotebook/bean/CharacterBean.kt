package com.lianyi.paimonsnotebook.bean

import com.lianyi.paimonsnotebook.bean.materials.*
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.csp
import com.lianyi.paimonsnotebook.util.toList
import org.json.JSONArray


/*
* area 地区
* name 名称
* weaponType 武器类型
* element 元素类型
* baseHP 基础生命
* baseATK 基础攻击力
* base DEF 基础防御力
* upAttribute 突破主属性
* upAttribute 突破主属性值
* dailyMaterial 所需每日材料
* weeklyMaterial 所需每周材料
* lockMaterial 所需采集材料
* monsterMaterial 所需怪物掉落材料
* bossMaterial 突破所需boss材料
* icon 图标
* star 星级
* */

class CharacterBean(val area:String,
                    val name:String,
                    val weaponType:Int,
                    val element:Int,
                    val baseHP:String,
                    val baseATK:String,
                    val baseDEF:String,
                    val upAttribute:String,
                    val upAttributeValue:String,
                    val dailyMaterials: DailyMaterial,
                    val weeklyMaterials: WeeklyMaterial,
                    val localMaterials: LocalMaterial,
                    val monsterMaterials: MonsterMaterial,
                    val bossMaterial: BossMaterial,
                    val icon:String,
                    val star:Int
                    ) {

    companion object{
        val characterList:MutableList<CharacterBean> by lazy {
            val list = mutableListOf<CharacterBean>()
            val characterJsonArray = JSONArray(csp.getString(JsonCacheName.CHARACTER_LIST,"")!!)
            characterJsonArray.toList(list)
            list
        }

        val characterMap:MutableMap<String,CharacterBean> by lazy {
            val map = mutableMapOf<String,CharacterBean>()
            characterList.forEach {
                map += it.name to it
            }
            map
        }

        fun getCharacterByName(name:String):CharacterBean?{
            return characterMap[name]
        }
    }

    override fun toString(): String {
        return "CharacterBean(area='$area', name='$name', weaponType=$weaponType, element=$element, baseHP='$baseHP', baseATK='$baseATK', baseDEF='$baseDEF', upAttribute='$upAttribute', upAttributeValue='$upAttributeValue', dailyMaterials=$dailyMaterials, weeklyMaterials=$weeklyMaterials, localMaterials=$localMaterials, monsterMaterials=$monsterMaterials, bossMaterial=$bossMaterial, icon='$icon', star=$star)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterBean

        if (area != other.area) return false
        if (name != other.name) return false
        if (weaponType != other.weaponType) return false
        if (element != other.element) return false
        if (baseHP != other.baseHP) return false
        if (baseATK != other.baseATK) return false
        if (baseDEF != other.baseDEF) return false
        if (upAttribute != other.upAttribute) return false
        if (upAttributeValue != other.upAttributeValue) return false
        if (dailyMaterials != other.dailyMaterials) return false
        if (weeklyMaterials != other.weeklyMaterials) return false
        if (localMaterials != other.localMaterials) return false
        if (monsterMaterials != other.monsterMaterials) return false
        if (bossMaterial != other.bossMaterial) return false
        if (icon != other.icon) return false
        if (star != other.star) return false

        return true
    }

    override fun hashCode(): Int {
        var result = area.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + weaponType
        result = 31 * result + element
        result = 31 * result + baseHP.hashCode()
        result = 31 * result + baseATK.hashCode()
        result = 31 * result + baseDEF.hashCode()
        result = 31 * result + upAttribute.hashCode()
        result = 31 * result + upAttributeValue.hashCode()
        result = 31 * result + dailyMaterials.hashCode()
        result = 31 * result + weeklyMaterials.hashCode()
        result = 31 * result + localMaterials.hashCode()
        result = 31 * result + monsterMaterials.hashCode()
        result = 31 * result + bossMaterial.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + star
        return result
    }

}