package com.lianyi.paimonsnotebook.card.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.CardUtil
import com.lianyi.paimonsnotebook.card.CardUtil.cShow
import com.lianyi.paimonsnotebook.card.service.ExpeditionListViewAdapterService
import com.lianyi.paimonsnotebook.card.service.GetDailyNoteService
import com.lianyi.paimonsnotebook.card.service.GetMonthLedgerService

class CardDailyNoteOverviewWidget : AppWidgetProvider() {
    lateinit var remoteViews: RemoteViews
    private lateinit var mContext:Context

    companion object{
        private var flag = true
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        mContext = context
        remoteViews = RemoteViews(context.packageName,R.layout.card_daily_note_overview_widget)
        remoteViews.apply {
            setOnClickPendingIntent(R.id.root,registerUpdateService(context))
        }
        appWidgetIds.forEach {
            appWidgetManager.updateAppWidget(it,remoteViews)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        mContext = context!!
        when(intent?.action){
            CardUtil.APPWIDGET_UPDATE,CardUtil.CLICK_ACTION->{
                startUpdateService(intent.action!!)
            }
            CardUtil.UPDATE_ACTION->{
                updateUI()
            }
            CardUtil.CLICK_UPDATE_ACTION -> {
                flag = !flag
                if(flag&&GetMonthLedgerService.requestOk){
                    "总览小组件更新".cShow()
                    updateUI()
                }
            }
        }
    }

    private fun updateUI(){
        if(!this::remoteViews.isInitialized){
            remoteViews = RemoteViews(mContext.packageName,R.layout.card_daily_note_overview_widget)
        }
        val dailyNoteBean = CardUtil.getCacheDailyNoteBean(CardUtil.mainUser.gameUid)
        val monthLedgerBean = CardUtil.getMonthLedgerBean(CardUtil.mainUser.gameUid)

        val adapter = Intent(mContext,ExpeditionListViewAdapterService::class.java).apply {
            data = Uri.parse("content"+(0..100000).random())
        }
        remoteViews.apply {
            setRemoteAdapter(R.id.grid_view,adapter)
//            setEmptyView(R.id.list,R.layout.item_world_expeditions_small)
            setTextViewText(R.id.current_resin,"${dailyNoteBean.current_resin}/${dailyNoteBean.max_resin}(${CardUtil.getRecoverTime(dailyNoteBean.resin_recovery_time.toLong())})")
            setTextViewText(R.id.daily_task_count,"${dailyNoteBean.finished_task_num}/${dailyNoteBean.total_task_num}")
            setTextViewText(R.id.current_home_coin,"${dailyNoteBean.current_home_coin}/${dailyNoteBean.max_home_coin}(${CardUtil.getRecoverTime(dailyNoteBean.home_coin_recovery_time.toLong())})")
            setTextViewText(R.id.weekly_challenge_count,"${dailyNoteBean.resin_discount_num_limit-dailyNoteBean.remain_resin_discount_num}/${dailyNoteBean.resin_discount_num_limit}")
            setTextViewText(R.id.quality_convert_recover_time,CardUtil.getQualityConvertTime(dailyNoteBean))
            if(monthLedgerBean!=null){
                setTextViewText(R.id.month_gemstone,"${monthLedgerBean.month_data.current_primogems}(${monthLedgerBean.month_data.primogems_rate}%)")
                setTextViewText(R.id.month_mora,"${monthLedgerBean.month_data.current_mora}(${monthLedgerBean.month_data.mora_rate}%)")
            }
        }
        AppWidgetManager.getInstance(mContext).updateAppWidget(ComponentName(mContext,CardDailyNoteOverviewWidget::class.java),remoteViews)
    }

    private fun startUpdateService(action:String){
        val dailyNoteIntent = Intent(mContext, GetDailyNoteService::class.java).apply {
            putExtra("type", CardUtil.TYPE_DAILY_NOTE_OVERVIEW)
            putExtra("action",action)
        }
        val monthLedgerIntent = Intent(mContext, GetMonthLedgerService::class.java).apply {
            putExtra("type", CardUtil.TYPE_DAILY_NOTE_OVERVIEW)
            putExtra("action",action)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(dailyNoteIntent)
            mContext.startForegroundService(monthLedgerIntent)
        } else {
            mContext.startService(dailyNoteIntent)
            mContext.startService(monthLedgerIntent)
        }
    }

    private fun registerUpdateService(context: Context):PendingIntent{
        val intent = Intent(CardUtil.CLICK_ACTION).apply {
            component = ComponentName(context,CardDailyNoteOverviewWidget::class.java)
        }
        return PendingIntent.getBroadcast(context,233,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}
