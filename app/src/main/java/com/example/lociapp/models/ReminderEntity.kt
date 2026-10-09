package com.example.lociapp.models

/**
 * One saved reminder (a row in the "reminders" table).
 *
 * @property time  text shown to the user, e.g. "10:00 AM"
 * @property date  text shown to the user, e.g. "07/12/2026"
 * @property triggerAtMillis the exact moment the notification fires
 * @property itemId the item this reminder belongs to (0 = none)
 * @property fromItem true when the reminder was created from an item's own
 *           reminder time/date (Edit Photo / Edit Item). Those are kept in
 *           sync automatically when the item changes.
 */
data class ReminderEntity(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val time: String,
    val date: String,
    val triggerAtMillis: Long,
    val itemId: Int = 0,
    val fromItem: Boolean = false
) {
    /** "Time sensitive" = due within the next 24 hours. */
    fun isUrgent(now: Long = System.currentTimeMillis()): Boolean =
        triggerAtMillis in now..(now + DAY_MILLIS)

    fun isPast(now: Long = System.currentTimeMillis()): Boolean = triggerAtMillis < now

    private companion object {
        const val DAY_MILLIS = 24L * 60 * 60 * 1000
    }
}
