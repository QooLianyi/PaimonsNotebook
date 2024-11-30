package com.lianyi.paimonsnotebook.common.database.app_widget_binding.util

//
enum class AppWidgetImageScaleType {
    //无
    None,

    //自适应居中,总是显示整个图片
    FitCenter,

    //剪切居中,超出组件部分不显示
    CropCenter,

    //填充整个组件的宽高
    Full,

    //根据尺寸填充
    BySize
}