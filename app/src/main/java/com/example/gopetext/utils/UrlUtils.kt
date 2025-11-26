package com.example.gopetext.utils

object UrlUtils {
    fun imageUrlBuilder(imagePath: String?): String? {
        if (imagePath == null || imagePath.isBlank()) return null
        
        // Check if URL is already absolute (starts with http:// or https://)
        if (imagePath.startsWith("http://", ignoreCase = true) ||
            imagePath.startsWith("https://", ignoreCase = true)) {
            return imagePath
        }
        
        // Remove leading slashes and prepend base URL
        val cleanPath = imagePath.trimStart('/')
        return "${Constants.BASE_URL}/$cleanPath"
    }
}
