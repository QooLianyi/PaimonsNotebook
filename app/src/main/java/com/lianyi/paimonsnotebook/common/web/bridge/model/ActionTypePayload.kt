package com.lianyi.paimonsnotebook.common.web.bridge.model

import com.google.gson.annotations.SerializedName

data class ActionTypePayload(
    @SerializedName("action_type")
    val actionType:String
)
