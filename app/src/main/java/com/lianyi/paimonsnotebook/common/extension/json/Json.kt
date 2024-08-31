package com.lianyi.paimonsnotebook.common.extension.json

import com.google.gson.stream.JsonReader

//将指针移动到key所在的value处
fun JsonReader.findField(key: String): Boolean {
    beginObject()
    while (hasNext()) {
        val name = nextName()

        if (name == key) {
            return true
        }

        skipValue()
    }

    endObject()

    return false
}