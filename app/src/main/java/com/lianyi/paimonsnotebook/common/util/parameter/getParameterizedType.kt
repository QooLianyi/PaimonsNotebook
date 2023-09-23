package com.lianyi.paimonsnotebook.common.util.parameter

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/*
*   使用时，传入具体类型，拼装成ParameterizedType
*   List<String>的type为
*   val type = getType(List::class.java,String::class.java)
*
*   //List<List<String>>的type为
*   val type = getType(List::class.java,getType(List::class.java,String::class.java))
*
*   //Map<Int,String>的type为
*   val type = getType(List::class.java,Int::class.java,String::class.java)
*
*   //Map<String,List<String>>的类型为
*   val type = getType(Map::class.java,String::class.java, getType(List::class.java,String::class.java))
*
*   //Set<String>的类型为
*   val type = getType(Set::class.java,String::class.java,String::class.java)
*
*   From:https://blog.csdn.net/jingzz1/article/details/107861870
* */

fun getParameterizedType(raw:Class<*>, vararg args: Type) = object :ParameterizedType{
    override fun getActualTypeArguments(): Array<out Type>  = args

    override fun getRawType(): Type = raw

    override fun getOwnerType(): Type? = null
}