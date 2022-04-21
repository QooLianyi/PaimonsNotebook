package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.ConstellationBean
import com.lianyi.paimonsnotebook.databinding.FragmentConstellationBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoConstellationBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoConstellationTextBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class ConstellationFragment : BaseFragment(R.layout.fragment_constellation) {
    lateinit var bind: FragmentConstellationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentConstellationBinding.bind(view)

        initView()
    }

    private fun initView() {
        HuTaoApi.get(HuTaoApi.CONSTELLATION){
            if(it.ok){
                val list = mutableListOf<ConstellationBean>()
                JSONArray(it.optString("data")).toList(list)
                list.sortByDescending { it.holdingRate }
                activity?.runOnUiThread {
                    bind.part.list.adapter = ReAdapter(list,R.layout.item_hutao_constellation){
                        view, constellationBean, position ->
                        val item = ItemHutaoConstellationBinding.bind(view)
                        val avatarsBean = AvatarsBean.avatarsMap[constellationBean.avatar]
                        setListItemMargin(item.root,position,50)
                        if(avatarsBean!=null){
                            val character = CharacterBean.characterMap[avatarsBean.name]
                            if(character!=null){
                                item.character.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                item.character.type.setImageResource(Element.getImageResourceByType(character.element))
                            }
                            loadImage(item.character.icon,avatarsBean.url)
                            item.character.name.text = avatarsBean.name
                        }
                        item.character.value.gone()
                        item.character.valueName.text = "${Format.DECIMALS_FORMAT.format(constellationBean.holdingRate*100)}%"

                        item.list.adapter = ReAdapter(constellationBean.rate,R.layout.item_hutao_constellation_text){
                            view, rateBean, position ->
                            val constellationTextItem = ItemHutaoConstellationTextBinding.bind(view)
                            constellationTextItem.index.text = "${rateBean.id}命"
                            constellationTextItem.value.text = "${Format.DECIMALS_FORMAT.format(rateBean.value*100)}%"
                        }
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.part.list)
    }
}