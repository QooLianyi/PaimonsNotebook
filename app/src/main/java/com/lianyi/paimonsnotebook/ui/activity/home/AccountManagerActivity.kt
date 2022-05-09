package com.lianyi.paimonsnotebook.ui.activity.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.account.AccountBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityAccountManagerBinding
import com.lianyi.paimonsnotebook.databinding.ItemAccountBinding
import com.lianyi.paimonsnotebook.databinding.PopInformationBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.launch.SetCookieActivity
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class AccountManagerActivity : BaseActivity() {
    lateinit var bind: ActivityAccountManagerBinding

    companion object{
        val userList =  mutableListOf<UserBean>()
    }

    private var originalMainUserId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAccountManagerBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView() {
        originalMainUserId = mainUser!!.gameUid+""
        refreshUserListData()
        if(userList.size>0){
            setAccountListAdapter()
        }

        bind.addUser.setOnClickListener {
            if(userList.size>=AppConfig.MAX_ACCOUNT_NUM){
                "不能再添加更多账号".show()
                return@setOnClickListener
            }

            SetCookieActivity.isAddUser = true
            startActivityForResult(Intent(this, SetCookieActivity::class.java),ActivityRequestCode.LOGIN)
        }

        bind.defaultAccountInformation.setOnClickListener {
            val layout = PopInformationBinding.bind(layoutInflater.inflate(R.layout.pop_information,null))
            val win = showAlertDialog(this,layout.root)

            layout.content.text = getString(R.string.information_default_account)

            layout.close.setOnClickListener {
                win.dismiss()
            }
        }

        bind.account.exportCookie.setOnClickListener {
            exportCookie(mainUser!!)
        }

        setViewMarginBottomByNavigationBarHeight(bind.accountManagerSpan)
        refreshMainUserInformation()
        setContentMargin(bind.root)
    }

    //设置账号列表适配器
    private fun setAccountListAdapter(){
        bind.list.adapter = ReAdapter(userList,R.layout.item_account){
                view, userBean, position ->
            val item = ItemAccountBinding.bind(view)

            //读取信息
            Ok.get(MiHoYoApi.getAccountInformation(userBean.loginUid)){
                if(it.ok){
                    val accountInfo = GSON.fromJson(it.optString("data"),AccountBean::class.java)
                    runOnUiThread {
                        loadImage(item.icon,accountInfo.user_info.avatar_url)
                        loadImage(item.pendant,accountInfo.user_info.pendant)
                        item.nick.text = userBean.nickName
                        item.gameUid.text = userBean.gameUid
                    }
                }
            }

            //账号删除
            item.deleteAccount.setOnClickListener {
                showConfirmAlertDialog(bind.root.context,"提示","你确定要删除账号\"${userBean.nickName}\"吗?"){
                    if(it){
                        deleteUser(userBean.gameUid)
                        item.scrollSpan.scrollTo(0,0)
                        "删除账号${userBean.nickName}".show()
                    }
                }
            }

            //更改默认账号
            item.setDefault.setOnClickListener {
                showConfirmAlertDialog(bind.root.context,"提示","你确定将账号\"${userBean.nickName}\"设为默认账号吗?"){
                    if(it){
                        SetCookieActivity.checkCookie(userBean.loginUid,userBean.lToken,userBean.cookieToken){ b: Boolean, getGameRolesByCookieBean: GetGameRolesByCookieBean? ->
                            if(b){
                                runOnUiThread {
                                    with(mainUser!!){
                                        userList.add(0,UserBean(nickName, loginUid, region, regionName, gameUid, lToken, cookieToken, gameLevel))
                                        nickName = userBean.nickName
                                        loginUid = userBean.loginUid
                                        region = userBean.region
                                        regionName = userBean.regionName
                                        gameUid = userBean.gameUid
                                        lToken = userBean.lToken
                                        cookieToken = userBean.cookieToken
                                        gameLevel = userBean.gameLevel

                                        deleteUser(userBean.gameUid)
                                        usp.edit().apply {
                                            putString(JsonCacheName.USER_LIST, GSON.toJson(userList))
                                            putString(JsonCacheName.MAIN_USER_NAME, GSON.toJson(mainUser))
                                            apply()
                                        }
                                    }
                                    item.scrollSpan.scrollTo(0,0)
                                    "已将默认账号更改为${userBean.gameUid}".show()
                                    refreshUserListData()
                                    refreshMainUserInformation()
                                }
                            }else{
                                runOnUiThread {
                                    "更改失败:所选账号Cookie过期".show()
                                }
                            }
                        }
                    }
                }
            }

            item.exportCookie.setOnClickListener {
                exportCookie(userBean)
            }
        }
    }

    private fun exportCookie(userBean: UserBean){
        showConfirmAlertDialog(bind.root.context,"导出Cookie","Cookie非常重要!泄露了别人可以通过Cookie来登录你的米游社,切勿外传!(修改密码后cookie会刷新)"){
           if(it){
               val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
               val clipData = ClipData.newPlainText("cookie",MiHoYoApi.getCookie(userBean))
               cm.setPrimaryClip(clipData)
               "cookie已复制到剪切板\n如果没有复制成功请检查相关权限".showLong()
           }
        }
    }

    //刷新账号列表的数据
    private fun refreshUserListData(){
        userList.clear()
        val userListArray = JSONArray(usp.getString(JsonCacheName.USER_LIST,"[]"))
        userListArray.toList(userList)
        if (bind.list.adapter == null) {
            setAccountListAdapter()
        } else {
            bind.list.adapter?.notifyDataSetChanged()
        }
    }

    //刷新默认账号信息
    private fun refreshMainUserInformation(){
        Ok.get(MiHoYoApi.getAccountInformation(mainUser!!.loginUid)){
            if(it.ok){
                val accountInfo = GSON.fromJson(it.optString("data"),AccountBean::class.java)
                runOnUiThread {
                    loadImage(bind.account.icon,accountInfo.user_info.avatar_url)
                    loadImage(bind.account.pendant,accountInfo.user_info.pendant)
                    bind.account.nick.text = mainUser!!.nickName
                    bind.account.gameUid.text = mainUser!!.gameUid
                }
            }
        }
        bind.account.deleteAccount.gone()
        bind.account.setDefault.gone()
    }

    //删除用户
    private fun deleteUser(gameUID:String){
        var deleteAccount:UserBean?=null
        userList.forEach {
            if(it.gameUid == gameUID){
                deleteAccount = it
                return@forEach
            }
        }
        userList.remove(deleteAccount)

        bind.list.adapter?.notifyDataSetChanged()
        usp.edit().apply{
            putString(JsonCacheName.USER_LIST,GSON.toJson(userList))
            apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //判断是否添加成功
        if(requestCode==ActivityRequestCode.LOGIN&&resultCode==ActivityResponseCode.OK){
            refreshUserListData()
            "添加成功".show()
        }else{
            "取消添加".show()
        }
    }

    override fun onBackPressed() {
        if(originalMainUserId!= mainUser!!.gameUid){
            setResult(ActivityResponseCode.DATA_CHANGE)
            sp.edit {
                putBoolean("main_user_change",true)
                apply()
            }
        }
        finish()
    }

}