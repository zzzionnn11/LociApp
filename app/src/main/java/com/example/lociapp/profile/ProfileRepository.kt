package com.example.lociapp.profile

import android.content.Context
import android.content.SharedPreferences
import com.example.lociapp.auth.ui.Session
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Languages the Settings screen can switch between. */
enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    FILIPINO("fil", "Filipino (placeholder)")
}

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val memberSince: String = "",
    val language: AppLanguage = AppLanguage.ENGLISH
)

/**
 * Profile data in SharedPreferences. Name and "user since" are saved per
 * account email, so signing out and back in brings them back. The language
 * choice belongs to the device, not the account.
 */
object ProfileRepository {
    private const val PREFS = "loci_profile"
    private const val KEY_LANGUAGE = "language"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    private fun today(): String = SimpleDateFormat("MMMM d, yyyy", Locale.US).format(Date())

    /** Called by Session.login: remember the first day this account was used. */
    fun onLogin(email: String) {
        if (!::prefs.isInitialized || email.isBlank()) return
        if (!prefs.contains("since_$email")) prefs.edit().putString("since_$email", today()).apply()
    }

    fun load(): ProfileState {
        val email = Session.email()
        val language = AppLanguage.entries.firstOrNull { it.code == prefs.getString(KEY_LANGUAGE, null) }
            ?: AppLanguage.ENGLISH
        val name = prefs.getString("name_$email", null)
            ?: Session.name().trim().ifBlank { "Loci User" }
        val since = prefs.getString("since_$email", null) ?: today().also {
            if (email.isNotBlank()) prefs.edit().putString("since_$email", it).apply()
        }
        return ProfileState(name = name, email = email, memberSince = since, language = language)
    }

    fun saveName(name: String) {
        val email = Session.email()
        if (email.isNotBlank()) prefs.edit().putString("name_$email", name).apply()
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    /** Used by "Delete Account": wipes everything stored here. */
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
