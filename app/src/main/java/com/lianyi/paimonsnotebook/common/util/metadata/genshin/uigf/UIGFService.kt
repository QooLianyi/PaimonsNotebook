package com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.web.uigf.UIGFClient

class UIGFService {

    private val uigfClient by lazy {
        UIGFClient()
    }
}