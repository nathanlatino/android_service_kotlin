package com.hearc.service_swiped.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.os.Binder
import android.app.PendingIntent
import android.app.Notification
import androidx.core.app.NotificationCompat
import com.hearc.service_swiped.R


class TimerService : Service() {

    private val TAG: String? = TimerService::class.java.simpleName
    private val CHANNEL_ID = "ForegroundService Kotlin"
    private val NOTIFICATION_ID = 1;
    private var startTime: Long = 0
    private var endTime: Long = 0
    var isTimerRunning = false

    private val serviceBinder = RunServiceBinder()

    inner class RunServiceBinder : Binder() {
        internal val service: TimerService
            get() = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Binding service");
        }
        return serviceBinder;
    }


    override fun onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Creating service");
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting service");
        }
        return START_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Destroying service");
        }
    }

    fun startTimer() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis();
            isTimerRunning = true;
        } else {
            Log.e(TAG, "startTimer request for an already running timer");
        }
    }

    fun stopTimer() {
        if (isTimerRunning) {
            endTime = System.currentTimeMillis()
            isTimerRunning = false
        } else {
            Log.e(TAG, "stopTimer request for a timer that isn't running")
        }
    }

    fun elapsedTime(): Long {
        // If the timer is running, the end time will be zero
        return if (endTime > startTime)
            (endTime - startTime) / 1000
        else
            (System.currentTimeMillis() - startTime) / 1000
    }

    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun background() {
        stopForeground(true)
    }

    private fun createNotification(): Notification {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer Active")
                .setContentText("Tap to return to the timer")
                .setSmallIcon(R.drawable.ic_launcher_background)

            val resultIntent = Intent(this, MainActivityTimer::class.java)
            val resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(resultPendingIntent)

            return builder.build()
        }
}
