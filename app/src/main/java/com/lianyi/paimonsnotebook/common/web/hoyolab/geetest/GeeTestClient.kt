package com.lianyi.paimonsnotebook.common.web.hoyolab.geetest

import android.util.Base64
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.extension.request.setDynamicSecret
import com.lianyi.paimonsnotebook.common.extension.request.setUser
import com.lianyi.paimonsnotebook.common.extension.string.toJSON
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.getAsText
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import org.json.JSONObject
import java.net.URLEncoder
import java.security.Key
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import java.util.UUID
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UerEntity


class GeeTestClient {

    var key = byteArrayOf()

    suspend fun miyousheVerification(user: UerEntity): ResultData<VerifyVerificationData>? {
        val verification = createMiYouSheVerification(user)

        println("verification = ${verification}")

        if (!verification.success) {
            println("verification 请求失败")
            return null
        }

        return with(verification.data) {
            val geetestGet = geetestGet(gt, challenge)

            println("geetestGet = ${geetestGet}")

            val type = getTypeV6(gt)
            val ajax = getAjaxV6(gt, challenge)

            println("type = ${type}")

            println("ajax = ${ajax}")

//            if (!ajax.success) {
//                println("ajax ${ajax}")
//                println("ajax 请求失败")
//                return null
//            }
//            val validata = ajax.data.validate

//            miyousheVerifyVerification(user, challenge, validata)
            null
        }

    }

    suspend fun getXrpcChallenge(user: UerEntity): String {
        val verification = createVerification(user)
        if (verification.success) {
            val type = getType(verification.data.gt)
            val ajaxData = getAjax(verification.data.gt, verification.data.challenge)
            if (ajaxData.success) {
                val verificationData = verifyVerification(
                    user,
                    verification.data.challenge,
                    ajaxData.data.validate
                )
                if (verificationData.success) {
                    return verificationData.data.challenge
                }
            }
        }
        return "error"
    }

    private suspend fun geetestGet(gt: String, challenge: String) =
        buildRequest {
            val byteArray2 = ByteArray(16)

            key = byteArray2

            val byteArray3 = ByteArray(16)
            SecureRandom.getInstance("SHA1PRNG").apply {
                nextBytes(byteArray2)
                nextBytes(byteArray3)
            }

            val jsonObject = JSONObject()
            jsonObject.put("lang", "zh-cn")
            val byteArray = jsonObject.toString().toByteArray()

            val base64 = getW(byteArray, byteArray2, byteArray3)
            val w = URLEncoder.encode(base64, "utf-8")

            url(ApiEndpoints.GeetestGet(gt, challenge, w = w))
        }.getAsText()

    suspend fun createVerification(user: UerEntity): ResultData<VerificationData> =
        buildRequest {
            url(ApiEndpoints.CardCreateVerification)
            setUser(user, CookieHelper.Type.Ltoken)
            setDynamicSecret(
                saltType = DynamicSecret.SaltType.X4,
                version = DynamicSecret.Version.Gen2
            )
        }.getAsJson()

    private suspend fun createMiYouSheVerification(user: UerEntity): ResultData<VerificationData> =
        buildRequest {
            url(ApiEndpoints.CreateMiYouSheVerifyVerification)
            setUser(user, CookieHelper.Type.Ltoken)
            setDynamicSecret(
                saltType = DynamicSecret.SaltType.X4,
                version = DynamicSecret.Version.Gen2
            )
        }.getAsJson()

    private suspend fun getType(gt: String): GeeTestResultData<TypeData> =
        buildRequest {
            url(ApiEndpoints.GeetestGetType(gt))
        }.getAsText().removePrefix("(").removeSuffix(")")
            .toJSON(
                getParameterizedType(
                    GeeTestResultData::class.java,
                    TypeData::class.java
                )
            )

    private suspend fun getTypeV6(gt: String): GeeTestResultData<TypeData> =
        buildRequest {
            url(ApiEndpoints.GeetestGetTypeV6(gt, System.currentTimeMillis()))
        }.getAsText().removePrefix("(").removeSuffix(")")
            .toJSON(
                getParameterizedType(
                    GeeTestResultData::class.java,
                    TypeData::class.java
                )
            )


    private suspend fun getAjax(gt: String, challenge: String): GeeTestResultData<AjaxData> =
        buildRequest {
            url(ApiEndpoints.GeetestAjax(gt, challenge))
        }.getAsText().removePrefix("(").removeSuffix(")")
            .toJSON(
                getParameterizedType(
                    GeeTestResultData::class.java,
                    AjaxData::class.java
                )
            )

    private suspend fun getAjaxV6(gt: String, challenge: String) =
        buildRequest {
            url(ApiEndpoints.GeetestAjax(gt, challenge))

            val jsonObject2 = JSONObject().apply {
//                    val fp = with(MessageDigest.getInstance("SHA-256")) {
//                        update(UUID.randomUUID().toString().toByteArray())
//                        digest()
//                    }
//                    put("d", "\$unknown")
//                    put("e", "\$unknown")
//                    put("fp", m65810a(fp))
//                    put("ts", "${System.currentTimeMillis()}")
//                    put("ver", "1.0.0")
//                    put("client_type", "android")
                put("light", "")
                put("gid", JSONObject().apply {
                    put("d", "\$unknown")
                    put("e", "\$unknown")
                    put("fp", UUID.randomUUID().toString())
                    put("\"ts\"", "${System.currentTimeMillis()}")
                    put("ver", "1.0.0")
                    put("client_type", "android")
                }.toString())
            }

            buildMap {
                put("gt", gt)
                put("challenge", challenge)
                put("client_type", "android")
                put("pt", "20")
                put("w", getW(jsonObject2.toString().toByteArray(), key, ByteArray(16).apply {
                    SecureRandom.getInstance("SHA1PRNG").nextBytes(this)
                }))
            }.post(this)
        }.getAsText()


    private suspend fun verifyVerification(
        user: UerEntity,
        challenge: String,
        validate: String,
    ): ResultData<VerifyVerificationData> {
        val bodyMap = buildMap {
            put("geetest_challenge", challenge)
            put("geetest_validate", validate)
            put("geetest_seccode", "$validate|jordan")
        }

        val result = buildRequest {
            url(ApiEndpoints.CardVerifyVerification)
            setUser(user, CookieHelper.Type.Ltoken)
//            setDynamicSecret(
//                DynamicSecret.SaltType.X4,
//                DynamicSecret.Version.Gen2,
//                body = bodyMap.toJson()
//            )
            bodyMap.post(this)

            mapOf(
                "" to ""
            ).post(this)

        }.getAsJson<VerifyVerificationData>()

        return result
    }

    suspend fun verifyVerification(
        user: UerEntity,
        jsonObject: JSONObject,
    ): ResultData<VerifyVerificationData> {
        val bodyMap = buildMap {
            put("geetest_challenge", jsonObject.getString("geetest_challenge"))
            put("geetest_validate", jsonObject.getString("geetest_validate"))
            put("geetest_seccode", jsonObject.getString("geetest_seccode"))
        }

        val result = buildRequest {
            url(ApiEndpoints.CardVerifyVerification)
            setUser(user, CookieHelper.Type.Ltoken)
//            setDynamicSecret(
//                DynamicSecret.SaltType.X4,
//                DynamicSecret.Version.Gen2,
//                body = bodyMap.toJson()
//            )
            bodyMap.post(this)
        }.getAsJson<VerifyVerificationData>()

        return result
    }


    private suspend fun miyousheVerifyVerification(
        user: UerEntity,
        challenge: String,
        validate: String,
    ): ResultData<VerifyVerificationData> {
        val bodyMap = buildMap {
            put("geetest_challenge", challenge)
            put("geetest_validate", validate)
            put("geetest_seccode", "$validate|jordan")
        }

        val result = buildRequest {
            url(ApiEndpoints.MiYouSheVerifyVerification)
            setUser(user, CookieHelper.Type.Stoken)
//            setDynamicSecret(
//                DynamicSecret.SaltType.X4,
//                DynamicSecret.Version.Gen2,
//                body = bodyMap.toJson()
//            )
            bodyMap.post(this)
        }.getAsJson<VerifyVerificationData>()

        return result
    }

    fun m66163a(bArr: ByteArray, bArr2: ByteArray?, bArr3: ByteArray): ByteArray {
        val newInstance =
            Class.forName(m66164a("amF2YXguY3J5cHRvLnNwZWMuSXZQYXJhbWV0ZXJTcGVj")).getConstructor(
                ByteArray::class.java
            ).newInstance(bArr3)
        val newInstance2 =
            Class.forName(m66164a("amF2YXguY3J5cHRvLnNwZWMuU2VjcmV0S2V5U3BlYw==")).getConstructor(
                ByteArray::class.java,
                String::class.java
            ).newInstance(bArr2, "AES")
        val cls = Class.forName(m66164a("amF2YXguY3J5cHRvLkNpcGhlcg=="))
        val invoke = cls.getMethod("getInstance", String::class.java)
            .invoke(cls, "AES/CBC/PKCS5Padding")
        cls.getMethod("init", Integer.TYPE, Key::class.java, AlgorithmParameterSpec::class.java)
            .invoke(invoke, 1, newInstance2, newInstance)
        return cls.getMethod("doFinal", ByteArray::class.java).invoke(invoke, bArr) as ByteArray
    }

    fun getW(bArr: ByteArray, bArr2: ByteArray, bArr3: ByteArray): String? {
        return try {
            val m66163a = m66163a(bArr, bArr2, bArr3)
            val bArr4 = ByteArray(bArr3.size + bArr2.size + m66163a.size)
            System.arraycopy(bArr3, 0, bArr4, 0, bArr3.size)
            System.arraycopy(bArr2, 0, bArr4, bArr3.size, bArr2.size)
            System.arraycopy(m66163a, 0, bArr4, bArr3.size + bArr2.size, m66163a.size)
            Base64.encodeToString(bArr4, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun m66164a(str: String): String {
        return String(Base64.decode(str, 0))
    }


}