package com.lianyi.paimonsnotebook.common.database.user.type_converter

/*
* 类型转换接口
* E为实体类型
* D为数据库存储类型
* */
interface ITypeConverter<E,D> {

    fun convertToEntityProperty(databaseValue:D):E

    fun convertToDatabaseValue(entity:E):D

}