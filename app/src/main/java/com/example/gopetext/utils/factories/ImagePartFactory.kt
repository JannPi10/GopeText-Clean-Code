package com.example.gopetext.utils.factories

import android.content.Context
import android.net.Uri
import com.example.gopetext.data.api.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

object ImagePartFactory {

    fun createImagePart(context: Context, uri: Uri?): MultipartBody.Part? {
        return uri?.let {
            val file = FileUtil.from(context, it)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
    }
}