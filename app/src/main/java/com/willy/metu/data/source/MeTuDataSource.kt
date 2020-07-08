package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result

interface MeTuDataSource {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(user: String): Result<List<Event>>

    fun getLiveAllEvents(user: String): MutableLiveData<List<Event>>

    suspend fun postEvent(event: Event): Result<Boolean>

}