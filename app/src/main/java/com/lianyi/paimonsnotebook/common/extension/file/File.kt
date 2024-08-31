package com.lianyi.paimonsnotebook.common.extension.file

import com.google.gson.stream.JsonReader
import java.io.File
import java.io.InputStreamReader

fun File.createJsonReader() =
    JsonReader(InputStreamReader(this.inputStream()))