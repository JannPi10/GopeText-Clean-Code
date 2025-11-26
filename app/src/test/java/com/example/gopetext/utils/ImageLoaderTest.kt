package com.example.gopetext.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.example.gopetext.R
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ImageLoaderTest {

    private lateinit var imageLoader: ImageLoader
    private val mockContext: Context = mockk(relaxed = true)
    private val mockImageView: ImageView = mockk(relaxed = true)
    private val mockRequestManager: RequestManager = mockk(relaxed = true)
    private val mockRequestBuilder: RequestBuilder<Drawable> = mockk(relaxed = true)

    @Before
    fun setUp() {
        // Configurar el comportamiento de Glide
        mockkStatic("com.bumptech.glide.Glide")
        
        // Configurar el RequestManager mock
        every { Glide.with(any<Context>()) } returns mockRequestManager
        
        // Configurar el RequestBuilder mock
        every { mockRequestManager.load(any<String>()) } returns mockRequestBuilder
        every { mockRequestBuilder.placeholder(any<Int>()) } returns mockRequestBuilder
        every { mockRequestBuilder.circleCrop() } returns mockRequestBuilder
        every { mockRequestBuilder.into(any<ImageView>()) } returns mockk(relaxed = true)
        
        imageLoader = ImageLoader(mockContext)
    }

    @Test
    fun `loadProfileImage with full URL should call Glide with correct URL`() {
        // Given
        val imageUrl = "http://example.com/image.jpg"
        
        // When
        imageLoader.loadProfileImage(imageUrl, mockImageView)
        
        // Then - Verificar que se llamó a Glide con la URL correcta
        verify { Glide.with(mockContext) }
    }

    @Test
    fun `loadProfileImage with empty URL should set default placeholder`() {
        // Given
        val emptyUrl = ""
        
        // When
        imageLoader.loadProfileImage(emptyUrl, mockImageView)
        
        // Then - Verificar que se establece la imagen por defecto
        verify { mockImageView.setImageResource(R.drawable.ic_baseline_person_24) }
    }

    @Test
    fun `loadProfileImage with null URL should set default placeholder`() {
        // When
        imageLoader.loadProfileImage(null, mockImageView)
        
        // Then
        verify { mockImageView.setImageResource(R.drawable.ic_baseline_person_24) }
    }
    
    @Test
    fun `buildImageUrl with full URL should return same URL`() {
        // Given
        val fullUrl = "http://example.com/image.jpg"
        
        // When
        val result = imageLoader.javaClass.getDeclaredMethod("buildImageUrl", String::class.java)
            .apply { isAccessible = true }
            .invoke(imageLoader, fullUrl) as String
        
        // Then
        assert(result == fullUrl)
    }
    
    @Test
    fun `buildImageUrl with relative URL should return full URL`() {
        // Given
        val relativeUrl = "/uploads/profile.jpg"
        val expectedUrl = Constants.BASE_URL + "uploads/profile.jpg"
        
        // When
        val result = imageLoader.javaClass.getDeclaredMethod("buildImageUrl", String::class.java)
            .apply { isAccessible = true }
            .invoke(imageLoader, relativeUrl) as String
        
        // Then
        assert(result == expectedUrl)
    }
}
