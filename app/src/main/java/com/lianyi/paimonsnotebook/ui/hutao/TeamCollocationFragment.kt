package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.TeamCollocationBean
import com.lianyi.paimonsnotebook.databinding.FragmentTeamCollocationBinding
import com.lianyi.paimonsnotebook.databinding.ItemCharacterSmallBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoTeamCollocationBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class TeamCollocationFragment : BaseFragment(R.layout.fragment_team_collocation) {
    lateinit var bind:FragmentTeamCollocationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentTeamCollocationBinding.bind(view)

        initView()
    }

    private fun initView() {
        HuTaoApi.get(HuTaoApi.TEAM_COLLOCATION){
            val list = mutableListOf<TeamCollocationBean>()
            JSONArray(it.optString("data")).toList(list)

            if(it.ok){
                activity?.runOnUiThread {
                    bind.part.list.adapter = ReAdapter(list,R.layout.item_hutao_team_collocation){
                        view, teamCollocationBean, position ->
                        val item = ItemHutaoTeamCollocationBinding.bind(view)
                        setListItemMargin(item.root,position,50)
                        val avatarsBean = AvatarsBean.avatarsMap[teamCollocationBean.avater]
                        if(avatarsBean!=null){
                            val character = CharacterBean.characterMap[avatarsBean.name]
                            if(character!=null){
                                item.character.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                item.character.type.setImageResource(Element.getImageResourceByType(character.element))
                            }
                            loadImage(item.character.icon,avatarsBean.url)
                            item.character.name.text = avatarsBean.name
                        }

                        item.list.adapter = ReAdapter(teamCollocationBean.collocations,R.layout.item_character_small){
                            view, collocationsBean, position ->
                            val characterItem = ItemCharacterSmallBinding.bind(view)
                            val avatarsItemBean = AvatarsBean.avatarsMap[collocationsBean.id]

                            if(avatarsItemBean!=null){
                                val character = CharacterBean.characterMap[avatarsItemBean.name]
                                if(character!=null){
                                    characterItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                }
                                loadImage(characterItem.icon,avatarsItemBean.url)
                                characterItem.valueName.gone()
                                characterItem.value.text = "${Format.DECIMALS_FORMAT.format(collocationsBean.value*100)}%"
                            }
                        }
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.part.list)
    }
}