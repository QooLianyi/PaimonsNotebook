package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
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
                if(p0.toString().length!=9){
                    "账号格式错误".show()
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

    companion object{
        private var switch = true
        fun searchPlayer(uid:String,server:String = "cn_gf01"){
            val query = "role_id=${uid}&server=${server}"
            Ok.get(MiHoYoApi.getPlayerInfoUrl(uid,server),query){
                if(it.ok){
                    val playerInfo = GSON.fromJson(it.optString("data"),
                        PlayerInformationBean::class.java)
                    SearchResultActivity.playerInfo = playerInfo
                    val intent = Intent(PaiMonsNoteBook.context,SearchResultActivity::class.java)
                    intent.putExtra("roleId",uid)
                    intent.putExtra("server",server)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    PaiMonsNoteBook.context.startActivity(intent)
                    switch = true
                }else{
                    switch = if(switch){
                        searchPlayer(uid, "cn_qd01")
                        false
                    }else{
                        true
                    }
                }
            }
        }

        fun querySelfInformation(uid: String,block:()->Unit){
            searchPlayer(uid)
            block()
        }
    }
}