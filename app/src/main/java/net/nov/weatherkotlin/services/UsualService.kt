package net.nov.weatherkotlin.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class UsualService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //.. наш код сервиса. Здесь поток UI!
        Thread {
            ///...
            stopSelf()
        }.start()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun start(context: Context) {
            val usualServiceIntent = Intent(context, UsualService::class.java)
            context?.startService(usualServiceIntent)
        }

        fun stop(context: Context) {
            val usualServiceIntent = Intent(context, UsualService::class.java)
            context?.stopService(usualServiceIntent)
        }
    }
}