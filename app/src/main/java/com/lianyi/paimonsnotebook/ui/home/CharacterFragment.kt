package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.activity.CharacterDetailActivity
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.databinding.FragmentCharacterBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.information.WeaponType
import com.lianyi.paimonsnotebook.util.*

class CharacterFragment : BaseFragment(R.layout.fragment_character) {

    lateinit var bind: FragmentCharacterBinding
    private val characterShowList = mutableListOf<CharacterBean>()
    //选择的属性和装备类型
    private val selectProperty = mutableListOf<Int>()
    private val selectEquipType = mutableListOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentCharacterBinding.bind(view)

        CharacterBean.characterList.forEach {
            characterShowList+=it
        }
        //根据星级排序
        characterShowList.sortByDescending { it.star }

        bind.list.adapter = ReAdapter(characterShowList,R.layout.item_entity){
                view, character, position ->
            val item = ItemEntityBinding.bind(view)
            item.name.text = character.name
            loadImage(item.icon,character.icon)
            item.type.setImageResource(Element.getImageResourceByType(character.element))
            item.starBackground.setBackgroundResource(Star.getStarResourcesByStarNum(character.star,false))

            item.root.setOnClickListener {
                CharacterDetailActivity.character = character
                goA<CharacterDetailActivity>()
                activity?.overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
            }
        }
        initSelect()
    }

    //筛选
    private fun notificationUpdate(){
        characterShowList.clear()
        CharacterBean.characterList.forEach { entity->
            selectProperty.forEach { property->
                if(entity.element==property)
                    characterShowList += entity
            }
        }
        val deleteList = mutableListOf<CharacterBean>()
        characterShowList.forEach { entity->
            if(selectEquipType.indexOf(entity.weaponType)==-1){
                deleteList += entity
            }
        }
        deleteList.forEach {
            characterShowList.remove(it)
        }
        characterShowList.sortByDescending { it.star }
        activity?.runOnUiThread {
            bind.list.adapter?.notifyDataSetChanged()
            bind.selectSpan.isEnabled = true
        }
    }

    private fun initSelect(){

        with(bind){

            selectProperty += Element.FIRE
            selectProperty += Element.WATER
            selectProperty += Element.GRASS
            selectProperty += Element.ICE
            selectProperty += Element.WIND
            selectProperty += Element.ELECT
            selectProperty += Element.ROCK

            selectEquipType += WeaponType.ONE_HAND_SWORD
            selectEquipType += WeaponType.BOTH_HAND_SWORD
            selectEquipType += WeaponType.BOW_AND_ARROW
            selectEquipType += WeaponType.MAGIC_ARTS
            selectEquipType += WeaponType.SPEAR


            selectFire.select {
                if(it)
                    selectProperty.add(Element.FIRE)
                else
                    selectProperty.remove(Element.FIRE)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectWater.select {
                if(it)
                    selectProperty.add(Element.WATER)
                else
                    selectProperty.remove(Element.WATER)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectGrass.select {
                if(it)
                    selectProperty.add(Element.GRASS)
                else
                    selectProperty.remove(Element.GRASS)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectIce.select {
                if(it)
                    selectProperty.add(Element.ICE)
                else
                    selectProperty.remove(Element.ICE)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectWind.select {
                if(it)
                    selectProperty.add(Element.WIND)
                else
                    selectProperty.remove(Element.WIND)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectElect.select {
                if(it)
                    selectProperty.add(Element.ELECT)
                else
                    selectProperty.remove(Element.ELECT)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectRock.select {
                if(it)
                    selectProperty.add(Element.ROCK)
                else
                    selectProperty.remove(Element.ROCK)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }

            selectTypeOneHandSword.select {
                if(it)
                    selectEquipType.add(WeaponType.ONE_HAND_SWORD)
                else
                    selectEquipType.remove(WeaponType.ONE_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBothHandSword.select {
                if(it)
                    selectEquipType.add(WeaponType.BOTH_HAND_SWORD)
                else
                    selectEquipType.remove(WeaponType.BOTH_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBowAndArrow.select {
                if(it)
                    selectEquipType.add(WeaponType.BOW_AND_ARROW)
                else
                    selectEquipType.remove(WeaponType.BOW_AND_ARROW)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectMagicArts.select {
                if(it)
                    selectEquipType.add(WeaponType.MAGIC_ARTS)
                else
                    selectEquipType.remove(WeaponType.MAGIC_ARTS)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectSpear.select {
                if(it)
                    selectEquipType.add(WeaponType.SPEAR)
                else
                    selectEquipType.remove(WeaponType.SPEAR)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }

        }
    }

}