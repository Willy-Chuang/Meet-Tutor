package com.willy.metu.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MyWorker(appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        createNotificationChannel()

        return try {

            //Get the input
            val eventTime = inputData.getLong("eventTime",0L)

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name = "MeeTu"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MeeTu", name, importance).apply {
                description = "MeeT Tutor"
            }
            val notificationManager: NotificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

}