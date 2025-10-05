package com.example.gopetext.auth.home.fragments.chats

import android.content.Context
import android.content.Intent
import com.example.gopetext.data.model.Contact
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DefaultChatsNavigatorTest {

    private lateinit var navigator: DefaultChatsNavigator
    private lateinit var context: Context

    @Before
    fun setup() {
        context = mock()
        navigator = DefaultChatsNavigator(context)
    }

    @Test
    fun `navigateToChat creates intent with correct extras for individual chat`() {
        val contact = Contact(1, false, "John Doe", null, 1, null)

        navigator.navigateToChat(contact)

        val intentCaptor = argumentCaptor<Intent>()
        verify(context).startActivity(intentCaptor.capture())

        val intent = intentCaptor.firstValue
        assert(intent.getIntExtra("chatId", -1) == 1)
        assert(intent.getStringExtra("chatName") == "John Doe")
        assert(intent.getBooleanExtra("isGroup", true) == false)
    }

    @Test
    fun `navigateToChat creates intent with correct extras for group chat`() {
        val contact = Contact(5, true, "Team Group", "group.jpg", null, 10)

        navigator.navigateToChat(contact)

        val intentCaptor = argumentCaptor<Intent>()
        verify(context).startActivity(intentCaptor.capture())

        val intent = intentCaptor.firstValue
        assert(intent.getIntExtra("chatId", -1) == 5)
        assert(intent.getStringExtra("chatName") == "Team Group")
        assert(intent.getBooleanExtra("isGroup", false) == true)
    }
}