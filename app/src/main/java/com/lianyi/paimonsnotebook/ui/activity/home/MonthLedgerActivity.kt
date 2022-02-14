package com.lianyi.paimonsnotebook.ui.activity.home

import android.os.Bundle
import android.widget.ArrayAdapter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.MonthLedgerBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.databinding.ActivityMonthLedgerBinding
import com.lianyi.paimonsnotebook.databinding.PopFailureBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class MonthLedgerActivity : BaseActivity() {
    lateinit var bind: ActivityMonthLedgerBinding

    private val showAccountList = mutableListOf<String>()
    private val accountList = mutableListOf<UserBean>()
    private val monthList = mutableListOf<Int>()
    private val pieEntry = mutableListOf<PieEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        bind = ActivityMonthLedgerBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView() {
        bind.accountSelectSpan.setOnClickListener {
            bind.accountSelect.performClick()
        }

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

        bind.accountSelect.select{ position, id ->
            val monthSelectPosition = bind.monthSelect.selectedItemPosition
            loadMonthLedger(accountList[position],if(monthSelectPosition==-1) 0 else monthList[monthSelectPosition])
        }

        bind.monthSelect.select { position, id ->
            loadMonthLedger(accountList[bind.accountSelect.selectedItemPosition],monthList[position])
        }

        with(bind.pie) {
            isRotationEnabled = false
            isSelected = false
            description.isEnabled = false
            setNoDataText("暂无数据,请稍后再来看看吧。")

            holeRadius = 80f

            extraRightOffset = 20.dp
            legend.textSize = 15f

            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP

            setTouchEnabled(false)
            setDrawEntryLabels(false)
        }

        setViewMarginBottomByNavigationBarHeight(bind.monthLedgerSpan)
        setContentMargin(bind.root)
    }

    private fun loadMonthLedger(user: UserBean,month:Int = 0) {
        //加载本月获得的原石
        Ok.get(MiHoYoApi.getMonthLedgerUrl(month, user.gameUid, user.region),user){
            runOnUiThread {
                if(it.ok){
                    val monthLedger = GSON.fromJson(it.optString("data"),MonthLedgerBean::class.java)
                    if(monthList.size==0){
                        val monthShowList = mutableListOf<String>()
                        monthLedger.optional_month.forEach {
                            monthList+=it
                            monthShowList += "${it}月"
                        }
                        bind.monthSelect.adapter = ArrayAdapter(bind.root.context,R.layout.item_text,monthShowList).apply {
                            setDropDownViewResource(R.layout.spinner_drop_down_style)
                        }
                        bind.monthSelect.setSelection(monthList.size-1)
                    }

                    bind.dayGemstone.text = monthLedger.day_data.current_primogems.toString()
                    bind.dayMora.text = monthLedger.day_data.current_mora.toString()
                    bind.monthGemstone.text =
                        monthLedger.month_data.current_primogems.toString()
                    bind.monthMora.text = monthLedger.month_data.current_mora.toString()

                    pieEntry.clear()
                    val actionIds = mutableListOf<Int>()
                    monthLedger.month_data.group_by.forEach {
                        actionIds += it.action_id
                        pieEntry += PieEntry(it.percent.toFloat(), it.action + it.percent + "%")
                    }

                    with(bind.pie) {
                        val pieDataSet = PieDataSet(pieEntry, "")
                        pieDataSet.colors = Constants.getMonthLegendColors(actionIds)
                        pieDataSet.setDrawValues(false)
                        val pieData = PieData(pieDataSet)
                        data = pieData
                        invalidate()
                    }
                }else{
                    val layout = PopFailureBinding.bind(layoutInflater.inflate(R.layout.pop_failure,null))
                    val win = showAlertDialog(bind.root.context,layout.root)

                    layout.title.text = "获取信息失败啦"
                    layout.message.text = "暂无数据,请稍后再试"

                    layout.close.setOnClickListener {
                        win.dismiss()
                    }
                }
            }
        }
    }
}