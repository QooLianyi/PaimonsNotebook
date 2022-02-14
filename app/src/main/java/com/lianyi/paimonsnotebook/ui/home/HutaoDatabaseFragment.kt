package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentHutaoDatabaseBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment

class HutaoDatabaseFragment : BaseFragment(R.layout.fragment_hutao_database) {
    lateinit var bind:FragmentHutaoDatabaseBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentHutaoDatabaseBinding.bind(view)
    }
}