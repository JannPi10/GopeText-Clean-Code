package com.example.gopetext.auth.register.validation

class NonEmptyTextValidator(private val fieldLabel: String, private val minLength: Int = 1) {
    fun validate(text: String): String? {
        if (text.isBlank()) return "$fieldLabel is required"
        if (text.length < minLength) return "$fieldLabel must be at least $minLength characters"
        return null
    }
}

class AgeRangeValidator(private val min: Int = 1, private val max: Int = 120) {
    fun validate(ageText: String): String? {
        val value = ageText.toIntOrNull() ?: return "Age must be a number"
        if (value < min) return "Age must be at least $min"
        if (value > max) return "Age must be $max or less"
        return null
    }
}
