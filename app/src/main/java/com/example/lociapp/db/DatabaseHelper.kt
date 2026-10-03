package com.example.lociapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lociapp.R
import com.example.lociapp.models.ItemEntity

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "loci_items.db"
        private const val DATABASE_VERSION = 1

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
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        onCreate(db)
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
}
