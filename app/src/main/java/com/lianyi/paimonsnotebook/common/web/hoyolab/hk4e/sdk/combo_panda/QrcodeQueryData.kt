package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.sdk.combo_panda

import com.lianyi.paimonsnotebook.common.util.json.JSON

data class QrcodeQueryData(
    val payload: Payload,
    val realname_info: Any,
    val stat: String
) {
    data class Payload(
        val ext: String,
        val proto: String,
        val raw: String
    ) {
        //是否确认登录
        val success:Boolean
            get() = proto == "Account"

        fun getRawData() =
            JSON.parse<Raw>(raw)

        data class Raw(
            val token: String,
            val uid: String
        )
    }

}

