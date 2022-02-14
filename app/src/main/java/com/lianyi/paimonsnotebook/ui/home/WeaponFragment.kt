package com.lianyi.paimonsnotebook.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.activity.WeaponDetailActivity
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.bean.config.SideBarButtonSettings
import com.lianyi.paimonsnotebook.databinding.FragmentWeaponBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.information.WeaponType
import com.lianyi.paimonsnotebook.ui.MainActivity
import com.lianyi.paimonsnotebook.ui.activity.CharacterDetailActivity
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class WeaponFragment : BaseFragment(R.layout.fragment_weapon) {

    private val weaponList = mutableListOf<WeaponBean>()
    private val weaponShowList = mutableListOf<WeaponBean>()
    private val selectWeapon = mutableListOf<Int>()

    companion object{
        private lateinit var bind: FragmentWeaponBinding

        fun initConfig() {
            if(SideBarButtonSettings.instance.enabled){
                bind.selectSpan.setOnClickListener {
                    MainActivity.bind.container.openDrawer(MainActivity.bind.navViewSpan)
                }
            }else{
                bind.selectSpan.setOnClickListener {
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWeaponBinding.bind(view)

        initView()
        initSelect()
        initConfig()
    }

    private fun initView() {
        JSONArray(wsp.getString(JsonCacheName.WEAPON_LIST,"")).toList(weaponList)

        weaponList.forEach {
            weaponShowList +=it
        }

        weaponShowList.sortByDescending { it.star }
        bind.list.adapter = ReAdapter(weaponShowList,R.layout.item_entity){
                viewItem, weapon, position ->
            val item = ItemEntityBinding.bind(viewItem)
            item.name.text = weapon.name
            loadImage(item.icon,weapon.icon)
            item.type.setImageResource(WeaponType.getResourceByType(weapon.weaponType))
            item.starBackground.setBackgroundResource(Star.getStarResourcesByStarNum(weapon.star,false))

            item.root.setOnClickListener {
                WeaponDetailActivity.weapon = weapon
                startActivity(
                    Intent(activity!!, WeaponDetailActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(activity!!,item.icon,"icon").toBundle()
                )
            }
        }

        setViewMarginBottomByNavigationBarHeight(bind.list)
    }

    private fun notificationUpdate(){
        weaponShowList.clear()
        weaponList.forEach { entity->
            if(selectWeapon.indexOf(entity.weaponType)!=-1){
                weaponShowList += entity
            }
        }
        weaponShowList.sortByDescending { it.star }
        activity?.runOnUiThread {
            bind.list.adapter?.notifyDataSetChanged()
            bind.selectSpan.isEnabled = true
        }
    }

    private fun initSelect(){
        with(bind){
            selectWeapon += WeaponType.ONE_HAND_SWORD
            selectWeapon += WeaponType.BOTH_HAND_SWORD
            selectWeapon += WeaponType.BOW_AND_ARROW
            selectWeapon += WeaponType.MAGIC_ARTS
            selectWeapon += WeaponType.SPEAR

            val selectWeaponViews = listOf(selectOneHandSword,selectBothHandSword,selectBowAndArrow,selectMagicArts,selectSpear)
            val selectWeapons = listOf(WeaponType.ONE_HAND_SWORD,WeaponType.BOTH_HAND_SWORD,WeaponType.BOW_AND_ARROW,WeaponType.MAGIC_ARTS,WeaponType.SPEAR)

            selectWeaponViews.forEachIndexed{index, selectView ->
                selectView.select {
                    if(it)
                        selectWeapon.add(selectWeapons[index])
                    else
                        selectWeapon.remove(selectWeapons[index])
                    notificationUpdate()
                }
            }
        }
    }
}