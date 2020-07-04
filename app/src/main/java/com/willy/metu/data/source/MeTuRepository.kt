package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.Result

interface MeTuRepository {
    suspend fun getEvents(): Result<List<Event>>
    fun getLiveEvents(): MutableLiveData<List<Event>>
}