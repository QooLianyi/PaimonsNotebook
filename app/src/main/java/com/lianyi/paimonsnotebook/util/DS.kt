package com.lianyi.paimonsnotebook.util

import com.lianyi.paimonsnotebook.lib.information.Constants
import java.math.BigInteger
import java.security.MessageDigest

fun getDS(query: String, body: String = ""): String {
    val time = System.currentTimeMillis() / 1000L
    val rs = getRS()
    val cs = "salt=${Constants.API_SALT}&t=${time}&r=${rs}&b=$body&q=$query".digest(Algorithm.MD5)
    return "${time},${rs},${cs}"
}

fun getSignDS(salt: String): String {
    val time = System.currentTimeMillis() / 1000L
    val rs = getRS()
    val cs = "salt=$salt&t=${time}&r=${rs}".digest(Algorithm.MD5)
    return "${time},${rs},${cs}"
}

fun getRS(): String {
    return (100000..200000).random().toString()
}

fun String.digest(algorithm: Algorithm): String = MessageDigest.getInstance(algorithm.s).run {
    BigInteger(1, digest(toByteArray())).toString(16).padStart(algorithm.length, '0')
}

@Suppress("EnumEntryName")
enum class Algorithm(
    val s: String,
    val length: Int
) {
    MD2("MD2", 32),
    MD5("MD5", 32),
    Sha1("SHA-1", 40),
    Sha224("SHA-224", 56),
    Sha256("SHA-256", 64),
    Sha384("SHA-384", 96),
    Sha512("SHA-512", 128),
    `Sha512-224`("SHA-512/224", 56),
    `Sha512-256`("SHA-512/256", 64),
    `Sha3-224`("SHA3-224", 56),
    `Sha3-384`("SHA3-384", 96),
    `Sha3-512`("SHA3-512", 128)
}

fun getQ1(uid: String, server: String): String {
    return "role_id=$uid&server=$server"
}


