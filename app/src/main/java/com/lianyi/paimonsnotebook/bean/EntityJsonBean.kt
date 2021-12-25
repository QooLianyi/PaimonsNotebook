package com.lianyi.paimonsnotebook.bean

/*
*数据初始化时获取的JSON转为此类
*
* entity 实体信息
* entityType 实体类型 武器:大剑 单手剑 角色:元素类型
* equipType 装备类型 角色:角色使用的装备类型
* materialsList 所需的材料列表
* effectName 武器:武器效果名称
* effect 武器:武器效果内容
* area 角色:角色所属地区
* mainProperty 武器:主属性 角色:突破属性
* propertyList 副属性列表
* star 星级
* story 武器故事
* */

class EntityJsonBean(val entity:EntityBean,
                     val entityType:Int,
                     val equipType:Int,
                     val materialsList:List<Pair<String,String>>,
                     val effectName:String,
                     val effect:String,
                     val area:String,
                     val mainProperty:String,
                     val propertyList:List<String>,
                     val star:Int,
                     val story:String
                     ){

    override fun toString(): String {
        return "EntityJsonBean(entity=$entity, entityType=$entityType, equipType=$equipType, materialsList=$materialsList, effectName='$effectName', effect='$effect', area='$area', mainProperty='$mainProperty', propertyList=$propertyList, star=$star, story='$story')"
    }
}


