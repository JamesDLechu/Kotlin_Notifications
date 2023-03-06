package com.example.android.eggtimernotifications.receiver

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.SystemClock
import android.text.format.DateUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.util.UPDATABLE_NOTIFICATION_ID
import com.example.android.eggtimernotifications.util.sendUpdatingNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdatingWorker(context: Context, params: WorkerParameters):
    CoroutineWorker(context, params) {

    private val workerDataInput= "WorkerDataInput"

    override suspend fun doWork(): Result {
        val notificationManager= ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        return try {
            val receivedTime= inputData.getLong(workerDataInput, 0L)
            var tickingTime= receivedTime / 1000

            while (tickingTime > 0){
                tickingTime= (receivedTime - SystemClock.elapsedRealtime()) / 1000

                notificationManager.sendUpdatingNotification(
                    applicationContext.getString(R.string.remaining_time, remainingTime( tickingTime )),
                    applicationContext
                )
                sleep()
            }

            Result.success()
        }catch (e: Throwable){
            notificationManager.cancel(UPDATABLE_NOTIFICATION_ID)
            Result.failure()
        }
    }

    private suspend fun sleep() {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }
    }

    private fun remainingTime(value: Long): String =
         DateUtils.formatElapsedTime(value).toString()
}