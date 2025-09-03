package com.example.gopetext.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.gopetext.R

fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(this)
    } else {
        setImageResource(R.drawable.ic_baseline_person_24)
    }
}