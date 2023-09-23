package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty

object FormatMethod {

    fun getFormatMethod(value: Int) = when (value) {
        FightProperty.FIGHT_PROP_BASE_HP -> Method.Integer
        FightProperty.FIGHT_PROP_HP -> Method.Integer
        FightProperty.FIGHT_PROP_HP_PERCENT -> Method.Percent

        FightProperty.FIGHT_PROP_BASE_ATTACK -> Method.Integer
        FightProperty.FIGHT_PROP_ATTACK -> Method.Integer
        FightProperty.FIGHT_PROP_ATTACK_PERCENT -> Method.Percent

        FightProperty.FIGHT_PROP_BASE_DEFENSE -> Method.Integer
        FightProperty.FIGHT_PROP_DEFENSE -> Method.Integer
        FightProperty.FIGHT_PROP_DEFENSE_PERCENT -> Method.Percent

        FightProperty.FIGHT_PROP_CRITICAL -> Method.Percent
        FightProperty.FIGHT_PROP_CRITICAL_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_CHARGE_EFFICIENCY -> Method.Percent
        FightProperty.FIGHT_PROP_HEAL_ADD -> Method.Percent
        FightProperty.FIGHT_PROP_ELEMENT_MASTERY -> Method.Integer

        FightProperty.FIGHT_PROP_PHYSICAL_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_PHYSICAL_ADD_HURT -> Method.Percent

        FightProperty.FIGHT_PROP_FIRE_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ELEC_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_WATER_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_GRASS_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_WIND_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ROCK_ADD_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ICE_ADD_HURT -> Method.Percent

        FightProperty.FIGHT_PROP_FIRE_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ELEC_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_WATER_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_GRASS_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_WIND_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ROCK_SUB_HURT -> Method.Percent
        FightProperty.FIGHT_PROP_ICE_SUB_HURT -> Method.Percent

        FightProperty.FIGHT_PROP_MAX_HP -> Method.Integer
        FightProperty.FIGHT_PROP_CUR_ATTACK -> Method.Integer
        FightProperty.FIGHT_PROP_CUR_DEFENSE -> Method.Integer

        else -> Method.None
    }

    enum class Method {
        //无格式化
        None,
        //取整
        Integer,
        //百分比
        Percent
    }
}