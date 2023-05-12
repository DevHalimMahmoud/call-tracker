package com.abdelhalim.calltracker.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.CallLog
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import java.util.Date


open class PhoneCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        var number = ""
        var state = 0

        when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
            null -> {
                //Outgoing call
                number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).toString()
                savedNumber = number
            }

            TelephonyManager.EXTRA_STATE_RINGING -> {
                //Incoming call, it is possible that the TelephonyManager.EXTRA_INCOMING_NUMBER extra may sometimes be null or unavailable due to device-specific limitations
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
                if (!number.isDigitsOnly()) {
                    // If the incoming number is null, we need to check the CallLog
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            val cursor = context.contentResolver.query(
                                CallLog.Calls.CONTENT_URI,
                                arrayOf(CallLog.Calls.NUMBER),
                                "${CallLog.Calls.TYPE}=${CallLog.Calls.INCOMING_TYPE}",
                                null,
                                "${CallLog.Calls.DATE} DESC LIMIT 1"
                            )
                            cursor?.use {
                                if (it.moveToFirst()) {
                                    Log.e("READ_CALL_LOG", "READ_CALL_LOG permission granted")
                                    number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("READ_CALL_LOG", "READ_CALL_LOG permission not granted")
                        }
                    } else {
                        Log.e("READ_CALL_LOG", "READ_CALL_LOG permission not granted")
                        // Request the READ_CALL_LOG permission from the user
                        // before retrieving the incoming call number
                    }
                }
                state = TelephonyManager.CALL_STATE_RINGING
            }

            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                //Incoming call
                number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).toString()
                state = TelephonyManager.CALL_STATE_OFFHOOK

            }

            TelephonyManager.EXTRA_STATE_IDLE -> {
                state = TelephonyManager.CALL_STATE_IDLE
            }
        }

        onCallStateChanged(context, state, number)
    }

    //Derived classes should override these to respond to specific events of interest
    open fun onIncomingCallReceived(context: Context, number: String?, start: Date) {}

    open fun onIncomingCallAnswered(context: Context, number: String?, start: Date) {}

    open fun onIncomingCallEnded(
        context: Context, number: String?, start: Date, end: Date
    ) {}

    open fun onOutgoingCallStarted(context: Context, number: String?, start: Date) {}

    open fun onOutgoingCallEnded(
        context: Context, number: String?, start: Date, end: Date
    ) {}

    open fun onMissedCall(context: Context, number: String?, start: Date) {}


    private fun onCallStateChanged(context: Context, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                onIncomingCallReceived(context, number, callStartTime)
            }

            TelephonyManager.CALL_STATE_OFFHOOK ->
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()

                    onOutgoingCallStarted(context, savedNumber, callStartTime)
                } else {
                    isIncoming = true
                    callStartTime = Date()

                    onIncomingCallAnswered(context, savedNumber, callStartTime)
                }

            TelephonyManager.CALL_STATE_IDLE ->
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    onMissedCall(context, savedNumber, callStartTime)
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }

    companion object {
        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date = Date()
        private var isIncoming: Boolean = false
        private var savedNumber: String? = null  //because the passed incoming is only valid in ringing
    }
}