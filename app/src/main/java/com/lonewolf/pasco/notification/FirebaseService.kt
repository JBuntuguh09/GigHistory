package com.lonewolf.pasco.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lonewolf.pasco.resources.Storage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lonewolf.pasco.MainActivity
import com.lonewolf.pasco.R


import kotlin.random.Random

private const val CHANNEL_ID = "My_channel"
class FirebaseService : FirebaseMessagingService(){
    lateinit var storage: Storage

    override fun onNewToken(nToken: String) {
        super.onNewToken(nToken)
        storage = Storage(this)
        storage.tokenId = nToken
    }
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)


        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificatioID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0,intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(p0.data.get("title"))
            .setContentText(p0.data.get("message"))
            .setSmallIcon(R.drawable.ic_baseline_email_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificatioID, notification)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My chat notification"
            enableLights(true)
            lightColor= Color.BLUE

        }
        notificationManager.createNotificationChannel(channel)
    }
}