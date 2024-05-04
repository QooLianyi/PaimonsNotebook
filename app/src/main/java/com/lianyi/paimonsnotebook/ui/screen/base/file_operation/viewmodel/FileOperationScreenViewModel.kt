package com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.data.FileOperationData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import java.io.File

open class FileOperationScreenViewModel : ViewModel() {

    val fileList = mutableStateListOf<FileOperationData>()

    var showLoadingDialog by mutableStateOf(false)

    init {
        updateFileList()
    }

    //显示属性弹窗
    var showPropertiesDialog by mutableStateOf(false)
        private set

    val propertyList = mutableStateListOf<Pair<String, String>>()

    var showInputDialog by mutableStateOf(false)
        private set

    var showConfirmDeleteDialog by mutableStateOf(false)
        private set

    //当前操作的记录索引
    private var operationRecordIndex = -1

    fun onRequestDismissInputDialog() {
        showInputDialog = false
    }

    fun onRequestDismissPropertiesDialog() {
        showPropertiesDialog = false
    }

    private fun showPropertiesDialog() {
        setPropertyListData()
    }

    private fun showInputDialog() {
        showInputDialog = true
    }

    private fun showConfirmDeleteDialog() {
        showConfirmDeleteDialog = true
    }

    fun dismissConfirmDeleteDialog() {
        showConfirmDeleteDialog = false
    }

    //更新文件列表
    private fun updateFileList() {
        fileList.clear()

        //将最后修改的数据排到顶部

        getFileList().sortedByDescending { it.lastModified() }.forEach {
            fileList += FileOperationData(
                name = it.name,
                sizeText = FileHelper.getFileSizeDescribeString(it.length()),
                lastModifierTimeText = TimeHelper.getTime(it.lastModified()),
                file = it
            )
        }
    }


    private fun setPropertyListData() {
        showLoadingDialog = true
        val file = fileList[operationRecordIndex].file

        viewModelScope.launchIO {
            getPropertyListData(file,
                {
                    propertyList.clear()
                    propertyList += it

                    showPropertiesDialog = true
                    showLoadingDialog = false
                }, {
                    showLoadingDialog = false
                }
            )
        }
    }

    fun getCurrentOperationFile() = fileList[operationRecordIndex]

    /*
    * 当长按item时,当前是长按调用删除
    * 无效的数据无法打开操作对话框
    * 虽然不知道是怎么混进来的无效数据,看不了还是能删掉的
    * */
    fun onLongClickItem(index: Int) {
        operationRecordIndex = index

        showConfirmDeleteDialog()
    }

    fun onClickItem(index: Int) {
        operationRecordIndex = index
        showPropertiesDialog()
    }

    fun onClickDelete() {
        showConfirmDeleteDialog()
    }

    fun onClickSend() {
        val file = fileList[operationRecordIndex].file
        val uri = FileHelper.getContentUriForFile(file)

        HomeHelper.goActivityByIntentNewTask {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "*/*"
        }
    }

    fun onClickRename() {
        showInputDialog()
    }

    //确认删除
    fun confirmDelete() {
        val data = fileList[operationRecordIndex]

        val name = data.name
        val deleteResult = data.file.delete()

        if (deleteResult) {
            "[${name}]已删除".warnNotify(false)
            updateFileList()
        } else {
            "[${name}]删除失败".errorNotify()
        }

        onRequestDismissPropertiesDialog()
        dismissConfirmDeleteDialog()
    }

    fun onInputDialogConfirm(value: String) {
        val fullName = "${value}.json"
        val haveSameNameFile = fileList.count { it.name == fullName } > 0

        if (haveSameNameFile) {
            "文件名:[${value}]已存在,请输入其它名称".show()
            return
        }

        val dest = getDestFile(value)

        val renameSuccess = fileList[operationRecordIndex].file.renameTo(dest)

        if (renameSuccess) {
            "文件重命名成功".notify()
            updateFileList()
        } else {
            "文件重命名失败,请稍后再试或使用系统的文件管理器修改名称".warnNotify(false)
        }

        onRequestDismissInputDialog()
        onRequestDismissPropertiesDialog()
    }

    //需要重写的部分标题内容
    open val filePropertiesOperationDialogTitle = "文件属性对话框"
    open val confirmDialogTitle = "确认对话框标题"
    open val pageTitle = "页面标题"

    //此类需要子类重写,父类总是返回空文件
    open fun getDestFile(name: String): File = File(name)

    //此类需要子类重写,父类总是返回空数据
    open fun getFileList(): Array<File> = arrayOf()

    //此方法需要子类重写,父类总是调用失败方法
    open suspend fun getPropertyListData(
        file: File,
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFail: () -> Unit
    ) {
        onFail.invoke()
    }
}