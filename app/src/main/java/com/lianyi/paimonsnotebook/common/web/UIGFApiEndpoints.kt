package com.lianyi.paimonsnotebook.common.web

object UIGFApiEndpoints {

    private const val UIGFApi = "https://api.uigf.org"


    fun getGameLangByItemNameUrl(game: String = "genshin", text: String = "chs") =
        "${UIGFApi}/${game}/${text}"

    fun getUIGFItemDictJsonUrl(game: String = "genshin", lang: String = "chs") =
        "${UIGFApi}/dict/${game}/${lang}.json"

    fun getUIGFItemMD5ValidateMapUrl(game: String = "genshin") =
        "${UIGFApi}/md5/${game}"

}