package com.lianyi.paimonsnotebook.ui.screen.home.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.VideoData
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.view.VideoPlayScreen
import com.lianyi.paimonsnotebook.common.view.WebViewScreen
import com.lianyi.paimonsnotebook.common.web.WebHomeClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class PostDetailViewModel : ViewModel() {

    private val webHomeClient by lazy {
        WebHomeClient()
    }

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    //视频列表数据
    val videoList = mutableStateListOf<VideoData>()

    //视频封面 使用视频的封面，当无视频封面时，使用images内的第一张图片作为视频封面
    var videoCover by mutableStateOf("")

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
            postLoadingState = if (result.success) {

                postFullData = result.data

                val vodList = postFullData?.post?.vod_list
                if (vodList?.isEmpty() == false) {
                    vodList.first().resolutions.forEach {
                        videoList += VideoData(
                            width = it.width,
                            height = it.height,
                            definition = it.definition,
                            label = it.label,
                            url = it.url
                        )
                    }
                    videoCover = vodList.first().cover
                } else {
//                    videoCover = article?.post?.post?.images?.first()
//                        ?: "https://img-static.mihoyo.com/communityweb/upload/417976a3dacde790f947f8769d85d55c.png"

                    val content = postFullData?.post?.post?.content ?: ""

                    //判断是否含有哔哩哔哩视频内容
                    if (content.contains("https://player.bilibili.com/player.html")) {
                        val url =
                            Jsoup.parse(content).getElementsByTag("iframe").first()?.attr("src")
                                ?: ""
                        videoList += VideoData(url = url, isWeb = true)
                    }
                }

                LoadingState.Success
            } else {
                LoadingState.Error
            }
        }
    }

    fun hyperlinkNavigate(url: String) {
        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        PostHelper.checkUrlType(url = url, isPostID = {
            intent.apply {
                putExtra(PostHelper.PARAM_POST_ID, it)
                component = ComponentName(context,PostDetailScreen::class.java)
            }
        }, isUrl = {
            intent.apply {
                component = ComponentName(context,WebViewScreen::class.java)
                putExtra("url", it)
            }
        })

        context.startActivity(intent)
    }

    fun playVideo(context: Context) {
        if (videoList.size == 1 && videoList.first().isWeb) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(videoList.first().url)
            }
            context.startActivity(intent)
        } else {
            val intent = Intent(context, VideoPlayScreen::class.java).apply {
                putExtra("video_list", JSON.stringify(videoList))
            }
            context.startActivity(intent)
        }
    }

    fun getHtmlImageDiskCacheData(post: PostFullData.Post.Post) =
        DiskCache(
            url = "",
            lastUseFrom = "文章详情",
            createFrom = "文章详情"
        )

}