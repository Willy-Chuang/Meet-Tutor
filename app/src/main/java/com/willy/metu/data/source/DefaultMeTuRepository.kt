package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result

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
}