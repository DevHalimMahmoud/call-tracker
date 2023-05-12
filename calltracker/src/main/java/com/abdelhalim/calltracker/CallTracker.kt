package com.abdelhalim.calltracker

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.abdelhalim.calltracker.receiver.PhoneCallReceiver
import com.abdelhalim.calltracker.service.BackgroundCallTrackingService

class CallTracker private constructor() {
    private val TAG = CallTracker::class.java.simpleName
    private var mCallTrackingReceiver: PhoneCallReceiver = PhoneCallReceiver()

    private val ACTION_IN = "android.intent.action.PHONE_STATE"
    private val ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL"

    fun startCallTrackingReceiver(mContext: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_IN)
        intentFilter.addAction(ACTION_OUT)

        mContext.registerReceiver(mCallTrackingReceiver, intentFilter)
    }

    fun stopCallTrackingReceiver(mContext: Context) {
        try {
            mContext.unregisterReceiver(mCallTrackingReceiver)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    fun startCallTrackingService(mContext: Context) {
        val intent = Intent()
        intent.setClass(mContext, BackgroundCallTrackingService::class.java)

        mContext.startService(intent)
    }

    fun setPhoneCallReceiver(phoneCallReceiver: PhoneCallReceiver) {
        mCallTrackingReceiver = phoneCallReceiver
    }

    companion object {
        private lateinit var mInstance: CallTracker

        fun getInstance(): CallTracker {
            if (::mInstance.isInitialized.not()) {
                mInstance = CallTracker()
            }
            return mInstance
        }
    }
}
