package com.lianyi.paimonsnotebook.common.components.media

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.util.PaimonsNoteBookDatabaseHelper
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
* 使用ImageView实现的网络图片加载
* */
@Composable
fun NetworkImageView(
    url:String,
    modifier: Modifier = Modifier,
    diskCache: DiskCache = DiskCache(url)
) {

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            PaimonsNoteBookDatabaseHelper.updateImageUseInfo(diskCache)
        }
    }

    AndroidView(factory = {context->
        ImageView(context).apply {
            val imageFile = PaimonsNotebookImageLoader.getCacheImageFileByUrl(url)

            load(imageFile?:url){
                crossfade(true)
                diskCacheKey(url)
                memoryCacheKey(url)
            }
        }
    }, modifier = modifier)

}