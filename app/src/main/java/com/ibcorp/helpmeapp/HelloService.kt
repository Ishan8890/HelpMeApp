package com.ibcorp.helpmeapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*


class HelloService : Service() {

    private var serviceLooper: Looper? = null
    var counter = 0
//    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
   /* private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }*/

    override fun onCreate() {
        Log.d("TAG", "Service onCreate..")

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(
                1,
                Notification()
            )
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
//        HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND).apply {
//            start()
//
//            // Get the HandlerThread's Looper and use it for our Handler
//            serviceLooper = looper
//            serviceHandler = ServiceHandler(looper)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.cancelAll()
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)


    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId);
//        startTimer()
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
//        serviceHandler?.obtainMessage()?.also { msg ->
//            msg.arg1 = startId
//            serviceHandler?.sendMessage(msg)
//        }
        Log.d("TAG", "Service started.." + startId)

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

//    override fun onTaskRemoved(rootIntent: Intent?) {
//        super.onTaskRemoved(rootIntent)
//        Log.d("TAG", "onTaskRemoved..")
//        stopSelf()
//    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy..")
        val broadcastIntent = Intent()
        broadcastIntent.action = "RestartService"
        broadcastIntent.setClass(this, RestartService::class.java)
        this.sendBroadcast(broadcastIntent)
    }


    fun startTimer(){
        var timer = Timer()
        var timerTask = object : TimerTask() {
            override fun run() {
                Log.i("TAG Count", "=========  " + counter++)
            }
        }
        timer.schedule(timerTask, 1000, 1000)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}