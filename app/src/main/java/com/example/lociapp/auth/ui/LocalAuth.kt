package com.example.lociapp.auth.ui

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** One row of the users table. */
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)

/**
 * Local SQLite database stored on the phone (file: loci.db).
 * Passwords are never saved as plain text: we save a random "salt"
 * and a "hash" of salt + password.
 */
class UserDatabase(context: Context) : SQLiteOpenHelper(context, "loci.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                salt TEXT NOT NULL,
                password_hash TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Later (Lists part): add new tables here instead of deleting the users table.
    }

    /** True if an account with this email already exists. */
    fun emailExists(email: String): Boolean {
        return readableDatabase
            .rawQuery("SELECT 1 FROM users WHERE email = ?", arrayOf(clean(email)))
            .use { it.moveToFirst() }
    }

    /** Creates an account. Returns false if the email is already registered. */
    fun addUser(firstName: String, lastName: String, email: String, password: String): Boolean {
        if (emailExists(email)) return false

        val salt = newSalt()
        val values = ContentValues().apply {
            put("first_name", firstName.trim())
            put("last_name", lastName.trim())
            put("email", clean(email))
            put("salt", salt)
            put("password_hash", hash(password, salt))
        }
        return writableDatabase.insert("users", null, values) != -1L
    }

    /** Returns the User if the email + password are correct, otherwise null. */
    fun login(email: String, password: String): User? {
        return readableDatabase.rawQuery(
            "SELECT id, first_name, last_name, email, salt, password_hash FROM users WHERE email = ?",
            arrayOf(clean(email))
        ).use { cursor ->
            if (!cursor.moveToFirst()) {
                null
            } else {
                val salt = cursor.getString(4)
                val savedHash = cursor.getString(5)
                if (hash(password, salt) != savedHash) {
                    null
                } else {
                    User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                }
            }
        }
    }

    /** Sets a new password for this email. Returns false if there is no such account. */
    fun resetPassword(email: String, newPassword: String): Boolean {
        if (!emailExists(email)) return false

        val salt = newSalt()
        val values = ContentValues().apply {
            put("salt", salt)
            put("password_hash", hash(newPassword, salt))
        }
        return writableDatabase.update("users", values, "email = ?", arrayOf(clean(email))) > 0
    }

    /** Removes the account (used by "Delete Account"). */
    fun deleteUser(email: String): Boolean =
        writableDatabase.delete("users", "email = ?", arrayOf(clean(email))) > 0

    // --- helpers ---

    private fun clean(email: String) = email.trim().lowercase()

    private fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    private fun hash(password: String, salt: String): String {
        val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 10_000, 256)
        val key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
        return key.toHex()
    }

    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
}

/**
 * Remembers who is logged in (saved in SharedPreferences).
 * Call Session.init(...) once in MainActivity before using it.
 */
object Session {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences("loci_session", Context.MODE_PRIVATE)
    }

    fun login(user: User, remember: Boolean) {
        prefs.edit()
            .putString("email", user.email)
            .putString("name", "${user.firstName} ${user.lastName}")
            .putBoolean("remember", remember)
            .apply()
        com.example.lociapp.profile.ProfileRepository.onLogin(user.email)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    /** True if someone is logged in AND chose "Remember me". */
    fun isRemembered(): Boolean =
        prefs.getString("email", null) != null && prefs.getBoolean("remember", false)

    fun name(): String = prefs.getString("name", "") ?: ""

    fun email(): String = prefs.getString("email", "") ?: ""
}
