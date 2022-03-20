package com.lianyi.paimonsnotebook.lib.information

enum class PagerIndex {

    HOME_PAGE,
    DAILY_MATERIALS_PAGE,
    WEEK_MATERIALS_PAGE,
    CHARACTER_PAGE,
    WEAPON_PAGE,
    MAP_PAGE,
    WISH_PAGE,
    SEARCH_PAGE,
    HUTAO_DATABASE,
    SUMMER_LAND,
    CULTIVATE_CALCULATE,
    EMPTY_PAGE;

    companion object{
        const val HUTAO_AVATAR_PARTICIPATION = 0
        const val HUTAO_CONSTELLATION = 1
        const val HUTAO_TEAM_COLLOCATION = 2
        const val HUTAO_WEAPON_USAGE = 3
        const val HUTAO_AVATAR_RELIQUARY_USAGE = 4
        const val HUTAO_TEAM_COMBINATION = 5
    }
}