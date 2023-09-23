package com.lianyi.paimonsnotebook.common.extension.map

val Map<String, String>.cookieString: String
    get() = this.let {
        val sb = StringBuffer()
        this.map {
            sb.append("${it.key}=${it.value};")
        }
        sb.toString()
    }
