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

        when(pagerIndex){
            PagerIndex.DAILY_MATERIALS_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,DailyMaterialsFragment())
            }
            PagerIndex.WEEK_MATERIALS_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,WeekMaterialsFragment())
            }
            PagerIndex.CHARACTER_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,CharacterFragment())
            }
            PagerIndex.WEAPON_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,WeaponFragment())
            }
            PagerIndex.MAP_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,MapFragment())
            }
            PagerIndex.WISH_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,WishFragment())
            }
            PagerIndex.SEARCH_PAGE->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,SearchFragment())
            }
            PagerIndex.HUTAO_DATABASE ->{
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,HutaoDatabaseFragment())
            }
            else -> {
                "意外的INDEX".show()
                supportFragmentManager.beginTransaction().add(R.id.fragment_container,EmptyFragment())
            }
        }.commit()

        setContentMargin(MainActivity.bind.root)
    }
}