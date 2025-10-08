package com.example.gopetext.utils

import org.junit.Test

class ErrorUtilsTest {

    @Test
    fun `parseError with valid JSON should return ErrorResponse`() {
        val validJson = """{"error":{"message":"Test error","code":400}}"""
        val result = ErrorUtils.parseError(validJson)
        assert(result != null)
        assert(result?.error?.message == "Test error")
        assert(result?.error?.code == 400)
    }

    @Test
    fun `parseError with null should return null`() {
        val json: String? = null
        val result = ErrorUtils.parseError(json)
        assert(result == null)
    }

    @Test
    fun `parseError with empty string should return null`() {
        val json = ""
        val result = ErrorUtils.parseError(json)
        assert(result == null)
    }

    @Test
    fun `parseError with invalid JSON should return null`() {
        val invalidJson = "invalid json"
        val result = ErrorUtils.parseError(invalidJson)
        assert(result == null)
    }

    @Test
    fun `parseError with malformed JSON should return null`() {
        val malformedJson = """{"error":{"message":"Test error","code":}"""
        val result = ErrorUtils.parseError(malformedJson)
        assert(result == null)
    }

    @Test
    fun `parseError with incomplete JSON should return null`() {
        val incompleteJson = """{"error":{"message":"Test error"""
        val result = ErrorUtils.parseError(incompleteJson)
        assert(result == null)
    }

    @Test
    fun `parseError with wrong structure should return null`() {
        val wrongStructureJson = """{"message":"Test error","code":400}"""
        val result = ErrorUtils.parseError(wrongStructureJson)
        assert(result == null)
    }

    @Test
    fun `parseError with different error message should work`() {
        val json = """{"error":{"message":"User not found","code":404}}"""
        val result = ErrorUtils.parseError(json)
        assert(result != null)
        assert(result?.error?.message == "User not found")
        assert(result?.error?.code == 404)
    }

    @Test
    fun `parseError with zero code should work`() {
        val json = """{"error":{"message":"Success","code":0}}"""
        val result = ErrorUtils.parseError(json)
        assert(result != null)
        assert(result?.error?.message == "Success")
        assert(result?.error?.code == 0)
    }

    @Test
    fun `parseError with negative code should work`() {
        val json = """{"error":{"message":"Internal error","code":-1}}"""
        val result = ErrorUtils.parseError(json)
        assert(result != null)
        assert(result?.error?.message == "Internal error")
        assert(result?.error?.code == -1)
    }

    @Test
    fun `parseError with empty message should work`() {
        val json = """{"error":{"message":"","code":500}}"""
        val result = ErrorUtils.parseError(json)
        assert(result != null)
        assert(result?.error?.message == "")
        assert(result?.error?.code == 500)
    }

    @Test
    fun `parseError with special characters in message should work`() {
        val json = """{"error":{"message":"Error: ñáéíóú @#$%","code":400}}"""
        val result = ErrorUtils.parseError(json)
        assert(result != null)
        assert(result?.error?.message == "Error: ñáéíóú @#$%")
        assert(result?.error?.code == 400)
    }

    @Test
    fun `parseError with whitespace JSON should return null`() {
        val json = "   "
        val result = ErrorUtils.parseError(json)
        assert(result == null)
    }

    @Test
    fun `parseError with array JSON should return null`() {
        val json = """[{"error":{"message":"Test","code":400}}]"""
        val result = ErrorUtils.parseError(json)
        assert(result == null)
    }

    @Test
    fun `parseError with nested object should return null`() {
        val json = """{"data":{"error":{"message":"Test","code":400}}}"""
        val result = ErrorUtils.parseError(json)
        assert(result == null)
    }
}
