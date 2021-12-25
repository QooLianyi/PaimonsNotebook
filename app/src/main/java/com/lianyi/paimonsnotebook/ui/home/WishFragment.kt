package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.databinding.FragmentWishBinding
import com.lianyi.paimonsnotebook.databinding.PopEditUrlBinding
import com.lianyi.paimonsnotebook.lib.information.ActivityRequestCode
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.GachaType
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.ui.activity.LoadingGachaWishHistoryActivity
import com.lianyi.paimonsnotebook.util.*

class WishFragment : BaseFragment(R.layout.fragment_wish){

    lateinit var bind:FragmentWishBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWishBinding.bind(view)

        initPage()
        refreshPage()
        onNoData()
    }

    private fun refreshPage() {
        val pages = listOf(
            LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list,null),
            
        )
    }

    private fun initPage() {
        bind.wishMenu.setOnClickListener {

        }

        bind.wishTabLayout.setupWithViewPager(bind.wishViewPager)
    }

    private fun onNoData() {
        bind.noDataSpan.show()
        bind.editUrl.setOnClickListener {
            val layout = PopEditUrlBinding.bind(LayoutInflater.from(bind.root.context).inflate(R.layout.pop_edit_url,null))
            val win = showAlertDialog(bind.root.context,layout.root)

            layout.confirm.setOnClickListener {
                val input = layout.input.text.toString().trim()
                checkUrl(input){
                    activity?.runOnUiThread {
                        if(it){
                            LoadingGachaWishHistoryActivity.logUrl = input
                            startActivityForResult(Intent(bind.root.context,LoadingGachaWishHistoryActivity::class.java),
                                ActivityRequestCode.GACHA_HISTORY)
                        }else{
                            "输入的MiHoYoApi不正确,请检查后再次输入吧!".show()
                        }
                    }
                }
                win.dismiss()
            }

            layout.cancel.setOnClickListener {
                win.dismiss()
            }
        }
    }

    private fun checkUrl(url:String,block:(Boolean)->Unit){
        if(url.takeLast(5)!="#/log") {
            block(false)
            return
        }
        Ok.get(MiHoYoApi.getGachaLogUrl(url, GachaType.PERMANENT,1,6,"0")){
            println(it.toString())
            block(it.ok)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== ActivityResponseCode.OK&&requestCode==ActivityRequestCode.GACHA_HISTORY){
            refreshPage()
        }

    }

}