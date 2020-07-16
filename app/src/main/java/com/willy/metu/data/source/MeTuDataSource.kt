package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.*

interface MeTuDataSource {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(user: String): Result<List<Event>>

    fun getLiveAllEvents(user: String): MutableLiveData<List<Event>>

    suspend fun postEvent(event: Event): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun updateUser (user: User): Result<Boolean>

    suspend fun getUser (userEmail: String): Result<User>

    suspend fun getAllUsers():Result<List<User>>

    suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean>

    suspend fun getFollowList(userEmail: String): Result<List<User>>

    suspend fun postChatRoom(chatRoom: ChatRoom):Result<Boolean>

    fun getLiveChatRooms(userEmail: String): MutableLiveData<List<ChatRoom>>

    suspend fun postMessage(emails: List<String>, message: Message): Result<Boolean>

    fun getAllLiveMessage (emails: List<String>) : MutableLiveData<List<Message>>

}