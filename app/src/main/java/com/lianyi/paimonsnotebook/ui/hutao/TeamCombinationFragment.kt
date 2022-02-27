package com.lianyi.paimonsnotebook.ui.hutao

import android.os.Bundle
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.AvatarsBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.TeamCombinationBean
import com.lianyi.paimonsnotebook.bean.materials.*
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapterPreload
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.listener.RecyclerViewPagerPreloadListener
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class TeamCombinationFragment : BaseFragment(R.layout.fragment_team_combination) {
    lateinit var bind:FragmentTeamCombinationBinding

    var dataSet = mutableListOf<TeamCombinationBean>()
    var showDataSet = mutableListOf<TeamCombinationBean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind = FragmentTeamCombinationBinding.bind(view)

        initView()
    }

    private fun initView() {
        Ok.hutaoGet(HuTaoApi.TEAM_COMBINATION){
            if(it.ok){
                val list = mutableListOf<TeamCombinationBean>()
                JSONArray(it.optString("data")).toList(list)

                list.sortByDescending { it.level.floor*10 + it.level.index }
                list.copy(dataSet)

                addData(0)
                activity?.runOnUiThread {
                    bind.part.list.adapter = ReAdapter(showDataSet,R.layout.item_hutao_team_combination){
                        view, teamCombinationBean, position ->
                        val item = ItemHutaoTeamCombinationBinding.bind(view)
                        setListItemMargin(item.root,position,50)

                        var startHeight = 50

                        item.index.text = "${teamCombinationBean.level.floor}-${teamCombinationBean.level.index}"

                        item.showContent.select {
                            if(it){
                                item.list.measure(0,0)
                                openAndCloseAnimationVer(item.root,startHeight,item.list.measuredHeight+60.dp.toInt(),500)
                                item.dropDown.rotation = 0f
                            }else{
                                openAndCloseAnimationVer(item.root,item.list.measuredHeight+60.dp.toInt(),startHeight,500)
                                item.dropDown.rotation = 180f
                            }
                            item.dropDown.animate().rotationBy(180f).duration = 500
                        }

                        teamCombinationBean.teams.sortByDescending { it.value }
                        val teams = mutableListOf<TeamCombinationBean.TeamsBean>()

                        repeat(8){
                            teams += teamCombinationBean.teams[it]
                        }

                        item.list.adapter = ReAdapter(teams,R.layout.item_hutao_combination){
                            view, teamsBean, position ->
                            val hutaoCombinationItem = ItemHutaoCombinationBinding.bind(view)

                            val characterList = mutableListOf<List<CharacterBean>>()
                            characterList += getCharacterListByIds(teamsBean.id.upHalf.split(","))
                            characterList += getCharacterListByIds(teamsBean.id.downHalf.split(","))

                            hutaoCombinationItem.value.text = "${teamsBean.value}次"

                            hutaoCombinationItem.list.adapter = ReAdapter(characterList,R.layout.item_grid_layout_list){
                                view, list, position ->
                                val gridListItem = ItemGridLayoutListBinding.bind(view)

                                gridListItem.list.adapter = ReAdapter(list,R.layout.item_character_small){
                                        view, characterBean, position ->
                                    val characterItem = ItemCharacterSmallBinding.bind(view)
                                    if(characterBean.star!=0){
                                        characterItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(characterBean.star,false))
                                    }
                                    loadImage(characterItem.icon,characterBean.icon)
                                    characterItem.valueName.gone()
                                    characterItem.value.text = characterBean.name
                                }
                            }

                            startHeight += when(position){
                                0,1,2,3->{
                                    hutaoCombinationItem.root.measure(0,0)
                                    hutaoCombinationItem.root.marginTop + hutaoCombinationItem.root.marginBottom + hutaoCombinationItem.root.measuredHeight + hutaoCombinationItem.root.paddingTop +hutaoCombinationItem.root.paddingBottom +20
                                }
                                else->{
                                    0
                                }
                            }

                            if(position==3||position==teams.size-1){
                                val lp = item.root.layoutParams
                                lp.apply {
                                    height = startHeight
                                }
                                item.root.layoutParams = lp
                            }
                        }
                    }

                    val listener =  object :RecyclerViewPagerPreloadListener(bind.part.list.layoutManager as LinearLayoutManager){
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

    private fun getCharacterListByIds(ids:List<String>):List<CharacterBean>{
        val list = mutableListOf<CharacterBean>()
        ids.forEach {
            val avatarsBean = AvatarsBean.avatarsMap[it.toInt()]
            if(avatarsBean!=null){
                val characterBean = CharacterBean.characterMap[avatarsBean.name]
                if(characterBean!=null){
                    list += characterBean
                }else{
                    list += CharacterBean(
                        "",
                        avatarsBean.name,
                        0,
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        DailyMaterial("","",0,"",""),
                        WeeklyMaterial("","",0,"",""),
                        LocalMaterial("",0,"",""),
                        MonsterMaterial("",0,"",""),
                        BossMaterial("",0,""),
                        avatarsBean.url,
                        0
                    )
                }
            }
        }
        return list
    }

    private fun addData(index:Int){
        if(showDataSet.size<=dataSet.size){
            showDataSet.add(dataSet[index])
        }
    }
}