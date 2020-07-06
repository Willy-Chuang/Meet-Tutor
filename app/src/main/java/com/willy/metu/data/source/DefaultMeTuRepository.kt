package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Events
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

    override suspend fun getAllEvents(user: String): Result<List<Events>> {
        return remoteDataSource.getAllEvents(user)
    }

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Events>> {
        return remoteDataSource.getLiveAllEvents(user)
    }
}