package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.dailynote.CurrentMonthDailySignInfoBean
import com.lianyi.paimonsnotebook.bean.dailynote.CurrentMonthDailySignRewardInfoBean
import com.lianyi.paimonsnotebook.databinding.ActivityDailySignBinding
import com.lianyi.paimonsnotebook.databinding.ItemSignBinding
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*

class DailySignActivity : BaseActivity() {
    lateinit var bind:ActivityDailySignBinding
    var isSign = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityDailySignBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.sign.setOnClickListener {
            if(isSign){
                "小伙伴今天已经签过到啦~".show()
            }else{
                Ok.dailySign(mainUser!!){
                    runOnUiThread {
                        if(it.ok){
                            "签到成功".show()
                        }else{
                            "签到失败:${it.optString("retcode")}".show()
                        }
                        refreshList()
                    }
                }
            }
        }
        refreshList()
    }

    private fun refreshList(){
        Ok.get(MiHoYoApi.GET_CURRENT_MONTH_SIGN_REWARD_INFO){ currentMonthSignReward->
            if(currentMonthSignReward.ok){
                //签到信息获取
                Ok.get(MiHoYoApi.getCurrentMonthSignInfoUrl(mainUser!!.gameUid, mainUser!!.region)){ signInfo->
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

                                if(currentMonthDailySignInfo.total_sign_day>i){
                                    item.isGet.show()
                                }else if(!currentMonthDailySignInfo.isIs_sign&&currentMonthDailySignInfo.total_sign_day==i){
                                    item.background.setImageResource(R.drawable.icon_current_daily_sign_background)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}