package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.ReliquaryService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquarySetData

class ReliquaryScreenViewModel : ViewModel() {

    var loadingState by mutableStateOf(LoadingState.Loading)

    val reliquarySetList = mutableStateListOf<ReliquarySetData>()

    private val reliquaryService by lazy {
        ReliquaryService(
            onMissingFile = this::onMissingFile
        )
    }

    init {
        viewModelScope.launchIO {
            reliquarySetList.clear()
            reliquarySetList += reliquaryService.reliquarySetList.sortedByDescending { it.SetId }
        }
    }


    private fun onMissingFile() {
        this.loadingState = LoadingState.Error
    }

    fun getReliquaryStar(setId: Int) = reliquaryService.reliquaryMaxStarMap[setId] ?: 0

}