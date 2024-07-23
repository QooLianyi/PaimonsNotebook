package com.lianyi.core.common.util

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAHelper {
    val defaultPublicKey = """
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDvekdPMHN3AYhm/vktJT+YJr7cI5DcsNKqdsx5DZX0gDuWFuIjzdwButrIYPNmRJ1G8ybDIF7oDW2eEpm5sMbL9zs
9ExXCdvqrn51qELbqj0XxtMTIpaCHFSI50PfPpTFV9Xt/hmyVwokoOXFlAEgCn+Q
CgGs52bFoYMtyi+xEQIDAQAB
        """.trimIndent().replace("\\s".toRegex(), "")

    // 从Base64字符串中获取公钥
    fun getPublicKeyFromString(key: String): PublicKey {
        val byteKey = java.util.Base64.getDecoder().decode(key)
        val keySpec = X509EncodedKeySpec(byteKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    // 使用公钥加密消息
    fun encryptWithPublicKey(data: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(data)
        return java.util.Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun encryptWithPublicKeyString(content: String, publicKeyString: String = defaultPublicKey): String {
        val publicKey = getPublicKeyFromString(publicKeyString)
        return encryptWithPublicKey(content.toByteArray(), publicKey)
    }
}