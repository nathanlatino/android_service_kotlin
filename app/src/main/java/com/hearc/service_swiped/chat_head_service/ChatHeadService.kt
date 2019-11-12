package com.hearc.service_swiped.chat_head_service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.hearc.service_swiped.R
import kotlin.math.log
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class ChatHeadService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var chatHead: ImageView

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        chatHead = ImageView(this)
        chatHead.setImageResource(R.drawable.android_head)

        var params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP and Gravity.LEFT
        params.x = 0
        params.y = 100
        Log.v("ChatHead", chatHead.toString());

        chatHead.performClick()

        chatHead.setOnTouchListener(listenerSwipe(params))


        windowManager.addView(chatHead, params)


    }

    private fun listenerSwipe(params: WindowManager.LayoutParams): View.OnTouchListener {
        return object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0.toFloat()
            private var initialTouchY = 0.toFloat()

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        getValue(event)
                        return true
                    }
                    MotionEvent.ACTION_UP -> return true
                    MotionEvent.ACTION_MOVE -> {
                        calcMove(event)
                        return true
                    }
                    else -> return false
                }
            }

            private fun getValue(event: MotionEvent) {
                initialX = params.x
                initialY = params.y

                initialTouchX = event.getRawX()
                initialTouchY = event.getRawY()

            }

            private fun calcMove(event: MotionEvent) {
                params.x = initialX + (event.getRawX() - initialTouchX).toInt();
                params.y = initialY + (event.getRawY() - initialTouchY).toInt();
                windowManager.updateViewLayout(chatHead, params);
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(chatHead)
    }
}
