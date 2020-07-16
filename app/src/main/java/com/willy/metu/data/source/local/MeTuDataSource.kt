package com.willy.metu.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.*
import com.willy.metu.data.source.MeTuDataSource

class MeTuLocalDataSource(val context: Context) : MeTuDataSource {

    override suspend fun getSelectedEvents(): Result<List<SelectedEvent>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllEvents(user: String): Result<List<Event>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Event>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postEvent(event: Event): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateUser(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUser(userEmail: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllUsers(): Result<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postUserToFollow(userEmail: String, user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFollowList(userEmail: String): Result<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}