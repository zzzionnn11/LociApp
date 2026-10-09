package com.example.lociapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lociapp.R
import com.example.lociapp.models.ItemEntity
import com.example.lociapp.models.ReminderEntity

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "loci_items.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_ITEMS = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_REMINDER_TIME = "reminder_time"
        private const val COLUMN_REMINDER_DATE = "reminder_date"
        private const val COLUMN_IMAGE_URI = "image_uri"
        private const val COLUMN_IMAGE_RES = "image_res"
        private const val COLUMN_ADDED_TIME = "added_time"
        private const val COLUMN_IS_URGENT = "is_urgent"

        private const val TABLE_REMINDERS = "reminders"
        private const val R_ID = "id"
        private const val R_TITLE = "title"
        private const val R_DESCRIPTION = "description"
        private const val R_TIME = "time_text"
        private const val R_DATE = "date_text"
        private const val R_TRIGGER = "trigger_at"
        private const val R_ITEM_ID = "item_id"
        private const val R_FROM_ITEM = "from_item"

        @Volatile
        private var instance: DatabaseHelper? = null

        /** One shared helper for the whole app, so every repository uses the same connection. */
        fun getInstance(context: Context): DatabaseHelper =
            instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
    }

    private fun createRemindersTable(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_REMINDERS (
                $R_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $R_TITLE TEXT NOT NULL,
                $R_DESCRIPTION TEXT NOT NULL DEFAULT '',
                $R_TIME TEXT NOT NULL,
                $R_DATE TEXT NOT NULL DEFAULT '',
                $R_TRIGGER INTEGER NOT NULL,
                $R_ITEM_ID INTEGER NOT NULL DEFAULT 0,
                $R_FROM_ITEM INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_ITEMS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_REMINDER_TIME TEXT,
                $COLUMN_REMINDER_DATE TEXT,
                $COLUMN_IMAGE_URI TEXT,
                $COLUMN_IMAGE_RES INTEGER NOT NULL DEFAULT 0,
                $COLUMN_ADDED_TIME TEXT,
                $COLUMN_IS_URGENT INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
        createRemindersTable(db)

        // Pre-populate default mock items
        insertInitialItem(db, "Tech Book", "A closed book is resting on a round wooden table next to a sofa, with a green plant in the background.", "10:00 AM", "07/12/2026", R.drawable.tech_book, "July 11 at 5:00 PM", isUrgent = true)
        insertInitialItem(db, "Camera in bag", "Bring camera to the function. The camera at my bedroom, inside the camera bag.", "12:00 PM", "", R.drawable.camera, "July 10 at 2:00 PM", isUrgent = false)
        insertInitialItem(db, "Headphones", "Noise-canceling over-ear headphones sitting on the computer desk, ready for the afternoon meeting.", "1:00 PM", "", R.drawable.headphones, "July 11 at 9:00 AM", isUrgent = false)
        insertInitialItem(db, "Wallet", "Brown leather wallet located in the top drawer of the nightstand.", "8:00 AM", "07/12/2026", R.drawable.wallet, "July 09 at 6:30 PM", isUrgent = false)
        insertInitialItem(db, "Keys", "Set of house and car keys hanging on the wooden key rack by the front door.", "7:30 AM", "07/12/2026", R.drawable.keys, "July 09 at 4:45 PM", isUrgent = false)
        insertInitialItem(db, "Laptop", "Silver laptop closed and charging on the dining room table.", "9:00 AM", "07/12/2026", R.drawable.laptop, "July 09 at 11:30 PM", isUrgent = false)
    }

    private fun insertInitialItem(
        db: SQLiteDatabase,
        title: String,
        description: String,
        reminderTime: String,
        reminderDate: String,
        imageRes: Int,
        addedTime: String,
        isUrgent: Boolean
    ) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_REMINDER_TIME, reminderTime)
            put(COLUMN_REMINDER_DATE, reminderDate)
            put(COLUMN_IMAGE_URI, null as String?)
            put(COLUMN_IMAGE_RES, imageRes)
            put(COLUMN_ADDED_TIME, addedTime)
            put(COLUMN_IS_URGENT, if (isUrgent) 1 else 0)
        }
        db.insert(TABLE_ITEMS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Never drop the items table here: it holds the user's photos.
        if (oldVersion < 2) createRemindersTable(db)
    }

    fun insertItem(item: ItemEntity): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, item.title)
            put(COLUMN_DESCRIPTION, item.description)
            put(COLUMN_REMINDER_TIME, item.reminderTime)
            put(COLUMN_REMINDER_DATE, item.reminderDate)
            put(COLUMN_IMAGE_URI, item.imageUri)
            put(COLUMN_IMAGE_RES, item.imageRes)
            put(COLUMN_ADDED_TIME, item.addedTime)
            put(COLUMN_IS_URGENT, if (item.isUrgent) 1 else 0)
        }
        return db.insert(TABLE_ITEMS, null, values)
    }

    fun getAllItems(): List<ItemEntity> {
        val list = mutableListOf<ItemEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ITEMS ORDER BY $COLUMN_ID DESC", null)
        cursor.use { c ->
            val idCol = c.getColumnIndexOrThrow(COLUMN_ID)
            val titleCol = c.getColumnIndexOrThrow(COLUMN_TITLE)
            val descCol = c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
            val rTimeCol = c.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)
            val rDateCol = c.getColumnIndexOrThrow(COLUMN_REMINDER_DATE)
            val imgUriCol = c.getColumnIndexOrThrow(COLUMN_IMAGE_URI)
            val imgResCol = c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)
            val addedCol = c.getColumnIndexOrThrow(COLUMN_ADDED_TIME)
            val urgentCol = c.getColumnIndexOrThrow(COLUMN_IS_URGENT)

            while (c.moveToNext()) {
                list.add(
                    ItemEntity(
                        id = c.getInt(idCol),
                        title = c.getString(titleCol),
                        description = c.getString(descCol),
                        reminderTime = c.getString(rTimeCol) ?: "",
                        reminderDate = c.getString(rDateCol) ?: "",
                        imageUri = c.getString(imgUriCol),
                        imageRes = c.getInt(imgResCol),
                        addedTime = c.getString(addedCol) ?: "",
                        isUrgent = c.getInt(urgentCol) == 1
                    )
                )
            }
        }
        return list
    }

    fun getItemById(id: Int): ItemEntity? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ITEMS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        cursor.use { c ->
            if (c.moveToFirst()) {
                return ItemEntity(
                    id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)),
                    title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    reminderTime = c.getString(c.getColumnIndexOrThrow(COLUMN_REMINDER_TIME)) ?: "",
                    reminderDate = c.getString(c.getColumnIndexOrThrow(COLUMN_REMINDER_DATE)) ?: "",
                    imageUri = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URI)),
                    imageRes = c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                    addedTime = c.getString(c.getColumnIndexOrThrow(COLUMN_ADDED_TIME)) ?: "",
                    isUrgent = c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_URGENT)) == 1
                )
            }
        }
        return null
    }

    fun updateItem(item: ItemEntity): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, item.title)
            put(COLUMN_DESCRIPTION, item.description)
            put(COLUMN_REMINDER_TIME, item.reminderTime)
            put(COLUMN_REMINDER_DATE, item.reminderDate)
            put(COLUMN_IMAGE_URI, item.imageUri)
            put(COLUMN_IMAGE_RES, item.imageRes)
            put(COLUMN_ADDED_TIME, item.addedTime)
            put(COLUMN_IS_URGENT, if (item.isUrgent) 1 else 0)
        }
        return db.update(TABLE_ITEMS, values, "$COLUMN_ID = ?", arrayOf(item.id.toString()))
    }

    fun deleteItem(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_ITEMS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteAllItems(): Int = writableDatabase.delete(TABLE_ITEMS, null, null)

    // ---------------- Reminders ----------------

    private fun reminderValues(r: ReminderEntity) = ContentValues().apply {
        put(R_TITLE, r.title)
        put(R_DESCRIPTION, r.description)
        put(R_TIME, r.time)
        put(R_DATE, r.date)
        put(R_TRIGGER, r.triggerAtMillis)
        put(R_ITEM_ID, r.itemId)
        put(R_FROM_ITEM, if (r.fromItem) 1 else 0)
    }

    fun insertReminder(reminder: ReminderEntity): Long =
        writableDatabase.insert(TABLE_REMINDERS, null, reminderValues(reminder))

    fun updateReminder(reminder: ReminderEntity): Int =
        writableDatabase.update(TABLE_REMINDERS, reminderValues(reminder), "$R_ID = ?", arrayOf(reminder.id.toString()))

    fun getAllReminders(): List<ReminderEntity> {
        val list = mutableListOf<ReminderEntity>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_REMINDERS ORDER BY $R_TRIGGER ASC", null).use { c ->
            val idCol = c.getColumnIndexOrThrow(R_ID)
            val titleCol = c.getColumnIndexOrThrow(R_TITLE)
            val descCol = c.getColumnIndexOrThrow(R_DESCRIPTION)
            val timeCol = c.getColumnIndexOrThrow(R_TIME)
            val dateCol = c.getColumnIndexOrThrow(R_DATE)
            val triggerCol = c.getColumnIndexOrThrow(R_TRIGGER)
            val itemCol = c.getColumnIndexOrThrow(R_ITEM_ID)
            val fromItemCol = c.getColumnIndexOrThrow(R_FROM_ITEM)
            while (c.moveToNext()) {
                list.add(
                    ReminderEntity(
                        id = c.getInt(idCol),
                        title = c.getString(titleCol),
                        description = c.getString(descCol) ?: "",
                        time = c.getString(timeCol),
                        date = c.getString(dateCol) ?: "",
                        triggerAtMillis = c.getLong(triggerCol),
                        itemId = c.getInt(itemCol),
                        fromItem = c.getInt(fromItemCol) == 1
                    )
                )
            }
        }
        return list
    }

    fun deleteReminder(id: Int): Int =
        writableDatabase.delete(TABLE_REMINDERS, "$R_ID = ?", arrayOf(id.toString()))

    fun deleteAllReminders(): Int = writableDatabase.delete(TABLE_REMINDERS, null, null)
}
