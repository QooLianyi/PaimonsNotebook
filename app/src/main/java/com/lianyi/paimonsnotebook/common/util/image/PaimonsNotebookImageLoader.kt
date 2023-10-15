package com.lianyi.paimonsnotebook.common.util.image

import coil.decode.GifDecoder
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

    //获得加载网络图片的request
    fun getImageRequest(url: String): ImageRequest {
        val imageFile = getCacheImageFileByUrl(url)

        return ImageRequest.Builder(context)
            .data(imageFile ?: url)
            .crossfade(true)
            .diskCacheKey(url)
//            .memoryCacheKey(url)
            .apply {
                if (url.endsWith(".gif")) {
                    this.decoderFactory(GifDecoder.Factory())
                }
            }
            .build()
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