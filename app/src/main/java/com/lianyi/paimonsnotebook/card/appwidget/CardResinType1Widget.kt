package com.lianyi.paimonsnotebook.card.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.dailynote.DailyNoteBean
import com.lianyi.paimonsnotebook.card.CardUtil
import com.lianyi.paimonsnotebook.card.CardUtil.cShow
import com.lianyi.paimonsnotebook.card.service.GetDailyNoteService

class CardResinType1Widget : AppWidgetProvider() {

    private lateinit var remoteViews: RemoteViews

    private lateinit var mContext:Context

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        mContext = context
        remoteViews = RemoteViews(context.packageName,R.layout.card_resin_type1_widget)

        //设置手动更新
        remoteViews.setOnClickPendingIntent(R.id.root,registerUpdateService(context))

        appWidgetIds.forEach {
            appWidgetManager.updateAppWidget(it, remoteViews)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        mContext = context!!
        CardUtil.context = context
        when(intent?.action){
            //系统调用更新
            CardUtil.APPWIDGET_UPDATE,CardUtil.CLICK_ACTION->{
                startGetDailyNoteService(intent.action!!)
            }
            CardUtil.UPDATE_ACTION-> {
                updateUI()
            }
            CardUtil.CLICK_UPDATE_ACTION->{
                "树脂小组件更新".cShow()
                updateUI()
            }
        }
    }

    private fun updateUI(){
        val dailyNoteBean = CardUtil.getCacheDailyNoteBean(CardUtil.mainUser.gameUid)
        if(!this::remoteViews.isInitialized){
            remoteViews = RemoteViews(mContext.packageName,R.layout.card_resin_type1_widget)
        }
        remoteViews.apply {
           if(dailyNoteBean!=null){
               setTextViewText(R.id.current_resin,dailyNoteBean.current_resin.toString())
               setTextViewText(R.id.max_resin,dailyNoteBean.max_resin.toString())
               setTextViewText(R.id.time,CardUtil.formatTime(dailyNoteBean.resin_recovery_time.toLong()))
           }
        }
        val componentName = ComponentName(mContext,CardResinType1Widget::class.java)
        AppWidgetManager.getInstance(mContext).updateAppWidget(componentName,remoteViews)
    }

    private fun startGetDailyNoteService(action:String){
        val intent = Intent(mContext, GetDailyNoteService::class.java).apply {
            putExtra("type",CardUtil.TYPE_RESIN_TYPE1)
            putExtra("action",action)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent)
        } else {
            mContext.startService(intent)
        }
    }

    private fun registerUpdateService(context: Context):PendingIntent{
        val intent = Intent(CardUtil.CLICK_ACTION).apply {
            component = ComponentName(context,CardResinType1Widget::class.java)
        }
        return PendingIntent.getBroadcast(context,233,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}
