package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.activity.WeaponDetailActivity
import com.lianyi.paimonsnotebook.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.EntityJsonBean
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.config.WeaponType
import com.lianyi.paimonsnotebook.databinding.FragmentWeaponBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityBinding
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class WeaponFragment : BaseFragment(R.layout.fragment_weapon) {
    lateinit var bind: FragmentWeaponBinding

    private val weaponList = mutableListOf<EntityJsonBean>()
    private val weaponShowList = mutableListOf<EntityJsonBean>()
    private val selectWeapon = mutableListOf<Int>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWeaponBinding.bind(view)

        val weaponJsonArray = JSONArray(wsp.getString(JsonCacheName.WEAPON_LIST,""))

        weaponJsonArray.toList(weaponList)
        weaponList.forEach {
            weaponShowList +=it
        }

        weaponShowList.sortByDescending { it.star }
        bind.list.adapter = ReAdapter(weaponShowList,R.layout.item_entity){
            viewItem, entityJsonBean, position ->
            val item = ItemEntityBinding.bind(viewItem)
            item.name.text = entityJsonBean.entity.name
            loadImage(item.icon,entityJsonBean.entity.iconUrl)
            item.type.setImageResource(WeaponType.getResourceByType(entityJsonBean.entityType))
            item.root.setOnClickListener {
                WeaponDetailActivity.detailInformation = entityJsonBean
                goA<WeaponDetailActivity>()
                activity?.overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
            }
        }
        initSelect()
    }

    private fun notificationUpdate(){
        weaponShowList.clear()
        weaponList.forEach { entity->
            if(selectWeapon.indexOf(entity.entityType)!=-1){
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

            selectTypeOneHandSword.select {
                if(it)
                    selectWeapon.add(WeaponType.ONE_HAND_SWORD)
                else
                    selectWeapon.remove(WeaponType.ONE_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBothHandSword.select {
                if(it)
                    selectWeapon.add(WeaponType.BOTH_HAND_SWORD)
                else
                    selectWeapon.remove(WeaponType.BOTH_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBowAndArrow.select {
                if(it)
                    selectWeapon.add(WeaponType.BOW_AND_ARROW)
                else
                    selectWeapon.remove(WeaponType.BOW_AND_ARROW)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectMagicArts.select {
                if(it)
                    selectWeapon.add(WeaponType.MAGIC_ARTS)
                else
                    selectWeapon.remove(WeaponType.MAGIC_ARTS)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectSpear.select {
                if(it)
                    selectWeapon.add(WeaponType.SPEAR)
                else
                    selectWeapon.remove(WeaponType.SPEAR)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }

        }
    }

}