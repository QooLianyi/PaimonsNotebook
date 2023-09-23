package com.lianyi.paimonsnotebook.common.util.convert

import java.security.MessageDigest

object Convert {
    fun toMd5HexString(string: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(string.toByteArray())

        with(StringBuilder()) {
            digest.forEach {
                val hex = it.toInt() and (0xFF)
                val toHexString = Integer.toHexString(hex)
                if (toHexString.length == 1) {
                    this.append("0$toHexString")
                } else {
                    this.append(toHexString)
                }
            }
            return this.toString()
        }
    }
}