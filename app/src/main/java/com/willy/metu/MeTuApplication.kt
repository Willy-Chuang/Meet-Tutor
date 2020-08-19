package com.willy.metu

import android.app.Application
import android.content.Context
import androidx.work.*
import com.willy.metu.data.source.MeTuRepository
import com.willy.metu.util.KEY_EVENT_CONTENT
import com.willy.metu.util.KEY_EVENT_TIME
import com.willy.metu.util.ServiceLocator
import com.willy.metu.worker.MyWorker
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MeTuApplication : Application() {

    // Depends on the flavor,
    val meTuRepository: MeTuRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: MeTuApplication by Delegates.notNull()
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        MeTuApplication.appContext = applicationContext
        instance = this
    }

    fun isLiveDataDesign() = true

    fun setWork (eventTime: Long, eventContent: String) {

        // Set firing time 6 hours early
        var diffTime = eventTime - 21600000 - Calendar.getInstance().timeInMillis

        diffTime = if (eventTime - Calendar.getInstance().timeInMillis <= 21600000) {
            60000
        } else {
            eventTime - 21600000 - Calendar.getInstance().timeInMillis
        }

        // Create the Constraints
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // Define the input
        val eventData = workDataOf(KEY_EVENT_TIME to eventTime, KEY_EVENT_CONTENT to eventContent)

        // Bring it all together by creating the WorkRequest; this also sets the back off criteria
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(eventData)
                .setConstraints(constraints)
                .setInitialDelay(diffTime, TimeUnit.MILLISECONDS)
                .build()

        // Set work
        WorkManager.getInstance(this).enqueue(uploadWorkRequest)

    }
}