package com.example.gopetext.utils.validators

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)

class ProfileValidator {

    fun validateProfileData(name: String, lastName: String, ageText: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre no puede estar vacío")
            lastName.isBlank() -> ValidationResult(false, "El apellido no puede estar vacío")
            ageText.isBlank() -> ValidationResult(false, "La edad no puede estar vacía")
            !isValidAge(ageText) -> ValidationResult(false, "Edad inválida. Ingrese una edad que sea válida.")
            else -> ValidationResult(true)
        }
    }

    private fun isValidAge(ageText: String): Boolean {
        val age = ageText.toIntOrNull() ?: return false
        return age in 1..120
    }
}
