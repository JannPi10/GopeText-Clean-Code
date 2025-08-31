package com.example.gopetext.auth.login.validation

import android.util.Patterns

interface FieldValidator {
    fun validate(text: String): String?
}

class EmailValidator : FieldValidator {
    override fun validate(text: String): String? {
        if (text.isBlank()) return "Email is required"
        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) return "Invalid email"
        return null
    }
}

class PasswordValidator : FieldValidator {
    override fun validate(text: String): String? {
        if (text.isBlank()) return "Password is required"
        return null
    }
}
