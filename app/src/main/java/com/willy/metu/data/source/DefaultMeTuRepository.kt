package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result
import com.willy.metu.data.User

class DefaultMeTuRepository(
    private val remoteDataSource: MeTuDataSource,
    private val localDataSource: MeTuDataSource
) : MeTuRepository {

    override suspend fun getSelectedEvents(): Result<List<SelectedEvent>> {
        return remoteDataSource.getSelectedEvents()
    }

    override fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>> {
        return remoteDataSource.getLiveSelectedEvents()
    }

    override suspend fun getAllEvents(user: String): Result<List<Event>> {
        return remoteDataSource.getAllEvents(user)
    }

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveAllEvents(user)
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        return remoteDataSource.postEvent(event)
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        return remoteDataSource.postUser(user)
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        return remoteDataSource.updateUser(user)
    }

    override suspend fun getUser(userEmail: String): Result<User> {
        return remoteDataSource.getUser(userEmail)
    }

    override suspend fun getAllUsers():Result<List<User>> {
        return remoteDataSource.getAllUsers()
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean>{
        return remoteDataSource.postUserToFollow(userEmail, user)
    }
}