package com.adith.connect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adith.connect.data.model.ChatMessage
import com.adith.connect.repository.FirebaseChatRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = FirebaseChatRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMessages().collect { _messages.value = it }
        }
    }

    fun sendMessage(text: String, sender: String) {
        val msg = ChatMessage(text = text, sender = sender)
        viewModelScope.launch {
            repository.sendMessage(msg)
        }
    }
}
