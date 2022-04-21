package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarParticipationBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.databinding.FragmentAvatarParticipationBinding
import com.lianyi.paimonsnotebook.databinding.ItemCharacterBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoAvatarParticipationBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class AvatarParticipationFragment : BaseFragment(R.layout.fragment_avatar_participation) {
    lateinit var bind:FragmentAvatarParticipationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentAvatarParticipationBinding.bind(view)

        initView()
    }

    private fun initView() {
        HuTaoApi.get(HuTaoApi.AVATAR_PARTICIPATION){
            if(it.ok){
                val list = mutableListOf<AvatarParticipationBean>()
                JSONArray(it.optString("data")).toList(list)
                list.sortByDescending { it.floor }

                runOnUiThread {
                    bind.part.list.adapter = ReAdapter(list,R.layout.item_hutao_avatar_participation){
                        view, avatarParticipationBean, position ->

                        val item = ItemHutaoAvatarParticipationBinding.bind(view)
                        item.floor.text = avatarParticipationBean.floor.toString()
                        setListItemMargin(item.root,position,50)

                        avatarParticipationBean.avatarUsage.sortByDescending { it.value }

                        item.showContent.select {
                            if(it){
                                item.list.measure(0,0)
                                openAndCloseAnimationVer(item.floorSpan,180.dp.toInt(),item.list.measuredHeight+40.dp.toInt(),500)
                                item.dropDown.rotation = 0f
                            }else{
                                openAndCloseAnimationVer(item.floorSpan,item.list.measuredHeight+40.dp.toInt(),180.dp.toInt(),500)
                                item.dropDown.rotation = 180f
                            }
                            item.dropDown.animate().rotationBy(180f).duration = 500
                        }

                        //禁止滑动
                        item.list.layoutManager = object : StaggeredGridLayoutManager(4, VERTICAL){
                            override fun canScrollVertically(): Boolean = false
                        }
                        item.list.adapter = ReAdapter(avatarParticipationBean.avatarUsage,R.layout.item_character){
                            view, avatarUsageBean, position ->

                            val characterItem = ItemCharacterBinding.bind(view)
                            val avatarsBean = AvatarsBean.avatarsMap[avatarUsageBean.id]

                            if(avatarsBean!=null){
                                val character = CharacterBean.characterMap[avatarsBean.name]
                                if(character!=null){
                                    characterItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                    characterItem.type.setImageResource(Element.getImageResourceByType(character.element))

                                }
                                loadImage(characterItem.icon,avatarsBean.url)
                                characterItem.name.text = avatarsBean.name
                            }
                            characterItem.valueName.gone()
                            characterItem.value.text = "${Format.DECIMALS_FORMAT.format(avatarUsageBean.value*100)}%"
                        }
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.part.list)
    }

    private fun runOnUiThread(block:()->Unit){
        activity?.runOnUiThread {
            block()
        }
    }
}