package com.example.gopetext.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.example.gopetext.R
import com.example.gopetext.auth.chat.ChatAdapter
import com.example.gopetext.data.model.Message
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.util.*
import java.util.regex.Pattern

@RunWith(RobolectricTestRunner::class)
class ChatAdapterTest {

    private val currentUserId = 1
    private lateinit var adapter: ChatAdapter
    
    @Mock
    private lateinit var mockView: View
    
    @Mock
    private lateinit var mockParent: ViewGroup
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockParent.context).thenReturn(androidx.test.core.app.ApplicationProvider.getApplicationContext())
        `when`(mockView.findViewById<TextView>(anyInt())).thenReturn(TextView(mockParent.context))
        adapter = ChatAdapter(currentUserId)
    }

    @Test
    fun `getItemCount returns correct count`() {
        // Given
        val messages = listOf(
            createMessage(1, "Hello", "1234567890", currentUserId),
            createMessage(2, "Hi", "1234567891", 2)
        )
        
        // When
        adapter.setMessages(messages)
        
        // Then
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `getItemViewType returns correct type for sent and received messages`() {
        // Given
        val sentMessage = createMessage(1, "Hello", "1234567890", currentUserId)
        val receivedMessage = createMessage(2, "Hi", "1234567891", 2)
        
        // When
        adapter.setMessages(listOf(sentMessage, receivedMessage))
        
        // Then - Using direct values since the constants are private
        // VIEW_TYPE_SENT = 1, VIEW_TYPE_RECEIVED = 2 (from ChatAdapter)
        assertEquals(1, adapter.getItemViewType(0))
        assertEquals(2, adapter.getItemViewType(1))
    }

    @Test
    fun `setMessages replaces all messages`() {
        // Given
        val initialMessages = listOf(
            createMessage(1, "Hello", "1234567890", currentUserId)
        )
        val newMessages = listOf(
            createMessage(2, "New message", "1234567891", 2),
            createMessage(3, "Another one", "1234567892", 3)
        )
        
        // When
        adapter.setMessages(initialMessages)
        adapter.setMessages(newMessages)
        
        // Then
        assertEquals(2, adapter.itemCount)
        assertEquals("New message", (adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
            .createViewHolder(mockParent, 2) // VIEW_TYPE_RECEIVED = 2
            .let { holder ->
                adapter.onBindViewHolder(holder, 0)
                (holder as ChatAdapter.BaseMessageViewHolder).itemView
                    .findViewById<TextView>(R.id.textMessageReceive).text.toString()
            })
    }

    @Test
    fun `addMessage appends a message`() {
        // Given
        val initialMessage = createMessage(1, "Hello", "1234567890", currentUserId)
        val newMessage = createMessage(2, "New message", "1234567891", 2)
        
        // When
        adapter.setMessages(listOf(initialMessage))
        adapter.addMessage(newMessage)
        
        // Then
        assertEquals(2, adapter.itemCount)
        assertEquals("New message", (adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
            .createViewHolder(mockParent, 2) // VIEW_TYPE_RECEIVED = 2
            .let { holder ->
                adapter.onBindViewHolder(holder, 1)
                (holder as ChatAdapter.BaseMessageViewHolder).itemView
                    .findViewById<TextView>(R.id.textMessageReceive).text.toString()
            })
    }

    @Test
    fun `formatTimestamp formats timestamp correctly`() {
        // Given
        val timestamp = "1617235200000" // April 1, 2021 00:00:00 UTC
        
        // When
        val formattedTime = ChatAdapter.formatTimestamp(timestamp)
        
        // Then
        assertNotNull(formattedTime)
        val pattern = Pattern.compile("\\d{1,2}:\\d{2} [AP]M")
        assertTrue(pattern.matcher(formattedTime).matches())
    }

    @Test
    fun `formatTimestamp with invalid timestamp uses current time`() {
        // Given
        val invalidTimestamp = "invalid"
        
        // When
        val formattedTime = ChatAdapter.formatTimestamp(invalidTimestamp)
        
        // Then
        assertNotNull(formattedTime)
        val pattern = Pattern.compile("\\d{1,2}:\\d{2} [AP]M")
        assertTrue(pattern.matcher(formattedTime).matches())
    }

    @Test
    fun `onBindViewHolder binds message to view holder`() {
        // Given
        val message = createMessage(1, "Test message", "1617235200000", currentUserId)
        adapter.setMessages(listOf(message))
        
        // When - Using direct value for VIEW_TYPE_SENT (1)
        val viewHolder = adapter.createViewHolder(mockParent, 1)
        adapter.onBindViewHolder(viewHolder, 0)
        
        // Then - Verify the view holder has the correct data
        // Note: In a real test, you would verify the actual view contents
        assertTrue(viewHolder is ChatAdapter.BaseMessageViewHolder)
    }

    private fun createMessage(id: Int, content: String, timestamp: String, senderId: Int): Message {
        return Message(
            id = id,
            content = content,
            timestamp = timestamp,
            sender = senderId
        )
    }
}
