package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PlayerInformationBean
import com.lianyi.paimonsnotebook.databinding.FragmentSearchBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi.Companion.getPlayerData
import com.lianyi.paimonsnotebook.ui.activity.SearchResultActivity
import com.lianyi.paimonsnotebook.util.*

class SearchFragment : BaseFragment(R.layout.fragment_search){
    lateinit var bind: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind = FragmentSearchBinding.bind(view)

        bind.search.isSubmitButtonEnabled = true

        bind.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0.toString().length != 9) {
                    "账号格式错误".show()
                } else {
                    getPlayerData(p0!!.toString()) { b: Boolean, playerInformationBean: PlayerInformationBean?, intent:Intent? ->
                        activity?.runOnUiThread {
                            if (b){
                                goSearchResultActivity(playerInformationBean!!,intent!!)
                            }else{
                                showFailureAlertDialog(
                                    bind.root.context,
                                    "搜索失败",
                                    "可能是用户不在搜索范围内、今日搜索玩家过多、网络波动等原因导致的"
                                )
                            }
                        }
                    }
                    bind.search.isIconified = true
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    companion object{
        fun querySelfInformation(uid: String,block:()->Unit){
            getPlayerData(uid){ b: Boolean, playerInformationBean: PlayerInformationBean?, intent: Intent? ->
                block()
                if(b){
                    goSearchResultActivity(playerInformationBean!!,intent!!)
                }
            }
        }

        private fun goSearchResultActivity(playerInformationBean: PlayerInformationBean,intent: Intent){
            SearchResultActivity.playerInfo = playerInformationBean
            PaiMonsNoteBook.context.startActivity(intent)
        }
    }

}