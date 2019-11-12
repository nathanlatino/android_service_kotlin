package com.hearc.service_swiped.swiped

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.hearc.service_swiped.R
import kotlin.concurrent.thread

class SwipedService : Service() {



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

    fun createNotification() {
        val resultIntent = Intent(this, Result::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        thread {
            Log.d("Test", "notification swiped")
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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        Toast.makeText(this, "Service started!", Toast.LENGTH_SHORT).show()
//        createNotification()
        mediaPlayer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_SHORT).show()
        mediaPlayer.release()
    }
}
