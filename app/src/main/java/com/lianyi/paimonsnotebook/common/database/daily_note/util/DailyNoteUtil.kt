package com.lianyi.paimonsnotebook.common.database.daily_note.util

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.mutableStateListOf
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.web.hoyolab.geetest.GeeTestClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNote as DailyNoteEntity
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity

object DailyNoteUtil {

    private val dao = PaimonsNotebookDatabase.database.dailyNoteDao

    //实时便笺数据流
    private val DailyNoteListFlow = MutableStateFlow<List<DailyNote>>(listOf())

    //对外开放的实时便笺数据流
    val dailyNoteFlow = DailyNoteListFlow.asStateFlow()

    //在每日便笺中没有数据的用户的角色数据流
    private val NotInDailyNoteDBUserListFlow = MutableStateFlow<List<User>>(listOf())

    //对外开放的每日便笺中没有数据的用户的角色数据流
    val notInDailyNoteDBUserListFlow = NotInDailyNoteDBUserListFlow.asStateFlow()

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    private val geeTestClient by lazy {
        GeeTestClient()
    }

    init {
        //接收用户数据流
        CoroutineScope(Dispatchers.IO).launch {
            launch {

                AccountHelper.userListFlow.collect {
                    setDailyNoteList()
                }
            }
        }
    }

    //遍历用户角色列表并从数据库取对应用户与角色的数据
    private fun setDailyNoteList() {
        val userList = AccountHelper.userListFlow.value

        val list = mutableListOf<DailyNote>()
        val notInDailyNoteDBUserList = mutableListOf<User>()

        userList.forEach { user ->
            val notInDBRoles = mutableStateListOf<UserGameRoleData.Role>()

            user.userGameRoles.forEach { role ->
                //从数据库获取指定uid的数据
                val dailyNoteEntity = dao.getDailyNoteByGameRoleUid(role.game_uid)

                if (dailyNoteEntity != null) {
                    list += DailyNote(dailyNoteEntity, user.userEntity, role)
                } else {
                    //数据库没有相应数据就添加到未添加每日便笺的角色列表
                    notInDBRoles += role
                }
            }

            if (notInDBRoles.isNotEmpty()) {
                notInDailyNoteDBUserList += user
            }
        }

        list.sortByDescending { it.dailyNoteEntity.sort }

        DailyNoteListFlow.value = list
        NotInDailyNoteDBUserListFlow.value = notInDailyNoteDBUserList
    }

    //添加实时便笺
    suspend fun addDailyNote(user: User, role: UserGameRoleData.Role) {
        val dailyNoteData = getDailyNoteData(user.userEntity, role)

        if (dailyNoteData != null) {
            val dailyNoteEntity = DailyNoteEntity(
                uid = role.game_uid,
                userMid = user.userEntity.mid,
                dailyNote = dailyNoteData,
                sort = 0
            )

            //防止出现异常流程
            try {
                dao.insert(dailyNoteEntity)

                setDailyNoteList()

                "${role.nickname}已成功添加".notify()
            } catch (e: Exception) {
                when (e) {
                    is SQLiteConstraintException -> {
                        "外键约束失败:请检查添加的用户是否存在".errorNotify()
                    }
                    else -> {
                        "出现了意外的错误:${e.message}".errorNotify()
                    }
                }
            }
        } else {
            "添加失败,可能是实时便笺自动验证失败导致的,稍后重新获取或前往「米游社-我的角色-实时便笺」以解决".warnNotify()
        }
    }

    //删除实时便笺记录
    fun deleteDailyNote(dailyNote: DailyNote) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(dailyNote.dailyNoteEntity)
            setDailyNoteList()
        }
    }

    //更新实时便笺记录
    fun updateDailyNote(dailyNote: DailyNoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.update(dailyNote)
            setDailyNoteList()
        }
    }

    //刷新实时便笺
    suspend fun reloadDailyNote() {
        val notifyContent = StringBuilder()

        val list = mutableListOf<DailyNote>()

        DailyNoteListFlow.value.forEach { dailyNote ->
            val dailyNoteData = getDailyNoteData(dailyNote.userEntity, dailyNote.role)

            if (dailyNoteData != null) {
                val newDailyNoteEntity =
                    dailyNote.dailyNoteEntity.copy(dailyNote = dailyNoteData)

                val newDailyNote = dailyNote.copy(dailyNoteEntity = newDailyNoteEntity)
                list += newDailyNote

                updateDailyNote(newDailyNoteEntity)
            } else {
                val role = dailyNote.role
                notifyContent.append(
                    "\n${role.nickname} | ${role.region_name} | Lv.${role.level}"
                )
            }
        }

        if (notifyContent.isNotBlank()) {
            "实时便笺自动验证失败,稍后重新获取或前往「米游社-我的角色-实时便笺」以解决\n以下账号获取每日便笺失败:$notifyContent".warnNotify()
            notifyContent.clear()
        } else {
            DailyNoteListFlow.value = list
            "刷新完成".notify()
        }
    }

    //获取实时便笺数据
    private suspend fun getDailyNoteData(
        user: UserEntity,
        role: UserGameRoleData.Role,
    ): DailyNoteData? {
        val result = gameRecordClient.getDailyNote(user, role)

        return when (result.retcode) {
            ResultData.SUCCESS_CODE -> {
                result.data
            }
            //需要验证时,尝试自动过验证
            ResultData.NEED_VALIDATE -> {
                val xrpcChallenge = geeTestClient.getXrpcChallenge(user)

                return if (xrpcChallenge != "error") {
                    val dailyNoteDataXrpc = gameRecordClient.getDailyNote(user, role, xrpcChallenge)

                    if (dailyNoteDataXrpc.success) {
                        dailyNoteDataXrpc.data
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
            else -> {
                null
            }
        }
    }

}