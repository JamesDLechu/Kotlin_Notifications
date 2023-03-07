package com.example.android.eggtimernotifications.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.example.android.eggtimernotifications.ui.Unique_Work_Timer
import com.example.android.eggtimernotifications.util.UPDATABLE_NOTIFICATION_ID
import com.example.android.eggtimernotifications.util.cancelNotifications

class CancelUpdatesReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //Specified function to execute
        val workManager= WorkManager.getInstance(context)
        workManager.cancelUniqueWork(Unique_Work_Timer)

        val notificationManager= ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()
    }
}