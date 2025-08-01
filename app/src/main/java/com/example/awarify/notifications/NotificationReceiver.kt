package com.example.awarify.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getLongExtra("notification_id", -1)
        val title = intent.getStringExtra("title") ?: ""
        val message = intent.getStringExtra("message") ?: ""
        val hobbyId = intent.getLongExtra("hobby_id", -1)
        
        if (notificationId != -1L) {
            NotificationHelper.showHobbyNotification(
                context = context,
                notificationId = notificationId.toInt(),
                title = title,
                message = message,
                hobbyId = hobbyId
            )
        }
    }
} 