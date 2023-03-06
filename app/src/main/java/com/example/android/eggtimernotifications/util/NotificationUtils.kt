/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.CancelUpdatesReceiver
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver
import javax.inject.Singleton

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

internal val UPDATABLE_NOTIFICATION_ID = 1
private val REQUEST_CODE_CANCEL = 1

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    val intent= Intent(applicationContext, MainActivity::class.java)

    val pendingIntent= PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val eggImage= BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )

    val bigPicStyle= NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    val snoozeIntent= Intent(
        applicationContext,
        SnoozeReceiver::class.java
    )

    val snoozePendingIntent= PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        PendingIntent.FLAG_ONE_SHOT
    )

    // Build the notification
    val builder= NotificationCompat.Builder(applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id))

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    builder.apply {
        setContentTitle(applicationContext.getString(R.string.notification_title))
        setContentText(messageBody)
        setSmallIcon(R.drawable.cooked_egg)
    }

    builder.setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )
        .priority= NotificationCompat.PRIORITY_HIGH

    notify(NOTIFICATION_ID, builder.build())

}

/**
 * Builds and delivers the updatable notification.
 *
 * @param messageBody, text to be displayed.
 * @param applicationContext, activity context.
 */

fun NotificationManager.sendUpdatingNotification(messageBody: String, applicationContext: Context) {

    // Build the notification
    val builder= NotificationCompat.Builder(applicationContext,
        applicationContext.getString(R.string.breakfast_notification_channel_id))

    builder.apply {
        setContentTitle(applicationContext.getString(R.string.notification_title))
        //setProgress(100, messageBody,false)
        setSmallIcon(R.drawable.cooked_egg)
        setAutoCancel(true)
        setSilent(true)
        setContentText(messageBody)
        //setSubText("$messageBody / 100")
        priority= NotificationCompat.PRIORITY_DEFAULT
    }

    val cancelIntent= Intent(applicationContext, CancelUpdatesReceiver::class.java)
    val cancelPendingIntent= PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE_CANCEL,
        cancelIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    builder.addAction(
        0,
        "Cancel",
        cancelPendingIntent
    )

    notify(UPDATABLE_NOTIFICATION_ID, builder.build())
}

/**
 *  Cancels all notifications
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}