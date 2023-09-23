package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object SkillIconConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        if(name.startsWith("UI_Talent_")){
            "Talent"
        }else{
            "Skill"
        },
        "${name}.png"
    )
}