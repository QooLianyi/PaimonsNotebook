package com.lianyi.paimonsnotebook.common.util.hoyolab

import com.lianyi.paimonsnotebook.common.util.convert.Convert
import kotlin.math.ln
import kotlin.math.pow


/*
* 用于生成hoyolab请求的动态密钥
*
* */
object DynamicSecret {
    const val K2 = "oqrJbPCoFhWhFBNDvVRuldbrbiVxyWsP"
    const val LK2 = "zZDfHqEcwTqvvKDmqRcHyqqurxGgLfBV"
    const val X4 = "xV8v4Qu54lUKrEYFZkJhB8cuOh9Asafs"
    const val X6 = "t0qEgfub6cvueAPgR5m9aQWWVciEer7v"
    const val PROD = "JwYDpKvLj6MrMqqYU6jTKF17KNO2PXoS"

    enum class SaltType {
        K2,
        LK2,
        X4,
        X6,
        PROD
    }

    enum class Version {
        Gen1,
        Gen2
    }

    fun getDynamicSecret(
        version: Version,
        saltType: SaltType,
        includeChars: Boolean = true,
        query: String = "",
        body: String = "",
    ): String {

        val salt = when (saltType) {
            SaltType.K2 -> K2
            SaltType.LK2 -> LK2
            SaltType.X4 -> X4
            SaltType.X6 -> X6
            SaltType.PROD -> PROD
        }

        val t = System.currentTimeMillis() / 1000L

        val r = if (includeChars) getRs1() else getRs2()

        var dsContent = "salt=${salt}&t=${t}&r=${r}"

        val q = query.split("&").sortedBy { it }
            .joinToString(separator = "&") { it }

        if (version == Version.Gen2) {
            dsContent = "${dsContent}&b=${if (saltType == SaltType.PROD) "{}" else body}&q=${q}"
        }

        val check = Convert.toMd5HexString(dsContent).lowercase()
        return "${t},${r},${check}"
    }

    private fun getRs2(): String = (100000..200000).random().toString()

    private fun getRs1(): String {
        val range = "abcdefghijklmnopqrstuvwxyz1234567890"
        val sb = StringBuffer()
        repeat(6) {
            sb.append(range.random())
        }
        return sb.toString()
    }

    fun getSalt(intArray: IntArray) = with(StringBuilder()) {
        intArray.forEach { i2 ->
            val i = if (i2 < 0) {
                if ((-i2).toDouble() >= 3.0.pow(6.0)) (ln(-i2.toDouble()) / ln(3.0) - 6 + 48).toInt() else i2.inv()
            } else {
                i2 / 3 + 48
            }
            append(i.toChar())
        }
        toString()
    }
}
