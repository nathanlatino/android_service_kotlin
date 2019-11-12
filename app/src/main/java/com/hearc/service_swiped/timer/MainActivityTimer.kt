package com.hearc.service_swiped.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ComponentName
import android.os.IBinder
import android.content.ServiceConnection
import android.content.Intent
import android.widget.TextView
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import com.hearc.service_swiped.R
import java.lang.ref.WeakReference




class MainActivityTimer : AppCompatActivity() {



    private lateinit var timerService: TimerService
    private var serviceBound = false

    private lateinit var timerButton: Button
    private lateinit var timerTextView: TextView

    // Handler to update the UI every second when the timer is running
    private val mUpdateTimeHandler = UIUpdateHandler(this)

    // Message type for the handler
    companion object{
        private val TAG = TimerService::class.java.simpleName
        private val MSG_UPDATE_TIME = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_timer)
        timerButton = findViewById(R.id.timer_button) as Button
        timerTextView = findViewById(R.id.timer_text_view) as TextView
    }

    override fun onStart() {
        super.onStart()
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service")
        }
        val i = Intent(this, TimerService::class.java)
        startService(i)
        bindService(i, mConnection, 0)
        Log.v("TEST", "start")
    }

    override fun onStop() {
        super.onStop()
        updateUIStopRun()
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning) {
                timerService.foreground()
            } else {
                stopService(Intent(this, TimerService::class.java))
            }
            // Unbind the service
            unbindService(mConnection)
            serviceBound = false
        }
    }

    fun runButtonClick(v: View) {
        if (serviceBound && !timerService.isTimerRunning) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Starting timer")
            }
            timerService.startTimer()
            updateUIStartRun()
        } else if (serviceBound && timerService.isTimerRunning) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Stopping timer")
            }
            timerService.stopTimer()
            updateUIStopRun()
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private fun updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
        timerButton.setText("stop")
    }

    /**
     * Updates the UI when a run stops
     */
    private fun updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME)
        timerButton.setText("start")
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */
    private fun updateUITimer() {
        if (serviceBound) {
            timerTextView.text = timerService.elapsedTime().toString() + " seconds"
        }
    }

    /**
     * Callback for service binding, passed to bindService()
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound")
            }
            val binder = service as TimerService.RunServiceBinder
            timerService = binder.service
            serviceBound = true
            // Ensure the service is not in the foreground when bound
            timerService.background()
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning) {
                updateUIStartRun()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service disconnect")
            }
            serviceBound = false
        }
    }

    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    internal class UIUpdateHandler(activity: MainActivityTimer) : Handler() {

        private val UPDATE_RATE_MS : Long = 1000
        private val activity: WeakReference<MainActivityTimer>


        init {
            this.activity = WeakReference(activity)
        }

        override fun handleMessage(message: Message) {
            if (MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "updating time")
                }
                activity.get()?.updateUITimer()
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS)
            }
        }
    }
}
