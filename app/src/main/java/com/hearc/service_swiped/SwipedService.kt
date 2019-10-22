package com.hearc.service_swiped

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class SwipedService() : Service() {



    lateinit var mediaPlayer: MediaPlayer
    private var notificationManager: NotificationManager? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer.create(this, R.raw.dejavu)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.M)
    fun createNotification() {
        val resultIntent = Intent(this, Result::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        thread {
            Log.d("VERGE", "Coucou")
//            Thread.sleep(10000)
            val icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
            val action: Notification.Action =
                Notification.Action.Builder(icon, "Open", pendingIntent).build()

            val notification = Notification.Builder(this, "SwipedService")
                .setContentTitle("Poule")
                .setContentText("J'aime le poulet")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setActions(action)
                .setAutoCancel(true)
                .build()

            notificationManager?.notify(101, notification)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel("swipedChannel", "Swiped Chanel", importance).apply {
//                description = "this is a notification"
//            }
//
//            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "Service started!", Toast.LENGTH_SHORT).show()
        createNotification()
        mediaPlayer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_SHORT).show()
        mediaPlayer.release()
    }
}