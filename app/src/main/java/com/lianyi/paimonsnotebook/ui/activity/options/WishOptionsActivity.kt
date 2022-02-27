package com.lianyi.paimonsnotebook.ui.activity.options

import android.net.wifi.aware.WifiAwareManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityWishOptionsBinding
import com.lianyi.paimonsnotebook.databinding.PopConfirmBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.MainActivity
import com.lianyi.paimonsnotebook.ui.home.WishFragment
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import kotlin.concurrent.thread

class WishOptionsActivity : BaseActivity() {

    lateinit var bind:ActivityWishOptionsBinding
    private lateinit var account:String
    private val gachaHistoryUidList = mutableListOf<String>()
    private val isOpen = sp.getBoolean(AppConfig.SP_WISH_SORT_BY_DESCENDING,false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityWishOptionsBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initConfig()
    }

    private fun initConfig() {
        bind.sort.isChecked = isOpen
    }

    private fun initView() {
        refreshData()

        bind.deleteWishHistory.setOnClickListener {
            if(gachaHistoryUidList.isNotEmpty()){
                showConfirm(5,"警告","删除祈愿记录会导致记录永久丢失\n即使如此,你也要删除记录吗?"){
                    showListAlertDialog(this,gachaHistoryUidList,"选择要删除的账号"){
                            s, i ->
                        account = s
                        checkAccount()
                    }
                }
            }else{
                "没有找到祈愿记录".show()
            }
        }
    }

    private fun refreshData(){
        gachaHistoryUidList.clear()
        JSONArray(sp.getString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST, "[]")).toList(gachaHistoryUidList)
    }

    private fun checkAccount(){
        showInputAlertDialog(this,"确认","正确输入选择的账号以继续"){
            if(it==account){
                showConfirm(10,"最后警告","将要删除 $account 账号的祈愿记录,该操作会导致记录永久丢失(无法找回)\n即使如此,你也要删除记录吗?"){
                    PaiMonsNotebookDataBase.INSTANCE.deleteGachaHistory(account)
                    refreshData()
                    "删除成功".show()
                }
            }else{
                "选择的账号与输入的账号不一致".showLong()
            }
        }
    }

    private fun showConfirm(startTime:Int,title:String,content:String,block:()->Unit){
        val layout = PopConfirmBinding.bind(layoutInflater.inflate(R.layout.pop_confirm,null))
        val win = showAlertDialog(this,layout.root)

        var cancel = false
        layout.cancel.setOnClickListener {
            cancel = true
            win.dismiss()
        }
        layout.title.text = title
        layout.content.text = content

        thread {
            (startTime downTo 0).forEach {
                if(cancel){
                    return@thread
                }
                runOnUiThread {
                    layout.confirm.text = if(it!=0){
                        "确认($it)"
                    }else{
                        layout.confirm.setOnClickListener {
                            block()
                            win.dismiss()
                        }
                        "确认"
                    }
                }
                Thread.sleep(1000L)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(isOpen!=bind.sort.isChecked){
            sp.edit().apply{
                putBoolean(AppConfig.SP_WISH_SORT_BY_DESCENDING,bind.sort.isChecked)
                apply()
            }
            "祈愿记录已应用新的排序规则".show()
        }
    }
}