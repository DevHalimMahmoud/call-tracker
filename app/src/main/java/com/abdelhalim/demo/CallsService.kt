package com.abdelhalim.demo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.abdelhalim.R
import com.abdelhalim.calltracker.CallTracker
import com.abdelhalim.calltracker.CallTracker.Companion.getInstance

class CallsService : Service() {
    private var record: CallTracker? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannels()
        record = getInstance()
        record!!.setPhoneCallReceiver(CallTrackingReceiver())
        record!!.startCallTrackingReceiver(this)
        val intent1 = Intent(applicationContext, MainActivity::class.java)
        var pendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivities(applicationContext, 0, arrayOf(intent1), PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setContentText("Service is running")
            .setContentTitle("Service enabled")
            .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
        startForeground(100, notification.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        record!!.stopCallTrackingReceiver(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Channel 1"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
        }
    }

    companion object {
        const val CHANNEL_1_ID = "channel1"
    }
}