package com.lianyi.paimonsnotebook.ui.summerland

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentSummerLandLoadingBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.util.las
import com.lianyi.paimonsnotebook.util.onPlaying

class SummerLandLoadingFragment(var block: ((progress:Float) -> Unit)? =null) : BaseFragment(R.layout.fragment_summer_land_loading) {
    lateinit var bind: FragmentSummerLandLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = view.las()
        bind.loadAnim.onPlaying {
            if(block!=null){
                block!!(it)
            }
        }
    }
}