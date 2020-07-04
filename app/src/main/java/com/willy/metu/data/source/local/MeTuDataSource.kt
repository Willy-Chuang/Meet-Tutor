package com.willy.metu.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.willy.metu.data.Event
import com.willy.metu.data.Result
import com.willy.metu.data.source.MeTuDataSource

class MeTuLocalDataSource(val context: Context) : MeTuDataSource {

    override suspend fun getEvents(): Result<List<Event>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveEvents(): MutableLiveData<List<Event>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}