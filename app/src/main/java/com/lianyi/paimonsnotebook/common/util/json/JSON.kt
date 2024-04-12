package com.lianyi.paimonsnotebook.common.util.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.lang.reflect.Type

object JSON {
    val gson: Gson by lazy {
        GsonBuilder()
            .setObjectToNumberStrategy {
                return@setObjectToNumberStrategy it.nextLong()
            }
            .create()
    }

    //空对象
    const val EMPTY_OBJ = "{}"

    //空列表
    const val EMPTY_LIST = "[]"


    inline fun <reified T> parse(json: String): T = gson.fromJson(json, T::class.java)

    inline fun <reified T> parse(json: String, type: Type): T =
        gson.fromJson(json, type)

    fun stringify(obj: Any?): String = gson.toJson(obj)

    inline fun <reified T> parseList(json: String): List<T> {
        val list = mutableListOf<T>()
        val array = JSONArray(json)
        repeat(array.length()) {
            list += parse<T>(array[it].toString())
        }
        return list
    }
}