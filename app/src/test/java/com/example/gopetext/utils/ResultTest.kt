package com.example.gopetext.utils

import org.junit.Test

class ResultTest {

    @Test
    fun `Success should store data correctly`() {
        val data = "test data"
        val result = Result.Success(data)
        assert(result.data == "test data")
        assert(result is Result.Success)
    }

    @Test
    fun `Success with integer should work`() {
        val data = 42
        val result = Result.Success(data)
        assert(result.data == 42)
        assert(result is Result.Success)
    }

    @Test
    fun `Success with null should work`() {
        val data: String? = null
        val result = Result.Success(data)
        assert(result.data == null)
        assert(result is Result.Success)
    }

    @Test
    fun `Success with empty string should work`() {
        val data = ""
        val result = Result.Success(data)
        assert(result.data == "")
        assert(result is Result.Success)
    }

    @Test
    fun `Success with list should work`() {
        val data = listOf("item1", "item2", "item3")
        val result = Result.Success(data)
        assert(result.data == listOf("item1", "item2", "item3"))
        assert(result.data.size == 3)
        assert(result is Result.Success)
    }

    @Test
    fun `Error should store message correctly`() {
        val message = "Something went wrong"
        val result = Result.Error(message)
        assert(result.message == "Something went wrong")
        assert(result is Result.Error)
    }

    @Test
    fun `Error with empty message should work`() {
        val message = ""
        val result = Result.Error(message)
        assert(result.message == "")
        assert(result is Result.Error)
    }

    @Test
    fun `Error with special characters should work`() {
        val message = "Error: ñáéíóú @#$% 123"
        val result = Result.Error(message)
        assert(result.message == "Error: ñáéíóú @#$% 123")
        assert(result is Result.Error)
    }

    @Test
    fun `Success and Error are different types`() {
        val success = Result.Success("data")
        val error = Result.Error("error")
        assert(success is Result.Success)
        assert(error is Result.Error)
        assert(success !is Result.Error)
    }

    @Test
    fun `Success equality should work correctly`() {
        val success1 = Result.Success("test")
        val success2 = Result.Success("test")
        val success3 = Result.Success("different")
        assert(success1 == success2)
        assert(success1 != success3)
    }

    @Test
    fun `Error equality should work correctly`() {
        val error1 = Result.Error("test error")
        val error2 = Result.Error("test error")
        val error3 = Result.Error("different error")
        assert(error1 == error2)
        assert(error1 != error3)
    }

    @Test
    fun `Success and Error should not be equal`() {
        val success = Result.Success("test")
        val error = Result.Error("test")
        assert(success != error)
    }
}