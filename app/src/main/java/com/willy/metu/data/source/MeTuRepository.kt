package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result
import com.willy.metu.data.User

interface MeTuRepository {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(event: String): Result<List<Event>>

    fun getLiveAllEvents(event: String): MutableLiveData<List<Event>>

    suspend fun postEvent(event: Event): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun updateUser (user: User): Result<Boolean>

    suspend fun getUser (userEmail: String): Result<User>

    suspend fun getAllUsers():Result<List<User>>

}