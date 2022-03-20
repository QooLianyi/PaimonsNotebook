package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.ActivityFragmentContainerBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.MainActivity
import com.lianyi.paimonsnotebook.ui.home.*
import com.lianyi.paimonsnotebook.util.setContentMargin
import com.lianyi.paimonsnotebook.util.show

class FragmentContainerActivity : BaseActivity() {
    lateinit var bind:ActivityFragmentContainerBinding

    companion object{
        lateinit var pagerIndex:PagerIndex
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val page = when(pagerIndex){
            PagerIndex.DAILY_MATERIALS_PAGE->{
                DailyMaterialsFragment()
            }
            PagerIndex.WEEK_MATERIALS_PAGE->{
                WeekMaterialsFragment()
            }
            PagerIndex.CHARACTER_PAGE->{
               CharacterFragment()
            }
            PagerIndex.WEAPON_PAGE->{
               WeaponFragment()
            }
            PagerIndex.MAP_PAGE->{
               MapFragment()
            }
            PagerIndex.WISH_PAGE->{
               WishFragment()
            }
            PagerIndex.SEARCH_PAGE->{
               SearchFragment()
            }
            PagerIndex.HUTAO_DATABASE ->{
                HutaoDatabaseFragment()
            }
            PagerIndex.CULTIVATE_CALCULATE->{
                CultivateCalculateFragment()
            }
            else -> {
                "意外的INDEX".show()
                EmptyFragment()
            }
        }
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_sub,page).commit()
        setContentMargin(bind.root)
    }
}