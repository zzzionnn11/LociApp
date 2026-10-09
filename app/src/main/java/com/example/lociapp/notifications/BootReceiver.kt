package com.example.lociapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lociapp.db.DatabaseHelper

/** Alarms are wiped on reboot / app update, so put every future reminder back. */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) return

        val appContext = context.applicationContext
        val pending = goAsync()
        Thread {
            try {
                val now = System.currentTimeMillis()
                DatabaseHelper.getInstance(appContext).getAllReminders()
                    .filter { it.triggerAtMillis > now }
                    .forEach { ReminderScheduler.schedule(appContext, it) }
            } finally {
                pending.finish()
            }
        }.start()
    }
}
