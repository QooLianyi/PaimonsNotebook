package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lianyi.paimonsnotebook.R
import me.jessyan.autosize.internal.CustomAdapt

class CharacterFragment : Fragment(R.layout.fragment_character), CustomAdapt {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 730f
    }
}