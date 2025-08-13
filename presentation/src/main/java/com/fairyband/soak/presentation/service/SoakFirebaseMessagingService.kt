package com.fairyband.soak.presentation.service

import android.app.NotificationManager
import androidx.core.content.getSystemService
import com.fairyband.soak.presentation.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class SoakFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("새로운 FCM 토큰이에유: ${token}")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d( "From: ${remoteMessage.from}")

        remoteMessage.data.let {
            Timber.d("Message data payload: $it")
        }

        remoteMessage.notification?.let {
            Timber.d( "Message Notification ${it.title} : ${it.body}")
            showNotification(it.title ?: "Unknown", it.body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"
        val manager = getSystemService<NotificationManager>()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager?.createNotificationChannel(channel)
        }

        val builder = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.symbol)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        manager?.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}