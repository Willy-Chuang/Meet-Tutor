package com.willy.metu.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.data.Result
import com.willy.metu.data.source.MeTuDataSource
import com.willy.metu.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MeTuRemoteDataSource : MeTuDataSource {

    private const val PATH_EVENTS = "event"
    private const val KEY_CREATED_TIME = "createdTime"

    override suspend fun getEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MeTuApplication.appContext.getString(R.string.you_shall_not_pass)))
                }
            }
    }

    override fun getLiveEvents(): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .addSnapshotListener { snapshot, exception ->
                Logger.i("add SnapshotListener detected")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }
                liveData.value = list
            }

        return liveData

    }
}