package com.lianyi.paimonsnotebook.ui.screen.home.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.enums.ViewModelAction
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.TopicClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.painter_topic.PainterTopicListData
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.topic.TopicInfoData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import com.lianyi.paimonsnotebook.ui.screen.home.view.TopicScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TopicScreenViewModel : ViewModel() {

    var topicInfo by mutableStateOf<TopicInfoData?>(null)
        private set


    private val client by lazy {
        TopicClient()
    }

    private val _viewModelActionFlow = MutableStateFlow(ViewModelAction.None)
    val viewModelActionFlow = _viewModelActionFlow.asStateFlow()


    private val topicListMap = mutableStateMapOf<String, List<PainterTopicListData.TopicPost>>()

    var loadingState by mutableStateOf(LoadingState.Loading)
        private set

    lateinit var tabs: Array<String>
        private set

    var currentIndex by mutableIntStateOf(0)
        private set

    /*
    * 初始化
    * */
    fun update(intent: Intent) {
        viewModelScope.launchIO {
            val topicId = intent.getLongExtra(PostHelper.PARAM_TOPIC_ID, -1)

            //未找到传入的id时 直接退出
            if (topicId == -1L) {
                "未找到主题ID".errorNotify(false)
                _viewModelActionFlow.emit(ViewModelAction.Finish)
                return@launchIO
            }

            val res = client.getTopicInfo(topicId = topicId)
            if (!res.success) {
                "主题详情获取失败".errorNotify(false)
                _viewModelActionFlow.emit(ViewModelAction.Finish)
                return@launchIO
            }

            topicInfo = res.data

            if (topicInfo == null) {
                "主题详情获取失败".errorNotify(false)
                _viewModelActionFlow.emit(ViewModelAction.Finish)
                return@launchIO
            }

            tabs = topicInfo!!.topic.topic_sort_config.map { it.name }.toTypedArray()


            topicListMap.clear()
            onTabBarSelect(0)

            loadingState = LoadingState.Success
        }
    }

    private fun getTopicList(
        topicId: Long,
        listType: String = "UNKNOWN",
        offset: String = "",
        size: Int = 20,
        gameId: Int = 0
    ) {
        viewModelScope.launchIO {
            val res = client.getPainterTopicList(topicId, listType, offset, size, gameId)

            if (!res.success) {
                "获取文章列表时出现错误".warnNotify(false)
                return@launchIO
            }

            //将新获取的文章加入列表中
            val currentList = topicListMap[listType] ?: listOf()
            topicListMap[listType] = currentList + res.data.list
        }
    }

    //当选中顶部tabbar时
    fun onTabBarSelect(index: Int) {
        currentIndex = index

        //此处需要提供大写的类型才能正确的获取数据
        val listType = getListType()
        val id = topicInfo!!.topic.id.toLongOrNull() ?: -1L

        getTopicList(topicId = id, listType = listType)
    }

    //获取列表类型
    private fun getListType() = if (currentIndex == 0) {
        "UNKNOWN"
    } else {
        topicInfo?.topic?.topic_sort_config?.get(currentIndex)?.data_report_name?.uppercase()
            ?: "UNKNOWN"
    }

    //获取当前列表内容
    fun getCurrentList(): List<PainterTopicListData.TopicPost> {
        val key = getListType()

        return topicListMap[key] ?: listOf()
    }

    //当点击到tag时
    fun onClickTag(topic: PostFullData.Post.Topic) {
        val currentTopicId = topicInfo!!.topic.id.toIntOrNull() ?: 0

        //如果当前的id与点击的id不同时,跳转到点击的主题界面
        if (currentTopicId != topic.id) {
            HomeHelper.goActivityByIntentNewTask {
                setComponentName(TopicScreen::class.java)
                putExtra(PostHelper.PARAM_TOPIC_ID, topic.id.toLong())
            }
        }
    }

    //当点击帖子列表项时
    fun onClickPostListItem(item: PainterTopicListData.TopicPost) {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(PostDetailScreen::class.java)
            putExtra(PostHelper.PARAM_POST_ID, item.post.post.post_id.toLongOrNull() ?: 0L)
        }
    }
}