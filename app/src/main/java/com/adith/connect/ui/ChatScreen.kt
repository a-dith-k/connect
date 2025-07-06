package com.adith.connect.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adith.connect.data.model.ChatMessage
import com.adith.connect.ui.components.MessageBubble

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    currentUser: String,
    onSend: (String) -> Unit
) {
    var current by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                val isMe = message.sender == currentUser
                MessageBubble(message = message, isMe = isMe)
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = current,
                onValueChange = { current = it },
                placeholder = { Text("Type...") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (current.isNotBlank()) {
                        onSend(current.trim())
                        current = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}


