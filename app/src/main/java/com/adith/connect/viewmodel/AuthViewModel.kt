package com.adith.connect.viewmodel

import androidx.lifecycle.ViewModel
import com.adith.connect.ui.friendlyMessage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please enter both email and password")
            return
        }

        if (!email.contains("@")) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userDoc = mapOf(
                        "email" to email,
                        "enabled" to true
                    )

                    user?.uid?.let { uid ->
                        Firebase.firestore.collection("users").document(uid)
                            .set(userDoc)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Success
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error("Signup succeeded but user metadata failed: ${e.localizedMessage}")
                            }
                    } ?: run {
                        _authState.value = AuthState.Success
                    }

                } else {
                    val errorMessage = task.exception?.message ?: "Signup failed"
                    _authState.value = AuthState.Error(friendlyMessage(errorMessage))
                }
            }
    }


    fun login(email: String, password: String, isAdmin: Boolean) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    if (isAdmin && email.lowercase() == "admin@example.com") {
                        AuthState.AdminSuccess
                    } else {
                        AuthState.Success
                    }
                } else {
                    AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }


    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object AdminSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}

