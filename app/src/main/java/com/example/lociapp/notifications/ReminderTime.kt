package com.example.lociapp.notifications

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Turns the app's "10:00 AM" + "07/12/2026" strings into real timestamps (and back). */
object ReminderTime {

    fun formatTime(hour24: Int, minute: Int): String {
        val h12 = if (hour24 % 12 == 0) 12 else hour24 % 12
        return "%d:%02d %s".format(Locale.US, h12, minute, if (hour24 < 12) "AM" else "PM")
    }

    /** [month0] is 0-based, like Calendar.MONTH and the DatePickerDialog callback. */
    fun formatDate(year: Int, month0: Int, day: Int): String =
        "%02d/%02d/%d".format(Locale.US, month0 + 1, day, year)

    /**
     * Returns the trigger time in millis, or null if [timeText] is empty/invalid.
     * If [dateText] is empty the reminder means "the next time it is [timeText]".
     */
    fun parse(timeText: String, dateText: String, now: Long = System.currentTimeMillis()): Long? {
        if (timeText.isBlank()) return null
        return try {
            if (dateText.isBlank()) {
                val parsed = SimpleDateFormat("h:mm a", Locale.US).apply { isLenient = false }
                    .parse(timeText.trim()) ?: return null
                val clock = Calendar.getInstance().apply { setTime(parsed) }
                val target = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, clock.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, clock.get(Calendar.MINUTE))
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                if (target.timeInMillis <= now) target.add(Calendar.DAY_OF_YEAR, 1)
                target.timeInMillis
            } else {
                SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US).apply { isLenient = false }
                    .parse("${dateText.trim()} ${timeText.trim()}")?.time
            }
        } catch (e: ParseException) {
            null
        }
    }
}
