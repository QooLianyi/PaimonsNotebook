package com.lianyi.paimonsnotebook.ui.activity.options

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.DownloadListener
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityOptionsBinding
import com.lianyi.paimonsnotebook.databinding.PopWarningBinding
import com.lianyi.paimonsnotebook.lib.data.RefreshData
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread

class OptionsActivity : BaseActivity() {
    lateinit var bind: ActivityOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setContentMargin(bind.root)

        initView()
    }

    private fun initView() {
        initCommonSetting()
        initDataSetting()
        initAboutPaimonsNotebook()
        setContentMargin(bind.root)
    }

    private fun initCommonSetting() {
        bind.sidebarButtonSetting.setOnClickListener {
            goA<SideBarButtonSettingActivity>()
        }

        bind.screenMarginSetting.setOnClickListener {
            val layout = PopWarningBinding.bind(layoutInflater.inflate(R.layout.pop_warning,null))
            val win = showAlertDialog(bind.root.context,layout.root)

            layout.title.text = "警告"
            layout.message.text = "修改内容边距可能会导致UI错位。(后续版本也许会修复)\n即使如此,你也要修改内容边距吗?"

            layout.cancel.setOnClickListener {
                win.dismiss()
            }
            layout.confirm.setOnClickListener {
                goA<ContentMarginActivity>()
                win.dismiss()
            }
        }
    }

    private fun initDataSetting() {
        //更新数据
        bind.getJson.setOnClickListener {
            showLoading(bind.root.context)
            RefreshData.getJsonData { ok,newDataCount->
                runOnUiThread {
                    if (ok&&newDataCount>0) {
                        "新增${newDataCount}条数据,下次启动时使用".showLong()
                    } else if(ok) {
                        "没有发现新数据".show()
                    }else{
                        "更新失败,请稍后再试试吧".show()
                    }
                    dismissLoadingWindow()
                }
            }
        }
    }

    private fun initAboutPaimonsNotebook() {
        bind.checkUpdate.setOnClickListener {
            thread {
                try {
                    val document = Jsoup.connect(Constants.JSON_DATA).get()
                    val appLastVersionSelect = "p.app_lastest_version"
                    val appLastVersion = document.select(appLastVersionSelect).text()

                    val uri = Uri.parse(Constants.getApkUr(appLastVersion))

                    val manager = PaiMonsNoteBook.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val requestApk = DownloadManager.Request(uri)
                    //指定下载网络
//                    requestApk.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    requestApk.setDestinationInExternalPublicDir(Environment.getDownloadCacheDirectory().absolutePath,"${appLastVersion}.apk")
                    requestApk.setTitle("派蒙的笔记本${appLastVersion}")
                    manager.enqueue(requestApk)

                    println(Constants.getApkUr(appLastVersion))
                }catch (e:Exception){
                    e.printStackTrace()
                    runOnUiThread {
                        "检查新版本失败,请稍后再试".show()
                    }
                }
            }
        }

        bind.goFeedBack.setOnClickListener {

        }
    }

}