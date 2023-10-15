package com.lianyi.paimonsnotebook.common.util.builder

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.request.ImageRequest

inline fun Context.imageLoader(builder: ImageLoader.Builder.() -> Unit) =
    ImageLoader.Builder(this).apply(builder).build()

inline fun Context.imageRequest(url: String, builder: ImageRequest.Builder.() -> Unit = {}) =
    ImageRequest.Builder(this).apply {
        data(url)
        memoryCacheKey(url)
        diskCacheKey(url)
        if (url.endsWith(".gif")) {
            decoderFactory(GifDecoder.Factory())
        }
    }.apply(builder).build()

@Composable
fun requestOf(url: String): ImageRequest {
    return LocalContext.current.imageRequest(url)
}