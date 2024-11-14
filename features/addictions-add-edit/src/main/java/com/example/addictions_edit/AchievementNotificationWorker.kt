package com.example.addictions_edit

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.activity_api.MainActivityIntentRouter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.misufoil.addictions.uikit.R as uikitR

@HiltWorker
class AchievementNotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val mainActivityIntentRouter: MainActivityIntentRouter
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {
            val id = inputData.getString("id")
            val title = inputData.getString("title")
            val duration = inputData.getString("duration")

            sendNotification(id, title, duration)
            Result.success()
        } catch (e: Exception) {

            e.message?.let {
                Log.e(
                    "com.example.addictions_edit.AchievementNotificationWorker",
                    it
                )
            }
            Result.failure()
        }
    }

    private fun sendNotification(id: String?, title: String?, duration: String?) {
        val uniqueNotificationId = id?.hashCode() ?: System.currentTimeMillis().toInt()

        val intent = id?.let { mainActivityIntentRouter.launch(applicationContext, it) }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "addiction_channel")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentText(context.getString(uikitR.string.notif_text, duration))
            .setContentTitle(title)
            .setSmallIcon(uikitR.drawable.baseline_celebration_24)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("POST_NOTIFICATIONS", "Reminder name , todo $title")
        }

        notificationManagerCompat.notify(uniqueNotificationId, notification.build())
        return
    }
}