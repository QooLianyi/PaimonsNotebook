package com.lianyi.paimonsnotebook.common.web

import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment

/*
*
* */
object HutaoEndpoints {
    private const val ApiSnapGenshin = "https://api.snapgenshin.com"
    private const val ApiSnapGenshinMetadata = "${ApiSnapGenshin}/metadata"
    const val ApiSnapGenshinStaticRaw = "${ApiSnapGenshin}/static/raw"
    private const val ApiSnapGenshinStaticZip = "${ApiSnapGenshin}/static/zip"

    private const val Host = "api.snapgenshin.com"

    //请求元数据时的header
    val Headers by lazy {
        okhttp3.Headers.Builder()
            .add("User-Agent", CoreEnvironment.PaimonsNotebookUA)
            .build()
    }

    /// <summary>
    /// 胡桃元数据2文件
    /// </summary>
    /// <param name="locale">语言</param>
    /// <param name="fileName">文件名称</param>
    /// <returns>路径</returns>
    fun metadata(locale: String, fileName: String) =
        "${ApiSnapGenshinMetadata}/Genshin/${locale}/${fileName}"

    /// <summary>
    /// 图片资源
    /// </summary>
    /// <param name="category">分类</param>
    /// <param name="fileName">文件名称 包括后缀</param>
    /// <returns>路径</returns>
    fun staticRaw(category: String, fileName: String) =
        "${ApiSnapGenshinStaticRaw}/${category}/${fileName}"

    fun staticZip(fileName: String) = "${ApiSnapGenshinStaticZip}/${fileName}.zip"
}