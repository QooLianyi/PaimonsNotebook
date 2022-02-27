package com.lianyi.paimonsnotebook.ui.home

import android.app.ActivityOptions
import android.content.Intent
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

    //展示的角色列表
    private val characterShowList = mutableListOf<CharacterBean>()
    //选择的属性和装备类型
    private val selectProperty = mutableListOf<Int>()
    private val selectEquipType = mutableListOf<Int>()
    private lateinit var bind: FragmentCharacterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentCharacterBinding.bind(view)

        initView()
        initSelect()
    }

    private fun initView() {
        bind.rank.setOnClickListener {
            "数值排行榜:将在未来实现".show()
        }

        CharacterBean.characterList.copy(characterShowList)
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
                startActivity(
                    Intent(activity!!,CharacterDetailActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(activity!!,item.icon,"icon").toBundle()
                )
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.list)
    }

    //更新列表
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


            val selectElementViews = listOf(selectFire,selectWater,selectGrass,selectIce,selectWind,selectElect,selectRock)
            val selectElements = listOf(Element.FIRE,Element.WATER,Element.GRASS,Element.ICE,Element.WIND,Element.ELECT,Element.ROCK)

            selectElementViews.forEachIndexed{index, selectView ->
                selectView.select {
                    if(it)
                        selectProperty.add(selectElements[index])
                    else
                        selectProperty.remove(selectElements[index])
                    notificationUpdate()
                }
            }

            val selectWeaponViews = listOf(selectOneHandSword,selectBothHandSword,selectBowAndArrow,selectMagicArts,selectSpear)
            val selectWeapons = listOf(WeaponType.ONE_HAND_SWORD,WeaponType.BOTH_HAND_SWORD,WeaponType.BOW_AND_ARROW,WeaponType.MAGIC_ARTS,WeaponType.SPEAR)

            selectWeaponViews.forEachIndexed{index, selectView ->
                selectView.select {
                    if(it)
                        selectEquipType.add(selectWeapons[index])
                    else
                        selectEquipType.remove(selectWeapons[index])
                    notificationUpdate()
                }
            }
        }
    }
}