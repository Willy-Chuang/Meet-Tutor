package com.willy.metu.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.willy.metu.MainActivity
import com.willy.metu.MeTuApplication
import com.willy.metu.MeTuApplication.Companion.appContext
import com.willy.metu.R
import com.willy.metu.util.KEY_EVENT_CONTENT
import com.willy.metu.util.KEY_EVENT_TIME
import com.willy.metu.util.Logger
import java.util.*

class MyWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        createNotificationChannel()

        return try {

            //Get the input
            val eventTime = inputData.getLong(KEY_EVENT_TIME, 0L)
            val content = inputData.getString(KEY_EVENT_CONTENT)
            val currentTime = Calendar.getInstance().timeInMillis

            // Create the notification to be shown
            val mBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(appContext, appContext.getString(R.string.channel_id))
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(MeTuApplication.instance.resources.getColor(R.color.colorPrimary))
                        .setContentTitle(appContext.getString(R.string.upcoming_appointment))
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setWhen(currentTime)
                        .setContentIntent(PendingIntent.getActivity(appContext, 0, Intent(appContext, MainActivity::class.java), 0))
                        .setPriority(Notification.PRIORITY_DEFAULT)
            } else {
                Notification.Builder(appContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(MeTuApplication.instance.resources.getColor(R.color.colorPrimary))
                        .setContentTitle(appContext.getString(R.string.upcoming_appointment))
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setWhen(currentTime)
                        .setContentIntent(PendingIntent.getActivity(appContext, 0, Intent(appContext, MainActivity::class.java), 0))
                        .setPriority(Notification.PRIORITY_DEFAULT)
            }

            // Get the Notification manager service
            val am = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Generate an Id for each notification
            val id = System.currentTimeMillis() / 1000

            // Show a notification
            am.notify(id.toInt(), mBuilder.build())

            Logger.i("Notification Fired")

            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = appContext.getString(R.string.channel_id)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(appContext.getString(R.string.channel_id), name, importance).apply {
                description = appContext.getString(R.string.notification_channel_description)
            }
            val notificationManager: NotificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

}