package com.adith.connect.repository

import com.adith.connect.data.model.ChatMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose

class FirebaseChatRepository {

    private val ref = Firebase.firestore.collection("messages")

    suspend fun sendMessage(message: ChatMessage) {
        ref.add(message).await()
    }

    fun getMessages(): Flow<List<ChatMessage>> = callbackFlow {
        val listener = ref
            .orderBy("timestamp")
            .addSnapshotListener { snap, _ ->
                snap?.let { trySend(it.toObjects(ChatMessage::class.java)) }
            }
        awaitClose { listener.remove() }
    }
}
