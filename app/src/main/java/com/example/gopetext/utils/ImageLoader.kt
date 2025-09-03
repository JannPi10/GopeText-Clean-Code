package com.example.gopetext.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.gopetext.R
import com.example.gopetext.utils.Constants

class ImageLoader(private val context: Context) {

    fun loadProfileImage(imageUrl: String?, imageView: ImageView) {
        val fullImageUrl = imageUrl?.let { buildImageUrl(it) }

        if (!fullImageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(fullImageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    private fun buildImageUrl(imageUrl: String): String {
        return if (imageUrl.startsWith("http")) {
            imageUrl
        } else {
            Constants.BASE_URL + imageUrl.removePrefix("/")
        }
    }
}