package com.lianyi.paimonsnotebook.ui.screen.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.common.view.VideoPlayScreen
import com.lianyi.paimonsnotebook.common.web.WebHomeClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import com.lianyi.paimonsnotebook.ui.screen.home.view.TopicScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {

    private val webHomeClient by lazy {
        WebHomeClient()
    }

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    var postLoadingState by mutableStateOf(LoadingState.Loading)

    var postFullData by mutableStateOf<PostFullData?>(null)

    var fullScreenImgUrl = ""
    var showFullScreenImage by mutableStateOf(false)


    fun showFullScreenImage(url: String) {
        fullScreenImgUrl = url
        showFullScreenImage = true
    }

    fun dismissFullScreenImage() {
        showFullScreenImage = false
    }

    fun loadArticleContent(articleId: Long) {
        postLoadingState = LoadingState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val result = webHomeClient.getPostFull(articleId)

            println(result)

            postLoadingState = if (result.success) {
                postFullData = result.data
                LoadingState.Success
            } else {
                LoadingState.Error
            }
        }
    }

    fun hyperlinkNavigate(url: String) {
        HomeHelper.goActivityByIntentNewTask {
            PostHelper.checkUrlType(url = url, isPostID = {
                putExtra(PostHelper.PARAM_POST_ID, it)
                setComponentName(PostDetailScreen::class.java)
            }, isUrl = {
                setComponentName(HoyolabWebActivity::class.java)
                putExtra(HoyolabWebActivity.EXTRA_URL, it)
            }, isTopic = {
                setComponentName(TopicScreen::class.java)
                putExtra(PostHelper.PARAM_TOPIC_ID,it)
            })
        }
    }

    fun getHtmlImageDiskCacheData(post: PostFullData.Post.Post) =
        DiskCache(
            url = "",
            lastUseFrom = "文章详情",
            createFrom = "文章详情"
        )

    fun onClickVideo(vod: PostFullData.Post.Vod) {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(VideoPlayScreen::class.java)
            putExtra("video_list", JSON.stringify(vod))
        }
    }

    fun onClickTag(topic: PostFullData.Post.Topic) {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(TopicScreen::class.java)
            putExtra(PostHelper.PARAM_TOPIC_ID, topic.id.toLong())
        }
    }
}