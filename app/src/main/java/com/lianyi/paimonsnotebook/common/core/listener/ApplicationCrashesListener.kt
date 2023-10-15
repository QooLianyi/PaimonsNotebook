package com.lianyi.paimonsnotebook.common.core.listener

import com.microsoft.appcenter.crashes.CrashesListener
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog
import com.microsoft.appcenter.crashes.model.ErrorReport

/*
* 崩溃监听器
*
* */
class ApplicationCrashesListener:CrashesListener {
    //是否应该处理崩溃
    override fun shouldProcess(report: ErrorReport?): Boolean {
        println("shouldProcess = ${report?.stackTrace}")
        return true
    }

    //请求用户同意发送崩溃日志
    override fun shouldAwaitUserConfirmation(): Boolean {
        return true
    }

    //将附件添加到崩溃报告
    override fun getErrorAttachments(report: ErrorReport?): MutableIterable<ErrorAttachmentLog> {
        return mutableListOf()
    }

    //SDK发送崩溃日志前会调用以下回调
    override fun onBeforeSending(report: ErrorReport?) {
    }

    //如果SDK发送崩溃日志失败，将会调用以下回调
    override fun onSendingFailed(report: ErrorReport?, e: Exception?) {
    }

    //SDK发送崩溃日志成功后会调用以下回调
    override fun onSendingSucceeded(report: ErrorReport?) {
    }
}