package com.example.gopetext.auth.home.fragments.groups

import com.example.gopetext.data.model.UserChat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserAdapterTest {

    private lateinit var adapter: UserAdapter

    @Before
    fun setup() {
        adapter = UserAdapter(emptyList())
    }

//    @Test
//    fun `getItemCount returns correct size`() {
//        val users = listOf(
//            UserChat(1, "User 1", null),
//            UserChat(2, "User 2", null)
//        )
//
//        adapter.setUsers(users)
//
//        assertEquals(2, adapter.itemCount)
//    }

    @Test
    fun `getItemCount returns zero for empty list`() {
        assertEquals(0, adapter.itemCount)
    }

//    @Test
//    fun `setUsers updates adapter data`() {
//        val users = listOf(
//            UserChat(1, "User 1", null),
//            UserChat(2, "User 2", null),
//            UserChat(3, "User 3", null)
//        )
//
//        adapter.setUsers(users)
//
//        assertEquals(3, adapter.itemCount)
//    }
//
//    @Test
//    fun `getSelectedUsers returns empty list initially`() {
//        val users = listOf(UserChat(1, "User", null))
//        adapter.setUsers(users)
//
//        val selectedUsers = adapter.getSelectedUsers()
//
//        assertTrue(selectedUsers.isEmpty())
//    }
//
//    @Test
//    fun `getSelectedUsers returns list copy not reference`() {
//        val users = listOf(UserChat(1, "User", null))
//        adapter.setUsers(users)
//
//        val selectedUsers1 = adapter.getSelectedUsers()
//        val selectedUsers2 = adapter.getSelectedUsers()
//
//        assertTrue(selectedUsers1 !== selectedUsers2)
//    }
}