package com.ibcorp.helpmeapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build




class RestartService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
//        context!!.startService(Intent(context, HelloService::class.java))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, HelloService::class.java))
        } else {
            context!!.startService(Intent(context, HelloService::class.java))
        }
    }
}