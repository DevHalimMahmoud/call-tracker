package com.abdelhalim.demo

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdelhalim.R


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // you need to request this permissions in runtime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.PROCESS_OUTGOING_CALLS
                ), 0
            )
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.PROCESS_OUTGOING_CALLS), 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //this will run the tracking service in the foreground you can also use the built in background service
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != 0 || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != 0 || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != 0) {
            return
        }
        try {
            val startIntent = Intent(this, CallsService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(startIntent)
            } else {
                startService(startIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

