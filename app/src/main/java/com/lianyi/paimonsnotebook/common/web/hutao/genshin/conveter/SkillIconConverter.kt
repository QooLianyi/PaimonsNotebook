package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint


object SkillIconConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        if(name.startsWith("UI_Talent_")){
            "Talent"
        }else{
            "Skill"
        },
        "${name}.png"
    )
}