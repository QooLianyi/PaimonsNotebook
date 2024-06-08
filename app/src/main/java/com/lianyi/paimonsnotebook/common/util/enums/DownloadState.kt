package com.lianyi.paimonsnotebook.common.util.enums

//下载状态
enum class DownloadState {
    Pause, //暂停
    Error, //失败
    Success, //成功
    Fail, //失败
    Cancel, //取消
    Wait, //等待
    UnStart, //未开始
    Downloading, //正在下载
    WaitAction, //等待操作
    Empty //空
}