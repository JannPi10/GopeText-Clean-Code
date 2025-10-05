package com.example.gopetext.auth.home.fragments.chats

import com.example.gopetext.data.model.Contact
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChatsListAdapterDiffCallbackTest {

    private lateinit var diffCallback: ChatsListAdapter.DiffCallback

    @Before
    fun setup() {
        diffCallback = ChatsListAdapter.DiffCallback()
    }

    @Test
    fun `areItemsTheSame returns true when ids are equal`() {
        val contact1 = Contact(1, false, "John", null, 1, null)
        val contact2 = Contact(1, true, "Jane", "image.jpg", null, 3)

        val result = diffCallback.areItemsTheSame(contact1, contact2)

        assertEquals(true, result)
    }

    @Test
    fun `areItemsTheSame returns false when ids are different`() {
        val contact1 = Contact(1, false, "John", null, 1, null)
        val contact2 = Contact(2, false, "John", null, 2, null)

        val result = diffCallback.areItemsTheSame(contact1, contact2)

        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns true for identical contacts`() {
        val contact1 = Contact(1, false, "John", "image.jpg", 1, null)
        val contact2 = Contact(1, false, "John", "image.jpg", 1, null)

        val result = diffCallback.areContentsTheSame(contact1, contact2)

        assertEquals(true, result)
    }

    @Test
    fun `areContentsTheSame returns false for different content`() {
        val contact1 = Contact(1, false, "John", null, 1, null)
        val contact2 = Contact(1, false, "Jane", null, 1, null)

        val result = diffCallback.areContentsTheSame(contact1, contact2)

        assertEquals(false, result)
    }
}
