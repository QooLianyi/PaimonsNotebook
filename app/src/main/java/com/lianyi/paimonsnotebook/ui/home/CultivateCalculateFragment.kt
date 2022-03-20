package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentCultivateCalculateBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.util.las

class CultivateCalculateFragment : BaseFragment(R.layout.fragment_cultivate_calculate) {
    lateinit var bind:FragmentCultivateCalculateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = view.las()

        bind.switchButton.apply {
            setOffText("总览")
            setOnText("列表")
            callback = {
            }
        }
    }
}