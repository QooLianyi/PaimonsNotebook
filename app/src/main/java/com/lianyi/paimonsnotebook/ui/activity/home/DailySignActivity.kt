package com.lianyi.paimonsnotebook.ui.activity.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.dailynote.CurrentMonthDailySignInfoBean
import com.lianyi.paimonsnotebook.bean.dailynote.CurrentMonthDailySignRewardInfoBean
import com.lianyi.paimonsnotebook.databinding.ActivityDailySignBinding
import com.lianyi.paimonsnotebook.databinding.ItemSignBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.lib.information.ResponseCode
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import kotlin.concurrent.thread

class DailySignActivity : BaseActivity() {
    lateinit var bind: ActivityDailySignBinding
    var isSign = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityDailySignBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val showAccountList = mutableListOf<String>()
        val accountList = mutableListOf<UserBean>()
        showAccountList.add(mainUser!!.gameUid)
        accountList.add(mainUser!!)

        val jsonArray = JSONArray(usp.getString(JsonCacheName.USER_LIST,"[]"))
        repeat(jsonArray.length()){
            with(GSON.fromJson(jsonArray[it].toString(),UserBean::class.java)){
                showAccountList+=gameUid
                accountList+=this
            }
        }
        bind.accountSelect.adapter = ArrayAdapter(bind.root.context,R.layout.item_text,showAccountList).apply {
            setDropDownViewResource(R.layout.spinner_drop_down_style)
        }

        bind.accountSelect.select{ _: Int, _: Long ->
            refreshList(accountList[bind.accountSelect.selectedItemPosition])
        }

        bind.sign.setOnClickListener {
            showLoading(bind.root.context)
            "签到中...预计持续时间${accountList.size}秒".show()
            thread {
                var signed = 0
                var success = 0
                var failed = 0
                accountList.forEach {
                    Ok.dailySign(it){
                        when(it.optString("retcode")){
                            ResponseCode.OK->success++
                            ResponseCode.DAILY_SIGN_IS_SIGNED->signed++
                            else-> failed++
                        }
                    }
                    Thread.sleep(500L)
                }
                NotificationManager.sendNotification("签到结果","签到成功${success}个,失败${failed}个,已签到${signed}个 ")
                runOnUiThread {
                    "签到结果已经发送到通知栏".show()
                    dismissLoadingWindow()
                    if(success>0){
                        refreshList(accountList[bind.accountSelect.selectedItemPosition])
                    }
                }
            }
        }
        refreshList(mainUser!!)
        setViewMarginBottomByNavigationBarHeight(bind.dailySignSpan)
        setContentMargin(bind.root)
    }

    //刷新列表
    private fun refreshList(user:UserBean){
        Ok.get(MiHoYoApi.GET_CURRENT_MONTH_SIGN_REWARD_INFO){ currentMonthSignReward->
            if(currentMonthSignReward.ok){
                //签到信息获取
                Ok.get(MiHoYoApi.getCurrentMonthSignInfoUrl(user.gameUid,user.region),user){ signInfo->
                    if(signInfo.ok){
                        val currentMonthDailySignReward = GSON.fromJson(currentMonthSignReward.optString("data"),
                            CurrentMonthDailySignRewardInfoBean::class.java)
                        val currentMonthDailySignInfo = GSON.fromJson(signInfo.optString("data"),
                            CurrentMonthDailySignInfoBean::class.java)
                        isSign = currentMonthDailySignInfo.isIs_sign

                        runOnUiThread {
                            bind.month.text = currentMonthDailySignReward.month.toString()
                            bind.signDay.text = currentMonthDailySignInfo.total_sign_day.toString()

                            bind.list.adapter = ReAdapter(currentMonthDailySignReward.awards,R.layout.item_sign){
                                    view: View, awardsBean: CurrentMonthDailySignRewardInfoBean.AwardsBean, i: Int ->
                                val item = ItemSignBinding.bind(view)
                                loadImage(item.icon,awardsBean.icon)
                                item.rewardCount.text = "X${awardsBean.cnt}"

                                item.day.text = "${i+1}"

                                if(currentMonthDailySignInfo.total_sign_day>i){
                                    item.isGet.show()
                                }else if(!currentMonthDailySignInfo.isIs_sign&&currentMonthDailySignInfo.total_sign_day==i){
                                    item.background.setImageResource(R.drawable.icon_current_daily_sign_background)
                                    item.isGet.gone()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}