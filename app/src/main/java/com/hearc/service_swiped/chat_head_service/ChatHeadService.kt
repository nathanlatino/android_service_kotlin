package com.hearc.service_swiped.chat_head_service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlin.math.absoluteValue
import kotlin.math.pow


class ChatHeadService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var chatHead: View

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        chatHead = View(this)
//        chatHead.setImageResource(R.drawable.android_head)

        var params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER and Gravity.CENTER
        params.x = 0
        params.y = 0

        chatHead.setOnTouchListener(listenerSwipe(params))


        windowManager.addView(chatHead, params)

    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(chatHead)
    }


    //Listener
    private fun listenerSwipe(params: WindowManager.LayoutParams): View.OnTouchListener {
        return object : View.OnTouchListener {
            private var initialX = 0f
            private var initialY = 0f
            private var dist = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        getPos(event)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        calcMove(event)
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> return true
                    else -> return false
                }
            }

            private fun getPos(event : MotionEvent) {
                initialX = event.x
                initialY = event.y
            }

            private fun calcMove(event: MotionEvent) {
                dist = ((initialX - event.x) + (initialY - event.y)).absoluteValue
                Toast.makeText(this@ChatHeadService, "distance : $dist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
