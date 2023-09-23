package com.lianyi.paimonsnotebook.ui.overlay.debug.viewmodel

import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret

class DebugOverlayViewModel {
    val dsSaltType = buildMap {
        put(DynamicSecret.SaltType.PROD, DynamicSecret.PROD)
        put(DynamicSecret.SaltType.K2, DynamicSecret.K2)
        put(DynamicSecret.SaltType.LK2, DynamicSecret.LK2)
        put(DynamicSecret.SaltType.X4, DynamicSecret.X4)
        put(DynamicSecret.SaltType.X6, DynamicSecret.X6)
    }
    val dsVersion = buildMap {
        put(DynamicSecret.Version.Gen1, "Gen1")
        put(DynamicSecret.Version.Gen2, "Gen2")
    }

}