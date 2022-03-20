package com.lianyi.paimonsnotebook.ui.summerland

import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.ActivitySummerLandBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity

class SummerLandActivity : BaseActivity() {
//    val bind by las<ActivitySummerLandBinding>()
    lateinit var bind: ActivitySummerLandBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySummerLandBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val loadingFragment = SummerLandLoadingFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_sub,loadingFragment).commit()
        val isLand = SummerLandLoadingLandAnimFragment(700){
            supportFragmentManager.beginTransaction().remove(loadingFragment).commit()
        }
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_sup,isLand).commit()
    }
}