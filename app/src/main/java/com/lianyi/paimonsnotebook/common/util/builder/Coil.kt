package com.lianyi.paimonsnotebook.common.util.builder

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import okhttp3.Headers

inline fun Context.imageLoader(builder: ImageLoader.Builder.() -> Unit) =
    ImageLoader.Builder(this).apply(builder).build()

inline fun Context.imageRequest(
    url: String,
    builder: ImageRequest.Builder.() -> Unit = {}
) =
    ImageRequest.Builder(this).apply {
        data(url)
        memoryCacheKey(url)
        diskCacheKey(url)
        if (url.endsWith(".gif")) {
            decoderFactory(GifDecoder.Factory())
        }
    }.apply(builder).build()

fun requestOf(url: String): ImageRequest {
    return PaimonsNotebookApplication.context.imageRequest(url)
}

fun requestOf(url: String, headers: Headers?): ImageRequest {
    /*
    * 此处由原来的LocalContext.current,改为使用全局的context
    * 使用LocalContext.current会使用当前activity的context在图片未加载时activity销毁,可能会造成短期内多次调用GC,导致卡顿,甚至造成内存泄漏
    * 原因通过leakCanary日志推测是:coil启动的imageRequest在activity销毁后依然持有销毁的activity的引用,导致频繁调用GC,GC后可能会销毁这个imageRequest对象,但有相当的概率不会销毁
    * 使用全局的context会在图片显示后加载完成,测试时不再会在短期内频繁调用GC
    * */
    return PaimonsNotebookApplication.context.imageRequest(url) {
        if (headers != null){
            headers(headers)
        }
    }
}