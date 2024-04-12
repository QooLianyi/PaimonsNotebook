package com.lianyi.paimonsnotebook.common.util.enums

/*
* ViewModel的操作类型
*
* 用于在activity中接收来自viewModel的消息
* */
enum class ViewModelAction {
    Finish, //调用activity#finsh()
    None
}