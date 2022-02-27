package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarReliquaryUsageBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.ReliquariesBean
import com.lianyi.paimonsnotebook.databinding.FragmentAvatarReliquaryUsageBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoAvatarReliquaryBinding
import com.lianyi.paimonsnotebook.databinding.ItemHutaoAvatarReliquaryUsageBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.listener.RecyclerViewPagerPreloadListener
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class AvatarReliquaryUsageFragment : BaseFragment(R.layout.fragment_avatar_reliquary_usage) {
    lateinit var bind:FragmentAvatarReliquaryUsageBinding

    val dataSet = mutableListOf<AvatarReliquaryUsageBean>()
    val showDataSet = mutableListOf<AvatarReliquaryUsageBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind = FragmentAvatarReliquaryUsageBinding.bind(view)

        initView()
    }

    private fun initView() {
        Ok.hutaoGet(HuTaoApi.AVATAR_RELIQUARY_USAGE){
            if(it.ok){
                val list = mutableListOf<AvatarReliquaryUsageBean>()
                JSONArray(it.optString("data")).toList(list)

                list.sortByDescending { it.avatar }
                list.copy(dataSet)

                addData(0)
                activity?.runOnUiThread {
                    bind.part.list.adapter = ReAdapter(showDataSet,R.layout.item_hutao_avatar_reliquary_usage){
                        view, avatarReliquaryUsageBean, position ->
                        var startHeight = 20

                        val item = ItemHutaoAvatarReliquaryUsageBinding.bind(view)
                        setListItemMargin(item.root,position,50)

                        val avatarsBean = AvatarsBean.avatarsMap[avatarReliquaryUsageBean.avatar]
                        if(avatarsBean!=null){
                            val character = CharacterBean.characterMap[avatarsBean.name]
                            if(character!=null){
                                item.character.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                                item.character.type.setImageResource(Element.getImageResourceByType(character.element))
                            }
                            loadImage(item.character.icon,avatarsBean.url)
                            item.character.name.text = avatarsBean.name
                        }

                        item.showContent.select {
                            if(it){
                                item.list.measure(0,0)
                                openAndCloseAnimationVer(item.root,startHeight,item.list.measuredHeight+40.dp.toInt(),500)
                                item.dropDown.rotation = 0f
                            }else{
                                openAndCloseAnimationVer(item.root,item.list.measuredHeight+40.dp.toInt(),startHeight,500)
                                item.dropDown.rotation = 180f
                            }
                            item.dropDown.animate().rotationBy(180f).duration = 500
                        }

                        avatarReliquaryUsageBean.reliquaryUsage.sortByDescending { it.value }

                        item.list.adapter = ReAdapter(avatarReliquaryUsageBean.reliquaryUsage,R.layout.item_hutao_avatar_reliquary){
                            view, reliquaryUsageBean, position ->
                            val itemReliquaryUsage = ItemHutaoAvatarReliquaryBinding.bind(view)

                            val reliquaryUsageList = reliquaryUsageBean.id.split(";")

                            reliquaryUsageList.forEachIndexed{ index,string->
                                val name = string.split("-")
                                val id = name.first().toInt()
                                val count = name.last()

                                val reliquary = ReliquariesBean.reliquariesMap[id]

                                if(reliquary!=null){
                                    when(index){
                                        0->{
                                            loadImage(itemReliquaryUsage.icon1,reliquary.url)
                                            itemReliquaryUsage.name1.text = "${count}x${reliquary.name}"
                                        }
                                        else->{
                                            loadImage(itemReliquaryUsage.icon2,reliquary.url)
                                            itemReliquaryUsage.name2.text = "${count}x${reliquary.name}"
                                            itemReliquaryUsage.name2.show()
                                        }
                                    }
                                }
                            }
                            itemReliquaryUsage.value.text = "${Format.DECIMALS_FORMAT.format(reliquaryUsageBean.value*100)}%"

                            startHeight += when(position){
                                0,1->{
                                    itemReliquaryUsage.root.measure(0,0)
                                    itemReliquaryUsage.root.marginTop+ itemReliquaryUsage.root.marginBottom+ itemReliquaryUsage.root.measuredHeight + 10
                                }

                                2->{
                                    val lp = item.root.layoutParams
                                    lp.apply {
                                        height = startHeight
                                    }
                                    item.root.layoutParams = lp
                                    0
                                }
                                else->{
                                    0
                                }
                            }
                        }
                    }
                    val listener =  object :
                        RecyclerViewPagerPreloadListener(bind.part.list.layoutManager as LinearLayoutManager){
                        override fun onLoadMore(current_page: Int) {
                            if(current_page!=dataSet.size){
                                addData(current_page)
                            }
                        }
                    }
                    bind.part.list.addOnScrollListener(listener)
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.part.list)
    }

    private fun addData(index:Int){
        if(showDataSet.size<=dataSet.size){
            showDataSet.add(dataSet[index])
        }
    }
}