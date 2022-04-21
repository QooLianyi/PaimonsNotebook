package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.materials.WeeklyMaterial
import com.lianyi.paimonsnotebook.databinding.FragmentWeekMaterialsBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityGroupBinding
import com.lianyi.paimonsnotebook.databinding.ItemMaterialsGroupBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.ui.activity.detail.CharacterDetailActivity
import com.lianyi.paimonsnotebook.util.goA
import com.lianyi.paimonsnotebook.util.loadImage
import com.lianyi.paimonsnotebook.util.setViewMarginBottomByNavigationBarHeight

class WeekMaterialsFragment : BaseFragment(R.layout.fragment_week_materials) {
    lateinit var bind:FragmentWeekMaterialsBinding

    private lateinit var characterGroup:List<Pair<String, List<Pair<String, List<CharacterBean>>>>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWeekMaterialsBinding.bind(view)

        characterGroup = CharacterBean.characterList.groupBy { it.weeklyMaterials.key }.toList().groupBy { WeeklyMaterial.getGroupNameByKey(it.first) }.toList()

        bind.list.adapter = ReAdapter(characterGroup,R.layout.item_materials_group){
            view, pair, position ->
            val materialItem = ItemMaterialsGroupBinding.bind(view)

            materialItem.name.text = pair.first
            materialItem.list.adapter = ReAdapter(pair.second,R.layout.item_entity_group){
                view, pair, position ->
                val item = ItemEntityGroupBinding.bind(view)
                loadImage(item.dailyMaterial,pair.second.first().weeklyMaterials.icon)
                item.materialsName.text = pair.second.first().weeklyMaterials.name

                item.list.adapter = ReAdapter(pair.second,R.layout.item_entity){
                    view, characterBean, position ->
                    val characterItem = ItemEntityBinding.bind(view)
                    loadImage(characterItem.icon,characterBean.icon)
                    characterItem.name.text = characterBean.name
                    characterItem.type.setImageResource(Element.getImageResourceByType(characterBean.element))
                    characterItem.starBackground.setBackgroundResource(Star.getStarResourcesByStarNum(characterBean.star,false))

                    item.root.setOnClickListener {
                        CharacterDetailActivity.character = characterBean
                        goA<CharacterDetailActivity>()
                        activity?.overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.list)
    }
}