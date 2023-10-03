package com.lianyi.paimonsnotebook.common.web.hoyolab.cookie

object CookieHelper {

    object Keys {
        const val CookieToken = "cookie_token"
        const val AccountID = "account_id"
        const val LoginTicket = "login_ticket"
        const val Login_UID = "login_uid"
        const val Mid = "mid"

        const val LToken = "ltoken"
        const val LTuid = "ltuid"
        const val SToken = "stoken"
        const val STuid = "stuid"
    }

    object Type {
        const val CookieToken = 0x0001
        const val Ltoken = 0x0010
        const val Stoken = 0x0100
        const val None = 0x00000
        const val Cookie = CookieToken or Ltoken
    }

    fun stringToCookieMap(cookieString: String?): Map<String, String> {
        val cookies = mutableMapOf<String, String>()
        cookieString?.split(";")?.toList()
            ?.forEach { cookie ->
                val index = cookie.indexOfFirst { it == '=' }
                if (index != -1) {
                    val key = cookie.take(index).trim()
                    val value = cookie.takeLast(cookie.length - index - 1)
                    if (key.isNotBlank()) {
                        cookies[key] = value
                    }
                }
            }
        return cookies
    }

}