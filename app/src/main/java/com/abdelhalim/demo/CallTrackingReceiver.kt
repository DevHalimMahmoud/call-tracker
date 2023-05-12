package com.abdelhalim.demo

import android.content.Context
import android.content.Intent
import android.util.Log
import com.abdelhalim.calltracker.receiver.PhoneCallReceiver
import java.util.Date

class CallTrackingReceiver : PhoneCallReceiver() {


    override fun onIncomingCallReceived(context: Context, number: String?, start: Date) {
        super.onIncomingCallReceived(context, number, start)

        Log.d("CallRecordReceiver", "onIncomingCallReceived: $number started at $start")
    }


    override fun onIncomingCallEnded(context: Context, number: String?, start: Date, end: Date) {
        super.onIncomingCallEnded(context, number, start, end)

        Log.d("CallRecordReceiver", "onIncomingCallEnded: $number started at $start and ended at $end")
    }

    override fun onIncomingCallAnswered(context: Context, number: String?, start: Date) {
        super.onIncomingCallAnswered(context, number, start)

        Log.d("CallRecordReceiver", "onIncomingCallAnswered: $number started at $start")
    }

    override fun onMissedCall(context: Context, number: String?, start: Date) {
        super.onMissedCall(context, number, start)

        Log.d("CallRecordReceiver", "onMissedCall: $number started at $start")
    }

    override fun onOutgoingCallEnded(context: Context, number: String?, start: Date, end: Date) {
        super.onOutgoingCallEnded(context, number, start, end)

        Log.d("CallRecordReceiver", "onOutgoingCallEnded: $number started at $start and ended at $end")
    }

    override fun onOutgoingCallStarted(context: Context, number: String?, start: Date) {
        super.onOutgoingCallStarted(context, number, start)

        Log.d("CallRecordReceiver", "onOutgoingCallStarted: $number started at $start")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

    }

}
