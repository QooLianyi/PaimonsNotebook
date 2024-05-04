package com.lianyi.paimonsnotebook.ui.screen.base.file_operation.data

import java.io.File

data class FileOperationData(
    val name: String,
    val sizeText: String,
    val lastModifierTimeText: String,
    val file: File
)
