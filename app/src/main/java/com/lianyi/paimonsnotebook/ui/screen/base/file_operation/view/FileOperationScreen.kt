package com.lianyi.paimonsnotebook.ui.screen.base.file_operation.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.dialog.FilePropertiesOperationDialog
import com.lianyi.paimonsnotebook.common.components.dialog.InputDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.text.InfoText
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel.FileOperationScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Primary_8

open class FileOperationScreen : BaseActivity() {

    protected open val viewModel by lazy {
        ViewModelProvider(this)[FileOperationScreenViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {
                ContentSpacerLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor),
                    contentPadding = PaddingValues(16.dp, 0.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        PrimaryText(text = viewModel.pageTitle)
                    }


                    itemsIndexed(viewModel.fileList) { index, item ->
                        Row(
                            modifier = Modifier
                                .radius(8.dp)
                                .fillMaxWidth()
                                .background(CardBackGroundColor_Light_1)
                                .combinedClickable(
                                    onClick = {
                                        viewModel.onClickItem(index)
                                    },
                                    onLongClick = {
                                        viewModel.onLongClickItem(index)
                                    }
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_document),
                                contentDescription = null,
                                modifier = Modifier
                                    .radius(8.dp)
                                    .size(42.dp)
                                    .background(Primary_8)
                                    .padding(8.dp),
                                tint = Black
                            )

                            Column {
                                PrimaryText(text = item.name)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    InfoText(text = item.lastModifierTimeText)
                                    InfoText(text = item.sizeText)
                                }
                            }
                        }
                    }
                }

                if (viewModel.showPropertiesDialog) {
                    FilePropertiesOperationDialog(
                        title = viewModel.filePropertiesOperationDialogTitle,
                        properties = viewModel.propertyList,
                        onDismissRequest = viewModel::onRequestDismissPropertiesDialog,
                        onClickDelete = viewModel::onClickDelete,
                        onClickSend = viewModel::onClickSend,
                        onClickRename = viewModel::onClickRename
                    )
                }

                if (viewModel.showConfirmDeleteDialog) {
                    ConfirmDialog(
                        title = viewModel.confirmDialogTitle,
                        content = "确定要删除[${viewModel.getCurrentOperationFile().name}]吗?删除后无法恢复!",
                        onConfirm = viewModel::confirmDelete,
                        onCancel = viewModel::dismissConfirmDeleteDialog
                    )
                }

                if (viewModel.showInputDialog) {
                    InputDialog(
                        title = "文件重命名",
                        placeholder = "请输入新的文件名,文件名不可重复",
                        initialValue = (viewModel.getCurrentOperationFile().name ?: "")
                            .split(".")
                            .first(),
                        onConfirm = viewModel::onInputDialogConfirm,
                        onCancel = viewModel::onRequestDismissInputDialog
                    )
                }

                if (viewModel.showLoadingDialog) {
                    LoadingDialog()
                }
            }
        }
    }
}