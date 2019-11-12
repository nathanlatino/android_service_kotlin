package com.hearc.service_swiped.chat_head_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import com.hearc.service_swiped.R
import android.content.Context


class WindowManager : AppCompatActivity() {

    lateinit var btn_manager : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_manager)

        btn_manager = findViewById(R.id.btn_launch) as Button

        btn_manager.setOnClickListener{startService(Intent(this, ChatHeadService::class.java))}
    }
}
