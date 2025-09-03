package com.example.gopetext.utils

object UrlUtils {
    fun imageUrlBuilder(imagePath: String?): String? {
        if (imagePath.isNullOrBlank()) return null
        return if (imagePath.startsWith("http")) {
            imagePath
        } else {
            Constants.BASE_URL + imagePath.removePrefix("/")
        }
    }
}