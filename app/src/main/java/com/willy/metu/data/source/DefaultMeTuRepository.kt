package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.Result
import com.willy.metu.data.source.local.MeTuLocalDataSource

class DefaultMeTuRepository(
    private val remoteDataSource: MeTuDataSource,
    private val localDataSource: MeTuDataSource
) : MeTuRepository {

    override suspend fun getEvents(): Result<List<Event>> {
        return remoteDataSource.getEvents()
    }

    override fun getLiveEvents(): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveEvents()
    }
}