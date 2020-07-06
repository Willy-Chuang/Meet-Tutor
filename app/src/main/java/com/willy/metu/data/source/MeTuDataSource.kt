package com.willy.metu.data.source

import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Events
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result

interface MeTuDataSource {

    suspend fun getSelectedEvents(): Result<List<SelectedEvent>>

    fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>>

    suspend fun getAllEvents(user: Long): Result<List<Events>>

    fun getLiveAllEvents(user: Long): MutableLiveData<List<Events>>

}