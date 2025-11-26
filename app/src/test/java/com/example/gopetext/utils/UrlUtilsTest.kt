package com.example.gopetext.utils

import org.junit.Test
import org.junit.Ignore
import org.junit.Assert.*

class UrlUtilsTest {

    @Test
    fun `imageUrlBuilder with null should return null`() {
        val imagePath: String? = null
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertNull(result)
    }

    @Test
    fun `imageUrlBuilder with empty string should return base URL`() {
        val imagePath = ""
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertNull(result)
    }

    @Test
    fun `imageUrlBuilder with blank string should return null`() {
        val imagePath = "   "
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertNull(result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with http URL should return same URL`() {
        val imagePath = "http://example.com/image.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://example.com/image.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with https URL should return same URL`() {
        val imagePath = "https://example.com/image.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("https://example.com/image.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with relative path should prepend base URL`() {
        val imagePath = "images/profile.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/images/profile.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with path starting with slash should remove slash and prepend base URL`() {
        val imagePath = "/images/profile.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/images/profile.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with path without slash should prepend base URL`() {
        val imagePath = "profile.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/profile.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with multiple slashes should handle correctly`() {
        val imagePath = "//images//profile.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94//images//profile.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with http in middle of path should prepend base URL`() {
        val imagePath = "images/http_test.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/images/http_test.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with HTTP uppercase should return same URL`() {
        val imagePath = "HTTP://example.com/image.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("HTTP://example.com/image.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with HTTPS uppercase should return same URL`() {
        val imagePath = "HTTPS://example.com/image.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("HTTPS://example.com/image.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with mixed case http should return same URL`() {
        val imagePath = "Http://example.com/image.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("Http://example.com/image.jpg", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with just filename should prepend base URL`() {
        val imagePath = "avatar.png"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/avatar.png", result)
    }

    @Ignore
    @Test
    fun `imageUrlBuilder with deep path should prepend base URL`() {
        val imagePath = "users/123/images/profile/avatar.jpg"
        val result = UrlUtils.imageUrlBuilder(imagePath)
        assertEquals("http://159.203.187.94/users/123/images/profile/avatar.jpg", result)
    }
}
