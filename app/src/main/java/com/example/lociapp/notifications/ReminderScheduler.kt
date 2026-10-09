package com.example.lociapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.lociapp.models.ReminderEntity

/** Schedules / cancels the alarm that shows a reminder's notification. */
object ReminderScheduler {
    const val ACTION_REMINDER = "com.example.lociapp.ACTION_REMINDER"
    const val EXTRA_ID = "reminder_id"
    const val EXTRA_TITLE = "reminder_title"
    const val EXTRA_TEXT = "reminder_text"

    /** On Android 12+ the user can switch off "Alarms & reminders" for the app. */
    fun canScheduleExact(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()

    private fun pendingIntent(
        context: Context,
        id: Int,
        title: String?,
        text: String?,
        create: Boolean
    ): PendingIntent? {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra(EXTRA_ID, id)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_TEXT, text)
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or
                if (create) PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_NO_CREATE
        return PendingIntent.getBroadcast(context, id, intent, flags)
    }

    fun schedule(context: Context, reminder: ReminderEntity) {
        if (reminder.id == 0 || reminder.triggerAtMillis <= System.currentTimeMillis()) return

        val text = reminder.description.ifBlank { "It's time! You need this at ${reminder.time}." }
        val pi = pendingIntent(context, reminder.id, reminder.title, text, create = true) ?: return
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        try {
            if (canScheduleExact(context)) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.triggerAtMillis, pi)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.triggerAtMillis, pi)
            }
        } catch (e: SecurityException) {
            // Exact alarms got revoked between the check and the call: fall back to a normal alarm.
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.triggerAtMillis, pi)
        }
    }

    fun cancel(context: Context, reminderId: Int) {
        val pi = pendingIntent(context, reminderId, null, null, create = false) ?: return
        context.getSystemService(AlarmManager::class.java).cancel(pi)
        pi.cancel()
    }
}
