package com.willy.metu.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.data.Event
import com.willy.metu.data.SelectedEvent
import com.willy.metu.data.Result
import com.willy.metu.data.User
import com.willy.metu.data.source.MeTuDataSource
import com.willy.metu.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MeTuRemoteDataSource : MeTuDataSource {

    private const val PATH_EVENTS = "event"
    private const val PATH_USER = "user"
    private const val KEY_CREATED_TIME = "createdTime"

    override suspend fun getSelectedEvents(): Result<List<SelectedEvent>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<SelectedEvent>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val event = document.toObject(SelectedEvent::class.java)
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

    override fun getLiveSelectedEvents(): MutableLiveData<List<SelectedEvent>> {
        val liveData = MutableLiveData<List<SelectedEvent>>()
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .addSnapshotListener { snapshot, exception ->
                    Logger.i("add SnapshotListener detected")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<SelectedEvent>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(SelectedEvent::class.java)
                        list.add(event)
                    }
                    liveData.value = list
                }

        return liveData

    }

    override suspend fun getAllEvents(user: String): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .whereArrayContains("attendees", user)
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

    override fun getLiveAllEvents(user: String): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        FirebaseFirestore.getInstance()
                .collection(PATH_EVENTS)
                .whereArrayContains("attendees", user)
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

    override suspend fun postEvent(event: Event): Result<Boolean> = suspendCoroutine { continuation ->

        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        val document = events.document()
        event.id = document.id

        document
                .set(event)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Post: $event")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(MeTuApplication.instance.getString(R.string.you_shall_not_pass)))
                    }
                }
    }

    override suspend fun postUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->

        val users = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = users.document()
        user.id = document.id

        document
                .set(user)
                .addOnSuccessListener { documentReference ->
                    Logger.d("DocumentSnapshot added with ID: ${users}")
                }
                .addOnFailureListener { e ->
                    Logger.w("Error adding document $e")
                }

    }


    override suspend fun updateUser(user: User): Result<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveUser(userToken: String): Result<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}