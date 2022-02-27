package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentEmptyBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment

class EmptyFragment : BaseFragment(R.layout.fragment_empty) {
    lateinit var bind:FragmentEmptyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentEmptyBinding.bind(view)
    }
}