package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.util.PaimonsNoteBookDatabaseHelper
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/*
* 网络图片
*
* url:图片链接
* contentScale:图片缩放模式
* diskCacheData:本地存储数据
* tint:
* placeholder:自定义的图片占位符
* */
@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    diskCache: DiskCache = DiskCache(url = url),
    tint: Color? = null,
    placeholder: @Composable BoxScope.() -> Unit = {},
) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            PaimonsNoteBookDatabaseHelper.updateImageUseInfo(diskCache)
        }
    }
    Box(modifier = modifier) {

        val model = PaimonsNotebookImageLoader.getImageRequest(url)

        AsyncImage(
            model = model,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null,
            imageLoader = PaimonsNotebookImageLoader.current,
            contentScale = contentScale,
            onError = {
            },
            onSuccess = {
            },
            colorFilter = if (tint != null) ColorFilter.tint(tint) else null
        )
    }
}

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    diskCache: DiskCache,
    contentScale: ContentScale = ContentScale.Fit,
    placeholder: @Composable BoxScope.() -> Unit = {},
) {
    NetworkImage(
        url = diskCache.url,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = placeholder,
        diskCache = diskCache
    )
}