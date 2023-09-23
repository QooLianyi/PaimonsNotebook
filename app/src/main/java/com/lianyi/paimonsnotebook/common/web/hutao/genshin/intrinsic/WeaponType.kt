package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic

import com.lianyi.paimonsnotebook.R

/*
* 武器类型
* */
object WeaponType {

    fun getWeaponTypeName(type: Int) =
        when (type) {
            WEAPON_SWORD_ONE_HAND -> "单手剑"
            WEAPON_CATALYST -> "法器"
            WEAPON_CLAYMORE -> "双手剑"
            WEAPON_BOW -> "弓"
            WEAPON_POLE -> "长枪"
            else -> ""
        }

    fun getWeaponGachaTypeBgResIdByType(type: Int) =
        when (type) {
            WEAPON_SWORD_ONE_HAND -> R.drawable.img_gacha_type_bg_sword
            WEAPON_CATALYST -> R.drawable.img_gacha_type_bg_catalyst
            WEAPON_CLAYMORE -> R.drawable.img_gacha_type_bg_claymore
            WEAPON_BOW -> R.drawable.img_gacha_type_bg_bow
            WEAPON_POLE -> R.drawable.img_gacha_type_bg_pole
            else -> -1
        }

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_NONE = 0

    /// <summary>
    /// 单手剑
    /// </summary>
    const val WEAPON_SWORD_ONE_HAND = 1

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_CROSSBOW = 2

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_STAFF = 3

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_DOUBLE_DAGGER = 4

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_KATANA = 5

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_SHURIKEN = 6

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_STICK = 7

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_SPEAR = 8

    /// <summary>
    /// ?
    /// </summary>
    const val WEAPON_SHIELD_SMALL = 9

    /// <summary>
    /// 法器
    /// </summary>
    const val WEAPON_CATALYST = 10

    /// <summary>
    /// 双手剑
    /// </summary>
    const val WEAPON_CLAYMORE = 11

    /// <summary>
    /// 弓
    /// </summary>
    const val WEAPON_BOW = 12

    /// <summary>
    /// 长柄武器
    /// </summary>
    const val WEAPON_POLE = 13
}