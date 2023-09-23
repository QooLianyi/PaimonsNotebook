package com.lianyi.paimonsnotebook

import com.lianyi.paimonsnotebook.common.web.uigf.UIGFClient
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDateTime


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {
        runBlocking {
            println("开始请求")
            println("请求结束")
        }
    }

}