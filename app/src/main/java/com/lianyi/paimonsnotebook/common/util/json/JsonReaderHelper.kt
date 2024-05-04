package com.lianyi.paimonsnotebook.common.util.json

import com.google.gson.stream.JsonReader

object JsonReaderHelper {

    /*
    * 从jsonReader中获取指定key的value
    * */
    fun getJsonReaderSingleFieldValue(
        reader: JsonReader,
        key: String,
        onFound: (JsonReader) -> Unit,
        onNotFound: () -> Unit,
        onObjectEnd: () -> Unit = {},
        autoClose: Boolean = true
    ) {
        reader.apply {
            beginObject()
            while (hasNext()) {
                val name = nextName()

                if (name == key) {
                    onFound.invoke(this)

                    if (autoClose) {
                        close()
                    }
                    return@apply
                }

                skipValue()
            }

            endObject()
            onNotFound.invoke()

            if (autoClose) {
                close()
            }
        }

        onObjectEnd.invoke()
    }

}