package com.example.gopetext.data.api

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "temp_${System.currentTimeMillis()}"
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file
    }
}