package com.lianyi.paimonsnotebook.common.util.image

import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.GifDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import okio.ByteString.Companion.encodeUtf8
import java.io.File

/*
* 自定义image loader
* 设置指定的图片存储位置
* */
object PaimonsNotebookImageLoader {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val imageCache by lazy {
        context.filesDir.resolve("image_cache")
    }

    val current by lazy {
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                //设置图片占用当前可调用内存的百分比,默认25%
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                //设置图片缓存到本地磁盘的路径与设备空闲磁盘空间百分比,默认100%
                DiskCache.Builder()
                    .directory(imageCache)
                    .maxSizePercent(1.0)
                    .build()
            }
            .build()
    }

    //获得加载网络图片的request
    fun getImageRequest(url:String):ImageRequest{
        val imageFile = getCacheImageFileByUrl(url)

        return ImageRequest.Builder(context)
            .data(imageFile ?: url)
            .crossfade(true)
            .diskCacheKey(url)
            .memoryCacheKey(url)
            .apply {
                if (url.endsWith(".gif")) {
                    this.decoderFactory(GifDecoder.Factory())
                }
            }
            .build()
    }

    @OptIn(ExperimentalCoilApi::class)
    fun getCacheImage(url: String) = current.diskCache?.let {
        it.openSnapshot(url)?.data?.toFile()
    }

    fun getCacheImageFileByName(name: String) = File(imageCache, name)

    fun getCacheImageFileByUrl(url: String): File? {
        val file = File(imageCache, "${url.encodeUtf8().sha256().hex()}.1")
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    fun getCacheImageMetadataFileByUrl(url: String): File? {
        val file = File(imageCache, "${url.encodeUtf8().sha256().hex()}.0")
        return if (file.exists()) {
            file
        } else {
            null
        }
    }
}