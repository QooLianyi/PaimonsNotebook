package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit

import android.graphics.Paint
import android.graphics.Paint.Cap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetData
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetComponentType
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetDataType
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetImageScaleType
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_7
import java.util.UUID

/*
* 桌面组件编辑数据
* */
class AppWidgetEditData : Cloneable {
    //组件名称
    var appWidgetName by mutableStateOf("默认名称")

    //背景
    var background = Background()
        private set

    //组件
    var components = mutableStateListOf<Component>()
        private set

    //获取未锁定的组件的个数
    fun getUnLockedComponentCount() = components.count { !it.isLocked }


    class Base private constructor() {
        open class ComponentImpl : AppWidgetData.Base.Component() {
            //单位px
            override var x by mutableFloatStateOf(100f)
            override var y by mutableFloatStateOf(100f)

            //单位dp
            override var width by mutableFloatStateOf(0f)
            override var height by mutableFloatStateOf(0f)

            //实际值
            override var rotate by mutableFloatStateOf(0f)
            override var list = mutableStateListOf<DataImpl>()

            open fun updateDataByIndex(
                key: String,
                value: String,
                appWidgetDataType: AppWidgetDataType,
                index: Int
            ) {
                val baseData = list[index]
                list[index] = baseData.copy(
                    value = value,
                    key = key,
                    dataType = appWidgetDataType
                )
            }

            open fun updateDataByIndex(
                data: DataImpl,
                index: Int
            ) {
                list[index] = data
            }

            open fun addValue(
                value: String,
                key: String = "#",
                dataType: AppWidgetDataType = AppWidgetDataType.None
            ) {
                list += DataImpl(
                    dataType = dataType,
                    value = value,
                    key = key
                )
            }

            open fun getValue(): String {
                return list.joinToString { it.value }
            }
        }

        data class DataImpl(
            override val dataType: AppWidgetDataType,
            override val key: String,
            override val value: String
        ) : AppWidgetData.Base.Data() {
            val enabled: Boolean
                get() = key == "#"
        }
    }

    class Background {
        var isUrl by mutableStateOf(false)
        var isImage by mutableStateOf(false)
        var data by mutableStateOf("")
        var color by mutableStateOf(Color.White)
        var radius by mutableFloatStateOf(0f)
    }

    data class Component(
        val type: AppWidgetComponentType,
        val text: Text? = null,
        val image: Image? = null,
        val progressBar: ProgressBar? = null,
        val line: Line? = null,
        val circle: Circle? = null,
        val rectangle: Rectangle? = null,
        val triangle: Triangle? = null
    ) {
        val baseComponent: Base.ComponentImpl
            get() = listOf(
                text,
                image,
                progressBar,
                line,
                circle,
                rectangle,
                triangle
            ).takeFirstIf { it != null } ?: error("BaseComponentImpl创建失败:组件均为空")

        var showBorder by mutableStateOf(false)
        var isSelected by mutableStateOf(false)

        //组件是否锁定了,锁定后无法进行任何操作
        var isLocked by mutableStateOf(false)

        //组件唯一id
        val uuid = UUID.randomUUID().toString()

        class Text : Base.ComponentImpl() {
            //字号 sp
            var textSize by mutableFloatStateOf(14f)

            //字间距
            var textSpacer by mutableFloatStateOf(0f)

            //颜色
            var color by mutableIntStateOf(Color.Black.toArgb())

            //字体样式,粗细斜体
            var style by mutableIntStateOf(0)

            //加粗
            var isBold by mutableStateOf(false)

            //斜体
            var isItalic by mutableStateOf(false)

            //下划线
            var isUnderLine by mutableStateOf(false)

            //删除线
            var isStrikeThrough by mutableStateOf(false)


            //对齐方式
            var align: Paint.Align by mutableStateOf(Paint.Align.LEFT)

            var stringValueOrigin by mutableStateOf("")
                private set

            fun updateStringValueOrigin() {
                val sb = StringBuilder()
                list.forEach {
                    if (it.enabled) {
                        sb.append(it.value)
                    } else {
                        sb.append("{${it.key}}")
                    }
                }

                stringValueOrigin = sb.toString()
            }

            fun updateDataByIndex(
                value: String,
                index: Int,
                item: Base.DataImpl
            ) {
                super.updateDataByIndex(
                    key = item.key,
                    value = value,
                    appWidgetDataType = item.dataType,
                    index = index
                )
                updateStringValueOrigin()
            }

            override fun updateDataByIndex(data: Base.DataImpl, index: Int) {
                super.updateDataByIndex(data, index)
                updateStringValueOrigin()
            }

            override fun addValue(value: String, key: String, dataType: AppWidgetDataType) {
                super.addValue(value, key, dataType)
                updateStringValueOrigin()
            }

            init {
                super.width = 160f
                super.height = 30f

                addValue("输入内容或绑定数据")
            }
        }

        class Image : Base.ComponentImpl() {
            //存储的路径是否是url
            var isUrl by mutableStateOf(false)

            //存储的是否是资源文件id
            var isResId by mutableStateOf(false)

            //前景色
            var tintColor by mutableIntStateOf(Color.Black.toArgb())

            //图片缩放类型
            var scaleType by mutableStateOf(AppWidgetImageScaleType.FitCenter)

            init {
                super.width = 100f
                super.height = 100f

                addValue(value = R.drawable.icon_klee.toString())
            }
        }

        class ProgressBar : Base.ComponentImpl() {
            //线条类型
            var lineCap by mutableStateOf(Cap.ROUND)

            //前景色
            val primaryColor by mutableIntStateOf(Primary.toArgb())

            //背景色
            val secondColor by mutableIntStateOf(Primary_7.toArgb())

            init {
                super.width = 100f
                super.height = 10f
            }
        }

        class Line : Base.ComponentImpl() {
            //线条类型
            var lineCap by mutableStateOf(Cap.ROUND)

            var color by mutableIntStateOf(Color.Black.toArgb())

            init {
                super.width = 100f
                super.height = 10f
            }
        }

        class Rectangle : Base.ComponentImpl() {
            var color by mutableIntStateOf(Color.Black.toArgb())

            //线条类型
            var lineCap by mutableStateOf(Cap.ROUND)

            //线条宽度
            var lineWidth by mutableFloatStateOf(16f)

            //是否填充
            var isFull by mutableStateOf(true)

            init {
                super.width = 100f
                super.height = 100f
            }
        }

        class Circle : Base.ComponentImpl() {
            //线条宽度
            var lineWidth by mutableFloatStateOf(16f)

            //是否填充
            var isFull by mutableStateOf(true)

            var color by mutableIntStateOf(Color.Black.toArgb())

            init {
                super.width = 100f
                super.height = 100f
            }
        }

        class Triangle : Base.ComponentImpl() {
            //线条类型
            var lineCap by mutableStateOf(Cap.ROUND)

            //线条宽度
            var lineWidth by mutableFloatStateOf(16f)

            //是否填充
            var isFull by mutableStateOf(true)

            var color by mutableIntStateOf(Color.Black.toArgb())

            init {
                super.width = 100f
                super.height = 100f
            }
        }
    }

    public override fun clone(): AppWidgetEditData {
        return AppWidgetEditData().apply {
            this@apply.appWidgetName = appWidgetName
            this@apply.components = components
            this@apply.background = background
        }
    }

}
