package com.lianyi.paimonsnotebook.ui.screen.items.util

/*
* 角色搜索过滤类型
*
* 排序与搜索条件均使用该枚举类
* */
enum class ItemFilterType {
    //排序方式
    Default,
    //名称
    Name,
    //星级
    Star,
    //武器
    Weapon,
    //元素
    Element,
    //基础生命值
    BaseHp,
    //基础攻击力
    BaseATK,
    //基础防御力
    BaseDef,
    //服装数量
    CostumeCount,
    //生日
    BirthDay,
    //区域
    Association,
    //列表布局类型
    ListLayout,


    //等级
    Level,
    //好感
    Fetter,
    //解锁的名座个数
    ActiveConstellation,
}