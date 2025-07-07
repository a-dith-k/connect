package com.adith.connect.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users: StateFlow<List<UserData>> = _users

    fun fetchUsers() {
        db.collection("users").addSnapshotListener { snapshot, _ ->
            _users.value = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(UserData::class.java)?.copy(id = doc.id)
            } ?: emptyList()
        }
    }

    fun toggleUserEnabled(id: String, newState: Boolean) {
        db.collection("users").document(id).update("enabled", newState)
    }
}

data class UserData(
    val id: String = "",
    val email: String = "",
    val enabled: Boolean = true
)
