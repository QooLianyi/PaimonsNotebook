package com.lianyi.paimonsnotebook.bean

import com.lianyi.paimonsnotebook.bean.materials.DailyMaterial
import com.lianyi.paimonsnotebook.bean.materials.MonsterMaterial
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.toList
import com.lianyi.paimonsnotebook.util.wsp
import org.json.JSONArray

/*
* name 名称
* weaponType 武器类型
* ATK 武器攻击力
* attributeName 主属性名称
* attributeNameValue 主属性值
* dailyMaterial 所需每日材料
* eliteMonsterMaterial 所需的怪物掉落材料(四星)
* monsterMaterial 所需怪物掉落材料 (三星)
* effectName 武器效果名称
* effect 武器效果
* story 武器故事
* describe 武器描述
* icon 图标
* star 星级
* */

data class WeaponBean(val name:String,
                 val weaponType:Int,
                 val ATK:String,
                 val attributeName:String,
                 val attributeNameValue:String,
                 val dailyMaterials: DailyMaterial,
                 val eliteMonsterMaterial: MonsterMaterial,
                 val monsterMaterials: MonsterMaterial,
                 val effectName:String,
                 val effect:String,
                 val describe:String,
                 val icon:String,
                 val star:Int
                    ) {

    companion object{
        val weaponList:MutableList<WeaponBean> by lazy {
            val list = mutableListOf<WeaponBean>()
            val weaponJsonArray = JSONArray(wsp.getString(JsonCacheName.WEAPON_LIST,"")!!)
            weaponJsonArray.toList(list)
            list
        }

        val weaponMap:MutableMap<String,WeaponBean> by lazy {
            val map = mutableMapOf<String,WeaponBean>()
            weaponList.forEach {
                map += it.name to it
            }
            map
        }

        fun getWeaponByName(name:String):WeaponBean?{
            return weaponMap[name]
        }
    }

    override fun toString(): String {
        return "WeaponBean(name='$name', weaponType=$weaponType, ATK='$ATK', attributeName='$attributeName', attributeNameValue='$attributeNameValue', dailyMaterials=$dailyMaterials, eliteMonsterMaterial=$eliteMonsterMaterial, monsterMaterials=$monsterMaterials, effectName='$effectName', effect='$effect', describe='$describe', icon='$icon', star=$star)"
    }
}