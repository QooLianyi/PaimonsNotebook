package com.lianyi.paimonsnotebook.common.web.static_resources

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object StaticResourcesApiEndpoint {
    private const val CdnUrl = "https://cdn.jsdelivr.net/gh"
    private const val RepoUrl = "/QooLianyi/PaimonsNotebookStaticResources"

    const val GithubStaticResourcesRepoUrl = "${CdnUrl}${RepoUrl}"

//    private var configuration: ConfigurationData = ConfigurationData()

    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            SettingsHelper.configurationFlow.collect {
//                configuration = it
//            }
//        }
    }

    fun staticRaw(category: String, fileName: String) = HutaoEndpoints.staticRaw(category, fileName)

//    fun staticRaw(category: String, fileName: String) =
//        "${configuration.currentStaticResourcesUrl}/${category}/${fileName}"
}