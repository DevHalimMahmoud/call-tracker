package com.abdelhalim.calltracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.abdelhalim.calltracker.CallTracker

open class BackgroundCallTrackingService : Service() {

    private lateinit var mCallTracker: CallTracker

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


        mCallTracker = CallTracker.getInstance()

        mCallTracker.startCallTrackingReceiver(this)

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallTracker.stopCallTrackingReceiver(this)
    }
}
