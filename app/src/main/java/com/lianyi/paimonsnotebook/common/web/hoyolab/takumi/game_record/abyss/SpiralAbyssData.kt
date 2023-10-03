package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss

data class SpiralAbyssData(
    val damage_rank: List<DamageRank>,
    val defeat_rank: List<DefeatRank>,
    val end_time: String,
    val energy_skill_rank: List<EnergySkillRank>,
    val floors: List<Floor>,
    val is_unlock: Boolean,
    val max_floor: String,
    val normal_skill_rank: List<NormalSkillRank>,
    val reveal_rank: List<RevealRank>,
    val schedule_id: Int,
    val start_time: String,
    val take_damage_rank: List<TakeDamageRank>,
    val total_battle_times: Int,
    val total_star: Int,
    val total_win_times: Int
){

    data class DamageRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class DefeatRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class EnergySkillRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class Floor(
        val icon: String,
        val index: Int,
        val is_unlock: Boolean,
        val levels: List<Level>,
        val ley_line_disorder: List<String>,
        val max_star: Int,
        val settle_date_time: Any,
        val settle_time: String,
        val star: Int
    )

    data class NormalSkillRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class RevealRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class TakeDamageRank(
        val avatar_icon: String,
        val avatar_id: Int,
        val rarity: Int,
        val value: Int
    )

    data class Level(
        val battles: List<Battle>,
        val bottom_half_floor_monster: List<HalfFloorMonster>,
        val index: Int,
        val max_star: Int,
        val star: Int,
        val top_half_floor_monster: List<HalfFloorMonster>
    )

    data class Battle(
        val avatars: List<Avatar>,
        val index: Int,
        val settle_date_time: SettleDateTime,
        val timestamp: String
    )

    data class HalfFloorMonster(
        val icon: String,
        val level: Int,
        val name: String
    )

    data class Avatar(
        val icon: String,
        val id: Int,
        val level: Int,
        val rarity: Int
    )

    data class SettleDateTime(
        val day: Int,
        val hour: Int,
        val minute: Int,
        val month: Int,
        val second: Int,
        val year: Int
    )
}
