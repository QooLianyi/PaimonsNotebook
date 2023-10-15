package com.lianyi.paimonsnotebook.common.extension.intent

import android.content.Intent
import com.lianyi.paimonsnotebook.common.util.activity_code.ActivityCode

fun Intent.setResultCode(code:Int){
    this.putExtra(ActivityCode.EXTRA_RESULT_CODE,code)
}