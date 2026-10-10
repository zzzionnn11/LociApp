package com.example.lociapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lociapp.auth.ui.Session
import com.example.lociapp.auth.ui.UserDatabase
import com.example.lociapp.db.ItemRepository
import com.example.lociapp.db.ReminderRepository
import com.example.lociapp.profile.AppLanguage
import com.example.lociapp.profile.ProfileRepository
import com.example.lociapp.profile.ProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val _state = MutableStateFlow(ProfileRepository.load())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun updateName(newName: String) {
        val name = newName.trim()
        if (name.isEmpty()) return
        ProfileRepository.saveName(name)
        _state.value = ProfileRepository.load()
    }

    /** Called by the screen after the user picks an image from the gallery. */
    fun updatePhoto(uri: Uri) {
        viewModelScope.launch {
            // Copying a file can be slow, so do it off the main thread
            withContext(Dispatchers.IO) {
                ProfileRepository.savePhoto(getApplication(), uri)
            }
            _state.value = ProfileRepository.load()
        }
    }

    fun setLanguage(language: AppLanguage) {
        ProfileRepository.saveLanguage(language)
        _state.value = ProfileRepository.load()
    }

    /** Ends the session only. The account and the saved items stay on the device. */
    fun signOut() {
        Session.logout()
    }

    /** Wipes the account, items, reminders (and their alarms), photos and saved settings. */
    fun deleteAccount(onDone: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val context = getApplication<Application>()
                val email = Session.email()

                ReminderRepository.clearAll()
                ItemRepository.clearAll()
                File(context.filesDir, "photos").deleteRecursively()
                File(context.filesDir, "profile").deleteRecursively()   // profile pictures

                if (email.isNotBlank()) {
                    val users = UserDatabase(context)
                    users.deleteUser(email)
                    users.close()
                }
                ProfileRepository.clearAll()
                Session.logout()
            }
            onDone()
        }
    }
}