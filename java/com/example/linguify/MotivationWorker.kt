package com.example.linguify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlin.random.Random

class MotivationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val messages = listOf(
        "Did you know 'Bonjour' means 'Hello'? Come back and discover more French words!",
        "Can you guess what 'Tortue' means? It’s 'Turtle'! Let’s not be slow; come back and speed up your learning!",
        "What does 'Chat' mean? It's 'Cat'! Come back to purr-fect your French skills!",
        "Hey, are you coming back? Tu nous manques (We miss you)!",
        "What does 'Merci' mean? It means 'Thank you'! Join us again to learn more!",
        "Can you guess what 'Cuisine' means? It’s 'Kitchen' or 'Cooking'! Come back to savor more French terms!",
        "Ever heard of 'Chocolat'? It means 'Chocolate'! Come back to explore more sweet French words!",
        "What’s 'Pamplemousse'? It’s a grapefruit! Come check it out before it rolls away!",
        "Ever heard of 'Brouillard'? It means 'Fog'—because it’s foggy outside, and your French should be crystal clear! Come back to see through the haze!"
    )

    override fun doWork(): Result {
        Log.d("MotivationWorker", "Running MotivationWorker")

        // Check for notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.w("MotivationWorker", "Notification permission not granted")
                return Result.retry()
            }
        }

        try {
            val randomMessage = messages.random()
            sendNotification(randomMessage)
        } catch (e: Exception) {
            Log.e("MotivationWorker", "Error occurred: ${e.message}")
            return Result.failure()
        }

        return Result.success()
    }


    private fun sendNotification(message: String) {
        createNotificationChannel() // Ensure channel exists

        // Check for null values before creating a notification
        if (applicationContext == null) {
            Log.w("MotivationWorker", "Application context is null")
            return
        }

        val notification = NotificationCompat.Builder(applicationContext, "MOTIVATION_CHANNEL")
            .setSmallIcon(R.drawable.logowithoutbg)  // Ensure this icon exists
            .setContentTitle("Hello!!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Check for notification permission
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("MotivationWorker", "Notification permission not granted")
            return  // Do not send the notification if permission is not granted
        }

        // Use a fixed notification ID instead of a random one
        val notificationId = 1001  // Or any other unique ID you choose
        NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
        Log.d("MotivationWorker", "Notification sent: $message")
    }




    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "MOTIVATION_CHANNEL"
            val name = "Motivation Channel"
            val descriptionText = "Channel for motivational messages"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.d("MotivationWorker", "Notification channel created: $name")
        }
    }
}
