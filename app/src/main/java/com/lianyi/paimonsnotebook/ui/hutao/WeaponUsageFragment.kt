package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.WeaponUsageBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.WeaponsBean
import com.lianyi.paimonsnotebook.databinding.FragmentWeaponUsageBinding
import com.lianyi.paimonsnotebook.databinding.ItemCharacterSmallBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoWeaponUseageBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class WeaponUsageFragment : BaseFragment(R.layout.fragment_weapon_usage) {
    lateinit var bind:FragmentWeaponUsageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWeaponUsageBinding.bind(view)

        initView()
    }

    private fun initView() {
        HuTaoApi.get(HuTaoApi.WEAPON_USAGE){
            if(it.ok){
                val list = mutableListOf<WeaponUsageBean>()
                JSONArray(it.optString("data")).toList(list)
                activity?.runOnUiThread {
                    bind.part.list.adapter = ReAdapter(list,R.layout.item_hutao_weapon_useage){
                        view, weaponUsageBean, position ->
                        val item = ItemHutaoWeaponUseageBinding.bind(view)
                        setListItemMargin(item.root,position,50)
                        val avatarsBean = AvatarsBean.avatarsMap[weaponUsageBean.avatar]
                        if(avatarsBean!=null){
                            val character = CharacterBean.characterMap[avatarsBean.name]
                            if(character!=null){
                                item.character.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                item.character.type.setImageResource(Element.getImageResourceByType(character.element))
                            }
                            loadImage(item.character.icon,avatarsBean.url)
                            item.character.name.text = avatarsBean.name
                        }

                        item.list.adapter = ReAdapter(weaponUsageBean.weapons,R.layout.item_character_small){
                                view, weaponsBean, position ->
                            val characterItem = ItemCharacterSmallBinding.bind(view)
                            val weapon = WeaponsBean.weaponMap[weaponsBean.id]
1
                            if(weapon!=null){
                                val character = WeaponBean.weaponMap[weapon.name]
                                if(character!=null){
                                    characterItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                }
                                loadImage(characterItem.icon,weapon.url)
                                characterItem.valueName.gone()
                                characterItem.value.text = "${Format.DECIMALS_FORMAT.format(weaponsBean.value*100)}%"
                            }
                        }
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.part.list)
    }
}