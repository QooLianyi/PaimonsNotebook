package com.lianyi.paimonsnotebook.bean


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
    override fun toString(): String {
        return "CharacterBean(area='$area', name='$name', weaponType=$weaponType, element=$element, baseHP='$baseHP', baseATK='$baseATK', baseDEF='$baseDEF', upAttribute='$upAttribute', upAttributeValue='$upAttributeValue', dailyMaterials=$dailyMaterials, weeklyMaterials=$weeklyMaterials, localMaterials=$localMaterials, monsterMaterials=$monsterMaterials, bossMaterial=$bossMaterial, icon='$icon', star=$star)"
    }
}