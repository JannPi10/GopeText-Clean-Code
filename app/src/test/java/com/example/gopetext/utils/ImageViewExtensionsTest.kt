package com.example.gopetext.utils

import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gopetext.R
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageViewExtensionsTest {

    private lateinit var imageView: ImageView
    private val mockGlide = mockk<Glide>(relaxed = true)
    private val mockRequestManager = mockk<com.bumptech.glide.RequestManager>(relaxed = true)
    private val mockRequestBuilder = mockk<com.bumptech.glide.RequestBuilder<android.graphics.drawable.Drawable>>(relaxed = true)

    @Before
    fun setUp() {
        // Initialize the application context
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        imageView = ImageView(context)
        
        // Mock Glide
        mockkStatic(Glide::class)
        every { Glide.with(any<android.content.Context>()) } returns mockRequestManager
        every { mockRequestManager.load(any<String>()) } returns mockRequestBuilder as com.bumptech.glide.RequestBuilder<android.graphics.drawable.Drawable>
        every { mockRequestBuilder.placeholder(any<Int>()) } returns mockRequestBuilder
        every { mockRequestBuilder.into(any<ImageView>()) } returns mockk(relaxed = true)
    }

    @Test
    fun `loadImage with valid URL should load image using Glide`() {
        // Given
        val imageUrl = "https://example.com/image.jpg"

        // When
        imageView.loadImage(imageUrl)

        // Then
        verify { 
            Glide.with(imageView.context)
            mockRequestManager.load(imageUrl)
            mockRequestBuilder.placeholder(R.drawable.ic_baseline_person_24)
            mockRequestBuilder.into(imageView)
        }
    }

    @Test
    fun `loadImage with null URL should set default placeholder`() {
        // Given
        val nullUrl: String? = null

        // When
        imageView.loadImage(nullUrl)

        // Then
        verify(exactly = 0) { 
            Glide.with(any<android.content.Context>())
            mockRequestManager.load(any<String>())
        }
    }

    @Test
    fun `loadImage with empty URL should set default placeholder`() {
        // Given
        val emptyUrl = ""

        // When
        imageView.loadImage(emptyUrl)

        // Then
        verify(exactly = 0) { 
            Glide.with(any<android.content.Context>())
            mockRequestManager.load(any<String>())
        }
    }
}
