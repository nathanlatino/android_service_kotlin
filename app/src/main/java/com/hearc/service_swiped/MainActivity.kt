package com.hearc.service_swiped

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T






class MainActivity : AppCompatActivity() {

    lateinit var b_play : Button
    lateinit var b_stop : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        b_play = findViewById(R.id.b_play) as Button
        b_stop = findViewById(R.id.b_stop) as Button

        b_play.setOnClickListener{
            startService(Intent(this, SwipedService::class.java))
            createNotificationChannel()
        }

        b_stop.setOnClickListener({
            stopService(Intent(this, SwipedService::class.java))
        })
    }

    fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("swipedChannel", "Swiped Chanel", importance)

                channel.enableLights(true)
                channel.enableVibration(true)
                channel.lightColor = Color.GREEN
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

                val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val builder = NotificationCompat.Builder(this, "swipedChannel")
                builder.setContentTitle("This is heading")
                builder.setContentText("This is description")
                val notification = builder.build()
            }
        }
}
