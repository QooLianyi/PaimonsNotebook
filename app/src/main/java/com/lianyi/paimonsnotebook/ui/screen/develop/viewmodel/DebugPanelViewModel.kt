package com.lianyi.paimonsnotebook.ui.screen.develop.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.AccountData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.geetest.GeeTestClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.BindingClient
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DebugPanelViewModel : ViewModel() {

    val gameRecordClient by lazy {
        GameRecordClient()
    }

    val geeTestClient by lazy {
        GeeTestClient()
    }

    val bindingClient by lazy {
        BindingClient()
    }

    var dailyNoteData by mutableStateOf<DailyNoteData?>(null)

    var output by mutableStateOf("")

    var dsSaltType by mutableStateOf(DynamicSecret.SaltType.X4)
    var dsVersion by mutableStateOf(DynamicSecret.Version.Gen1)

    var showLoading by mutableStateOf(false)

    var query by mutableStateOf("")
    var body by mutableStateOf("")

    var includeChars by mutableStateOf(false)

    var showSelectDsType by mutableStateOf(false)
    var showSelectDsVersion by mutableStateOf(false)

    var showDialog by mutableStateOf(false)

    var descDialogItemCount by mutableStateOf(10)

    var hoyolabDeviceId by mutableStateOf("")

    var tempText = ""
    var tempInt = 0

    var timeStampInput by mutableStateOf("")

    val selectedUser by mutableStateOf<User?>(null)

    init {

//        println("json = ${JSON.stringify(accountData)}")
//
//        println("ds = ${DS.getDS(DS.Version.Gen1,DS.SaltType.K2,true)}")

        viewModelScope.launch {

//            val action = buildRequest {
//                url(ApiEndpoints.AuthActionTicket("game_role",accountData.cookieMap[Cookie.Keys.Stoken]?:"",accountData.cookieMap[Cookie.Keys.Stuid]?:""))
//                setDynamicSecret(DS.SaltType.K2, includeChars = true)
//                setUser(accountData,Cookie.Type.Stoken or Cookie.Type.Mid)
//            }.getAsJson<ActionTicketData>()
//            if(action.success){
//                val res = buildRequest {
//                    url(ApiEndpoints.UserGameRolesByActionTicket(action.data.ticket))
//                    setUser(accountData,Cookie.Type.Ltoken)
//                }.getAsJson<UserGameRoleData>()
//            }

//            getDailyNote(accountData)
//            val result = bindingClient.generateAuthenticationKey(accountData)
//            println(result)
        }

    }

    fun genAuthKey(){
//        AccountManager.defaultAccount.apply {
//            if (this==null){
//                "当前没有默认用户".notify(PaimonsNotebookNotificationType.Error, closeable = true)
//            }else{
//                viewModelScope.launch {
//                    val res = bindingClient.generateAuthenticationKey(this@apply)
//                    if(res.success){
//                        output = res.data.asQueryParameter
//                    }else{
//                        "获取失败:${res.retcode}".notify(PaimonsNotebookNotificationType.Error, closeable = true)
//                    }
//                }
//            }
//        }
    }

    fun parseInputTimeStamp() {
        val time =
            TimeHelper.getRecoverTime(timeStampInput.toLongOrNull() ?: 0L)
    }

    fun articleNavigate() {
        val intent = Intent(PaimonsNotebookApplication.context, PostDetailScreen::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("articleId", output.toLongOrNull() ?: 0L)
        }
        PaimonsNotebookApplication.context.startActivity(intent)
    }



    fun geeTestV6(){
        if(AccountHelper.selectedUserFlow.value == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val result = geeTestClient.miyousheVerification(AccountHelper.selectedUserFlow.value!!.userEntity)
            println("result = ${result}")
        }
    }

    private suspend fun getDailyNote(accountData: AccountData, challenge: String = "") {
//        viewModelScope.launch {
//            val result = gameRecordClient.getDailyNote(accountData, challenge)
//            if (result.success) {
//                dailyNoteData = result.data
//            } else {
//                if (challenge != "error") {
//                    val challengeResult = geeTestClient.getXrpcChallenge(accountData)
//                    gameRecordClient.getDailyNote(accountData, challengeResult)
//                } else {
//                    "自动验证失败".show()
//                }
//            }
//            output = JSON.stringify(dailyNoteData)
//        }
    }

}