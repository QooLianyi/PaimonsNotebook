package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character

data class CharacterDetailData(
    val list: List<DetailItem>,
    val property_map: Map<String, PropertyMapData>,
    val relic_property_options: RelicPropertyOptions
) {

    data class DetailItem(
        val base: Base,
        val base_properties: List<BaseProperty>,
        val constellations: List<Constellation>,
        val costumes: List<Any>,
        val element_properties: List<ElementProperty>,
        val extra_properties: List<ExtraProperty>,
        val recommend_relic_property: RecommendRelicProperty,
        val relics: List<Relic>,
        val selected_properties: List<SelectedProperty>,
        val skills: List<Skill>,
        val weapon: WeaponX
    )

    data class PropertyMapData(
        val filter_name: String,
        val icon: String,
        val name: String,
        val property_type: Int
    )

    data class RelicPropertyOptions(
        val circlet_main_property_list: List<Int>,
        val goblet_main_property_list: List<Int>,
        val sand_main_property_list: List<Int>,
        val sub_property_list: List<Int>
    )


    data class Base(
        val actived_constellation_num: Int,
        val element: String,
        val fetter: Int,
        val icon: String,
        val id: Int,
        val image: String,
        val is_chosen: Boolean,
        val level: Int,
        val name: String,
        val rarity: Int,
        val side_icon: String,
        val weapon: Weapon,
        val weapon_type: Int
    )

    data class BaseProperty(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

    data class Constellation(
        val effect: String,
        val icon: String,
        val id: Int,
        val is_actived: Boolean,
        val name: String,
        val pos: Int
    )

    data class ElementProperty(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

    data class ExtraProperty(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

    data class RecommendRelicProperty(
        val custom_properties: Any,
        val has_set_recommend_prop: Boolean,
        val recommend_properties: RecommendProperties
    )

    data class Relic(
        val icon: String,
        val id: Int,
        val level: Int,
        val main_property: MainProperty,
        val name: String,
        val pos: Int,
        val pos_name: String,
        val rarity: Int,
        val `set`: Set,
        val sub_property_list: List<SubProperty>
    )

    data class SelectedProperty(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

    data class Skill(
        val desc: String,
        val icon: String,
        val is_unlock: Boolean,
        val level: Int,
        val name: String,
        val skill_affix_list: List<SkillAffix>,
        val skill_id: Int,
        val skill_type: Int
    )

    data class WeaponX(
        val affix_level: Int,
        val desc: String,
        val icon: String,
        val id: Int,
        val level: Int,
        val main_property: MainPropertyX,
        val name: String,
        val promote_level: Int,
        val rarity: Int,
        val sub_property: SubPropertyX,
        val type: Int,
        val type_name: String
    )

    data class Weapon(
        val affix_level: Int,
        val icon: String,
        val id: Int,
        val level: Int,
        val rarity: Int,
        val type: Int
    )

    data class RecommendProperties(
        val circlet_main_property_list: List<Int>,
        val goblet_main_property_list: List<Int>,
        val sand_main_property_list: List<Int>,
        val sub_property_list: List<Int>
    )

    data class MainProperty(
        val property_type: Int,
        val times: Int,
        val value: String
    )

    data class Set(
        val affixes: List<Affixe>,
        val id: Int,
        val name: String
    )

    data class SubProperty(
        val property_type: Int,
        val times: Int,
        val value: String
    )

    data class Affixe(
        val activation_number: Int,
        val effect: String
    )

    data class SkillAffix(
        val name: String,
        val value: String
    )

    data class MainPropertyX(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

    data class SubPropertyX(
        val add: String,
        val base: String,
        val `final`: String,
        val property_type: Int
    )

}