package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Events
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result

interface MeTuRepository {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(user: String): Result<List<Events>>

    fun getLiveAllEvents(user: String): MutableLiveData<List<Events>>

}