package com.hearc.service_swiped.chat_head_service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.gesture.GestureOverlayView
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.WindowManager
import android.widget.Toast
import kotlin.math.absoluteValue


class ChatHeadService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: GestureOverlayView

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlayView = GestureOverlayView(this)
//        overlayView.setImageResource(R.drawable.android_head)

        var params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )


        params.gravity = Gravity.TOP
        params.x = 0
        params.y = 0
        params.token = overlayView.windowToken

        overlayView.setOnTouchListener(listenerSwipe())


        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
    }


    //Listener
    private fun listenerSwipe(): View.OnTouchListener {
        return object : View.OnTouchListener {
            private var initialX = 0f
            private var initialY = 0f
            private var dist = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                MotionEvent.ACTION_OUTSIDE

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

            private fun getPos(event: MotionEvent) {
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
