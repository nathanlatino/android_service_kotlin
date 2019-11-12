package com.hearc.service_swiped.swiped

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hearc.service_swiped.R


class MainActivity : AppCompatActivity() {

    lateinit var b_play: Button
    lateinit var b_stop: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        b_play = findViewById(R.id.b_play) as Button
        b_stop = findViewById(R.id.b_stop) as Button

        b_play.setOnClickListener {
            //            startService(Intent(this, SwipedService::class.java))
            ForegroundService.startService(this, "Foreground Service is running...")

        }

        b_stop.setOnClickListener({
            //            stopService(Intent(this, SwipedService::class.java))
            ForegroundService.stopService(this)
        })
    }
}
