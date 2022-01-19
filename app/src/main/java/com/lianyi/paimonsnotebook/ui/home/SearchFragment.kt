package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PlayerInformationBean
import com.lianyi.paimonsnotebook.databinding.FragmentSearchBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.ui.activity.SearchResultActivity
import com.lianyi.paimonsnotebook.util.*

class SearchFragment : BaseFragment(R.layout.fragment_search){

    lateinit var bind: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind = FragmentSearchBinding.bind(view)

        bind.search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0.toString().length<9){
                    "格式错误".show()
                }else{
                    searchPlayer(p0!!.toString())
                    bind.search.isIconified = true
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

    }

    private fun searchPlayer(uid:String){
        Ok.get(MiHoYoApi.getPlayerInfoUrl(uid)){
            if(it.ok){
                val playerInfo = GSON.fromJson(it.optString("data"),
                    PlayerInformationBean::class.java)
                SearchResultActivity.playerInfo = playerInfo
                goA<SearchResultActivity>()
            }
        }
    }

}