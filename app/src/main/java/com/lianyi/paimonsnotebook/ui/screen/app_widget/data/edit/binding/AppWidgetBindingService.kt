package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.binding

import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetDataType
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss.SpiralAbyssData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

//桌面组件绑定配置
class AppWidgetBindingService {

    val bindingListClass = listOf(
        DailyNoteData::class to AppWidgetDataType.DailyNote,
        DailyNoteWidgetData::class to AppWidgetDataType.DailyNoteWidget,
        SpiralAbyssData::class to AppWidgetDataType.Abyss
    )

    //字段名称含义
    val filedMeanMap = mapOf(
        "" to ""
    )

    val bindingClassMap by lazy {
        bindingListClass.associate {
            it.second to getPropertyNames(it.first)
        }
    }

    fun objToMap(obj: Any?, kClass: KClass<*> = obj!!::class, prefix: String = ""): Map<String, Any?> {
        if (obj == null) return emptyMap()

        return kClass.memberProperties.flatMap { prop ->
            val name = if (prefix.isEmpty()) prop.name else "$prefix.${prop.name}"
            val value = prop.getter.call(obj)

            when {
                value == null -> listOf(name to null)
                value::class.isData -> objToMap(value, value::class, name).toList()
                else -> listOf(name to value)
            }
        }.toMap()
    }

    fun getPropertyNames(kClass: KClass<*>, prefix: String = ""): List<String> {
        return kClass.memberProperties.flatMap { prop ->
            val name = if (prefix.isEmpty()) prop.name else "$prefix.${prop.name}"

            val propType = prop.returnType.jvmErasure
            if (propType.isData) {
                getPropertyNames(propType, name)
            } else {
                listOf(name)
            }
        }
    }

//    fun objToMap(obj: KClass<*>, prefix: String = ""): Map<String, Any?> {
//        return obj.memberProperties.flatMap { prop ->
//            val name = if (prefix.isEmpty()) prop.name else "$prefix.${prop.name}"
//            val value = prop.getter.call(obj)
//
//            when {
//                value == null -> listOf(name to null)
//                value::class.isData -> objToMap(value::class, name).toList()
//                else -> listOf(name to value)
//            }
//        }.toMap()
//    }

}
