package com.example.lociapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Called by AlarmManager when a reminder is due. */
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(ReminderScheduler.EXTRA_ID, 0)
        val title = intent.getStringExtra(ReminderScheduler.EXTRA_TITLE) ?: "Loci reminder"
        val text = intent.getStringExtra(ReminderScheduler.EXTRA_TEXT) ?: "You have a reminder."
        NotificationHelper.show(context.applicationContext, id, title, text)
    }
}
