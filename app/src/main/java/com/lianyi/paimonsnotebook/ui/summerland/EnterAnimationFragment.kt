package com.lianyi.paimonsnotebook.ui.summerland

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentEnterAnimationBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment

class EnterAnimationFragment : BaseFragment(R.layout.fragment_enter_animation) {
    lateinit var bind:FragmentEnterAnimationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentEnterAnimationBinding.bind(view)
    }

}