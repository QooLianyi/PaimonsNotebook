package com.lianyi.paimonsnotebook.common.database.app_widget_binding.data

import android.graphics.Paint
import android.graphics.Paint.Cap
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetComponentType
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetDataType
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetImageScaleType

/*
* 桌面组件数据原型
* */
data class AppWidgetData(
    val name: String,
    val desc: String,
    val width: Int,
    val height: Int,
    val background: Background,
    val components: List<Component>
) {

    //基础组件,定义了组件的基础属性
    class Base private constructor() {
        abstract class Component {
            //坐标
            abstract val x: Float
            abstract val y: Float

            //宽度
            abstract val width: Float

            //高度
            abstract val height: Float

            //旋转角度
            abstract val rotate: Float

            //数据
            abstract val list: List<Data>
        }

        abstract class Data {
            //数据类型,类型不为none代表绑定了某个数据上,根据类型进行反射获取
            //如果为none,直接获取value的值
            abstract val dataType: AppWidgetDataType

            //反射取值的key,绑定多个,按照key的顺序来获取
            //如果key的值为#(井号)代表直接使用value的值,而不是反射取值
            abstract val key: String
            abstract val value: String
        }
    }

    data class Background(
        val data: String,
        val isUrl: Boolean,
        val isImage: Boolean,
        val tintColor: Int,
        val enableImageBackground: Boolean,
        val scaleType: AppWidgetImageScaleType,
        val radius: Float
    )

    //桌面组件 组件数据
    data class Component(
        val type: AppWidgetComponentType,
        val text: Text? = null,
        val image: Image? = null,
        val progressBar: ProgressBar? = null
    ) {
        //文本类型
        data class Text(
            //字号 sp
            val textSize: Float,
            //颜色
            val color: Int,
            //字体样式,粗细斜体
            val style: Int,
            //对齐方式
            val align: Paint.Align,

            override val x: Float,
            override val y: Float,
            override val width: Float,
            override val height: Float,
            override val rotate: Float,
            override val list: List<Base.Data>,
        ) : Base.Component()

        data class Image(
            //存储的路径是否是url
            val isUrl: Boolean,
            //存储的是否是资源文件id
            val isResId: Boolean,
            //前景色
            val tintColor: Int,
            //图片缩放类型
            val scaleType: AppWidgetImageScaleType,

            override val x: Float,
            override val y: Float,
            override val width: Float,
            override val height: Float,
            override val rotate: Float,
            override val list: List<Base.Data>,
        ) : Base.Component()

        data class ProgressBar(
            //线条类型
            val lineCap: Cap,

            //是否是线性的(水平)
            val isLinear: Boolean,

            //前景色
            val primaryColor: Int,
            //背景色
            val secondColor: Int,

            override val x: Float,
            override val y: Float,
            override val width: Float,
            override val height: Float,
            override val rotate: Float,
            override val list: List<Base.Data>,
        ) : Base.Component()

    }

}
