package com.example.gopetext.utils

import android.util.Log

object Logger {
    private const val DEFAULT_TAG = "GoPetApp"

    fun d(tag: String = DEFAULT_TAG, message: String) {
        Log.d(tag, message)
    }

    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}
