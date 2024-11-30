package com.lianyi.paimonsnotebook.ui.widgets.util

/*
* 小组件配置选项
* 根据不同的配置选项显示不同的可配置内容
* */
enum class AppWidgetConfigurationOption {
    //允许在配置界面更改同类型的小组件
    ChangeWidget,
    //游戏角色
    GameRole,
    //用户
    User,
    //背景主题
    BackgroundPatten,
    //背景圆角半径
    BackgroundRadius,
    //背景颜色
    BackgroundColor,
    //字体颜色
    TextColor,
    //图片颜色
    ImageTintColor,
    //进度条颜色 主要
    ProgressBarColorPrimary,
    //进度条颜色 次要
    ProgressBarColorSecond,

    //时间格式
    TimeFormat,
    //自定义背景图片
    CustomBackgroundImage,
    //自定义图片背景
    CustomBackgroundImageUrl,

    //绑定功能
    BindFeature
}