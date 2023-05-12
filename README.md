[![](https://jitpack.io/v/abdomi7/call-tracker.svg)](https://jitpack.io/#abdomi7/call-tracker)

# Call Tracker

Call Tracker is an Android library that provides an easy way to track phone calls on Android devices.

## Features
Call Tracker offers the ability to handle different call events, including:

* Incoming call received
* Incoming call ended
* Incoming call answered
* Missed call
* Outgoing call ended
* Outgoing call started

When any of these events occur, the corresponding method is called and contains information about the call such as the phone number, start time, and end time.

## Permissions required
```
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
```

## Usage

### Step 1: Add the library dependency
Add the following dependency to your app's build.gradle file:

```
dependencies {
    implementation 'com.github.abdomi7:call-tracker:1.0.3'
}
```

### Step 2: Extend PhoneCallReceiver
Extend the PhoneCallReceiver class and implement the methods provided for each call event you want to track.


```
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
```
### Step 2: Start Call Tracking
Create an instance of CallTracker, set the PhoneCallReceiver you created in step 1, and start call tracking.


```
    val callsTracker = CallTracker.getInstance()
    callsTracker.setPhoneCallReceiver(CallTrackingReceiver())
    callsTracker.startCallTrackingReceiver(this)
```
### Optionally, you can also use the built-in background service by calling:


```
    callsTracker.startCallTrackingService(this)
```
### Step 3: Stop Call Tracking Receiver
To stop the call tracking receiver, call the following method:



```
    callsTracker.stopCallTrackingReceiver(this)
```

This stops the phone call receiver and ensures that your app does not continue to consume system resources unnecessarily.

## Conclusion
Call Tracker is a handy tool for Android developers who need to track phone calls in their apps. By following these simple steps, you can add call-tracking functionality to your app quickly and easily.


