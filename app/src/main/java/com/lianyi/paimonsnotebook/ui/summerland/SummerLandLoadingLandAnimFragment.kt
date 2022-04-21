package com.lianyi.paimonsnotebook.ui.summerland

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentSummerLandLoadingLandAnimBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread

class SummerLandLoadingLandAnimFragment(val delayTime:Long = 0L,var block:(()->Unit)?=null) : BaseFragment(R.layout.fragment_summer_land_loading_land_anim) {
    lateinit var bind:FragmentSummerLandLoadingLandAnimBinding

    var playMark = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = view.las()
        bind.root.gone()

        bind.workTable.setOnClickListener {
            showAlertDialog(bind.root.context,R.layout.pop_clock)
        }
    }

    override fun onResume() {
        super.onResume()
        if(playMark){
            thread {
                Thread.sleep(delayTime)
                activity?.runOnUiThread {
                    playAnim()
                    playMark = false
                }
            }
        }
    }

    private fun playAnim(){
        val anim = AnimationUtils.loadAnimation(bind.root.context,R.anim.fragment_entry_from_bottom)
        anim.duration = 1000L
        bind.root.animation = anim
        anim.start()
        bind.root.show()

        thread {
            Thread.sleep(600)
            activity?.runOnUiThread {
                bind.loadAnim.transitionToEnd()
                bind.loadAnim.onFinished {
                    block?.invoke()
                    "点击合成台可唤出时钟\n(无实际作用)".showLong()
                }
            }
        }
    }
}