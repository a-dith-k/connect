package com.adith.connect.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)
    var loggedIn by mutableStateOf(false)

    // ✅ Acceptable usernames
    private val validUsernames = listOf("adith", "samjith", "samyuktha", "rahul", "prathush","ridul")

    // ✅ Shared password for now
    private val validPassword = "test123"

    fun login() {
        loginError = when {
            username !in validUsernames -> "Invalid username"
            password != validPassword -> "Invalid password"
            else -> {
                loggedIn = true
                null
            }
        }
    }
}
