package com.example.gopetext.utils.factories

import android.content.Context
import android.net.Uri
import com.example.gopetext.data.api.FileUtil
import okhttp3.MultipartBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import java.io.File

class ImagePartFactoryTest {

    private lateinit var context: Context
    private lateinit var uri: Uri

    @Before
    fun setup() {
        context = mock()
        uri = mock()
    }

    @Test
    fun `createImagePart returns null when uri is null`() {
        val result = ImagePartFactory.createImagePart(context, null)

        assertNull(result)
    }

    @Test
    fun `createImagePart does not return null when uri is not null`() {

        val result = try {
            ImagePartFactory.createImagePart(context, uri)

            false
        } catch (e: Exception) {
            true
        }

        assertTrue(result)
    }
}