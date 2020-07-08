package com.willy.metu.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result
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

}