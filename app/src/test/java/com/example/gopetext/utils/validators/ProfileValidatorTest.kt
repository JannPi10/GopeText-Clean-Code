package com.example.gopetext.utils.validators

import org.junit.Before
import org.junit.Test

class ProfileValidatorTest {

    private lateinit var profileValidator: ProfileValidator

    @Before
    fun setup() {
        profileValidator = ProfileValidator()
    }

    @Test
    fun `validateProfileData with valid data should return success`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "25"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(result.isValid)
        assert(result.errorMessage.isEmpty())
    }

    @Test
    fun `validateProfileData with blank name should return error`() {
        val name = ""
        val lastName = "Pérez"
        val age = "25"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "El nombre no puede estar vacío")
    }

    @Test
    fun `validateProfileData with whitespace name should return error`() {
        val name = "   "
        val lastName = "Pérez"
        val age = "25"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "El nombre no puede estar vacío")
    }

    @Test
    fun `validateProfileData with blank lastName should return error`() {
        val name = "Juan"
        val lastName = ""
        val age = "25"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "El apellido no puede estar vacío")
    }

    @Test
    fun `validateProfileData with whitespace lastName should return error`() {
        val name = "Juan"
        val lastName = "   "
        val age = "25"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "El apellido no puede estar vacío")
    }

    @Test
    fun `validateProfileData with blank age should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = ""
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "La edad no puede estar vacía")
    }

    @Test
    fun `validateProfileData with whitespace age should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "   "
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "La edad no puede estar vacía")
    }

    @Test
    fun `validateProfileData with invalid age text should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "abc"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }

    @Test
    fun `validateProfileData with negative age should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "-5"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }

    @Test
    fun `validateProfileData with zero age should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "0"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }

    @Test
    fun `validateProfileData with age over 120 should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "121"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }

    @Test
    fun `validateProfileData with minimum valid age should return success`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "1"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(result.isValid)
        assert(result.errorMessage.isEmpty())
    }

    @Test
    fun `validateProfileData with maximum valid age should return success`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "120"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(result.isValid)
        assert(result.errorMessage.isEmpty())
    }

    @Test
    fun `validateProfileData with decimal age should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "25.5"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }

    @Test
    fun `validateProfileData with age containing spaces should return error`() {
        val name = "Juan"
        val lastName = "Pérez"
        val age = "2 5"
        val result = profileValidator.validateProfileData(name, lastName, age)
        assert(!result.isValid)
        assert(result.errorMessage == "Edad inválida. Ingrese una edad que sea válida.")
    }
}
