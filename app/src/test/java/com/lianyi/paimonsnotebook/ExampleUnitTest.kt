package com.lianyi.paimonsnotebook

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsText
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import kotlinx.coroutines.runBlocking
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {
        val url = ApiEndpoints.gameRecordSpiralAbyss("1", PlayerUid("106981262","cn_gf01"))

        val pms = url.split("?").last().split("&").sortedBy { it }.joinToString(separator = "&") { it }

        println("pms = ${pms}")

        runBlocking {
            val res = buildRequest {
                url(url)

                setDynamicSecret(DynamicSecret.SaltType.X4,DynamicSecret.Version.Gen2)
            }.getAsText()

            println("res = ${res}")
        }
    }

}