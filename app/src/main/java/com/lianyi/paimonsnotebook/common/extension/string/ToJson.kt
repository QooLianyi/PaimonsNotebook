package com.lianyi.paimonsnotebook.common.extension.string

import com.lianyi.paimonsnotebook.common.util.json.JSON
import java.lang.reflect.ParameterizedType

inline fun <reified T> String.toJSON(type: ParameterizedType? = null): T {
    return if (type==null){
        JSON.parse(this)
    }else{
        JSON.parse(this,type)
    }
}
