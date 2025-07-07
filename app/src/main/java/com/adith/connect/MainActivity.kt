package com.adith.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adith.connect.ui.ChatScreen
import com.adith.connect.ui.LoginScreen
import com.adith.connect.ui.SignupScreen
import com.adith.connect.ui.theme.ConnectTheme
import com.adith.connect.viewmodel.AuthViewModel
import com.adith.connect.viewmodel.AuthState
import com.adith.connect.viewmodel.ChatViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Ensure Firebase is initialized

        setContent {
            ConnectTheme {
                val authViewModel: AuthViewModel = viewModel()
                val chatViewModel: ChatViewModel = viewModel()

                val authState by authViewModel.authState.collectAsState()
                val messages by chatViewModel.messages.collectAsState()

                // Control screen toggling between Login and Signup
                var showLoginScreen by remember { mutableStateOf(true) }

                when (authState) {
                    is AuthState.Success -> {
                        val currentUser = authViewModel.getCurrentUserEmail() ?: "anonymous"
                        ChatScreen(
                            messages = messages,
                            currentUser = currentUser,
                            onSend = { messageText ->
                                chatViewModel.sendMessage(
                                    text = messageText,
                                    sender = currentUser
                                )
                            }
                        )
                    }

                    else -> {
                        if (showLoginScreen) {
                            LoginScreen(
                                onLoginSuccess = { /* AuthState.Success handles nav */ },
                                onSignupClick = { showLoginScreen = false }
                            )
                        } else {
                            SignupScreen(
                                onSignupSuccess = { /* AuthState.Success handles nav */ },
                                onLoginClick = { showLoginScreen = true }
                            )
                        }
                    }
                }
            }
        }
    }
}
