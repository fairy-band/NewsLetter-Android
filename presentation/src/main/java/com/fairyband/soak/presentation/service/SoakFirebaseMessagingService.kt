package com.fairyband.soak.presentation.service

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.fairyband.soak.data.repository.NotificationRepository
import com.fairyband.soak.presentation.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SoakFirebaseMessagingService : FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(job + Dispatchers.IO)
    private val notificationRepository: NotificationRepository by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("새로운 FCM 토큰이에유: ${token}")

        serviceScope.launch {
            notificationRepository.registerFcmToken(fcmToken = token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")

        remoteMessage.data.let {
            Timber.d("Message data payload: $it")
        }

        remoteMessage.notification?.let {
            Timber.d("Message Notification ${it.title} : ${it.body}")
            showNotification(it.title ?: "Unknown", it.body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"
        val manager = getSystemService<NotificationManager>()

        val channel = android.app.NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager?.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.symbol)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        manager?.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}