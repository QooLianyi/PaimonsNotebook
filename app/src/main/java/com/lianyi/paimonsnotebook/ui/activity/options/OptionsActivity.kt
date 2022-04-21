package com.lianyi.paimonsnotebook.ui.activity.options

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PaimonsNotebookLatestBean
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityOptionsBinding
import com.lianyi.paimonsnotebook.databinding.PopWarningBinding
import com.lianyi.paimonsnotebook.lib.data.UpdateInformation
import com.lianyi.paimonsnotebook.lib.information.Constants
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
        setViewMarginBottomByNavigationBarHeight(bind.root)
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

        bind.wishSetting.setOnClickListener {
            goA<WishOptionsActivity>()
        }

        bind.homeSetting.setOnClickListener {
            goA<HomeOptionsActivity>()
        }

    }

    private fun initDataSetting() {
        //更新数据
        bind.getJson.setOnClickListener {
            showLoading(bind.root.context)
            UpdateInformation.updateJsonData { ok, newDataCount->
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

    //关于
    private fun initAboutPaimonsNotebook() {
        bind.checkUpdate.setOnClickListener {
            showLoading(this)
            thread {
                try {
                    val document = Jsoup.connect(Constants.PAIMONS_NOTEBOOK_WEB).get()
                    val appLastVersionSelect = "p.app_lastest_version"
                    val appLastVersionCodeSelect = "p.app_last_version_code"

                    val appLastVersion = document.select(appLastVersionSelect).text()
                    val appLastVersionCode = if(document.select(appLastVersionCodeSelect).text().isNullOrEmpty()) 0L else document.select(appLastVersionCodeSelect).text().toLong()

                    if(PaiMonsNoteBook.APP_VERSION_CODE<appLastVersionCode){
                        runOnUiThread {
                            "发现新版本(${appLastVersion})\n正在通过系统浏览器下载...".showLong()
                            UpdateInformation.getNewVersionApp()
                        }

//                            val uri = Uri.parse(Constants.getApkUr(appLastVersion))
//                            val manager = PaiMonsNoteBook.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                            val requestApk = DownloadManager.Request(uri)
//                            //指定下载网络
////                    requestApk.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                                requestApk.setDestinationInExternalPublicDir(MediaStore.Downloads.EXTERNAL_CONTENT_URI.path?:"","${appLastVersion}.apk")
//                            }else{
//                                requestApk.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,"${appLastVersion}.apk")
//                            }
//                            requestApk.setTitle("派蒙的笔记本${appLastVersion}")
//                            manager.enqueue(requestApk)
                    }else{
                        runOnUiThread {
                            "当前已是最新版本".showLong()
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    runOnUiThread {
                        "检查新版本失败,请稍后再试".show()
                    }
                }finally {
                    runOnUiThread {
                        dismissLoadingWindow()
                    }
                }
            }
        }

        //设置版本信息
        bind.appVersion.text = PaiMonsNoteBook.APP_VERSION_NAME

        bind.showUpdateNote.select {
            if(it){
                if(System.currentTimeMillis()-sp.getLong(Constants.GET_UPDATE_NOTE_TIME,0L)>=600000L){
                    Ok.get(Constants.PAIMONS_NOTE_BOOK_LATEST){
                        val latestBean = GSON.fromJson(it.toString(), PaimonsNotebookLatestBean::class.java)
                        sp.edit {
                            putLong(Constants.GET_UPDATE_NOTE_TIME,System.currentTimeMillis())
                            putString(Constants.UPDATE_NOTE_CACHE,latestBean.body)
                            apply()
                        }
                        runOnUiThread {
                            bind.markDown.setText(latestBean.body)
                            bind.updateNoteSpan.measure(0,0)
                            openAndCloseAnimationVer(bind.updateNoteSpan,60.dp.toInt(),(bind.updateNoteSpan.measuredHeight*1.215).toInt(),500)
                        }
                    }
                }else{
                    bind.markDown.setText(sp.getString(Constants.UPDATE_NOTE_CACHE,""))
                }
                bind.updateNoteSpan.measure(0,0)
                openAndCloseAnimationVer(bind.updateNoteSpan,60.dp.toInt(),(bind.updateNoteSpan.measuredHeight*1.215).toInt(),500)

                bind.updateNoteDropDown.rotation = 0f
            }else{
                openAndCloseAnimationVer(bind.updateNoteSpan,bind.updateNoteSpan.measuredHeight,60.dp.toInt(),500)
                bind.updateNoteDropDown.rotation = 180f
            }
            bind.updateNoteDropDown.animate().rotationBy(180f).duration = 500
        }

        bind.goFeedBack.setOnClickListener {
            "正在尝试唤起手机QQ".show()
            val key = "qhNCaJ5EPHebQIX4-G2mpQu86f-WlAc7"
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key");
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent);
            } catch (e:Exception) {
                // 未安装手Q或安装的版本不支持
                showFailureAlertDialog(bind.root.context,"唤起手机QQ失败","可能是未安装手Q或安装的版本不支持")
            }
        }

        bind.updatePlan.setOnClickListener {
            val uri = Uri.parse(Constants.PAIMONSNOTEBOOK_UPDATE_PLAN_URL)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            startActivity(intent)
        }

        //前往github
        bind.goGithub.setOnClickListener {
            val uri = Uri.parse(Constants.PAIMONSNOTEBOOK_GITHUB_URL)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            startActivity(intent)
        }
    }
}