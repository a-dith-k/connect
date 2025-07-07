package com.adith.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adith.connect.ui.*
import com.adith.connect.ui.theme.ConnectTheme
import com.adith.connect.viewmodel.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            ConnectTheme {
                val authViewModel: AuthViewModel = viewModel()
                val chatViewModel: ChatViewModel = viewModel()
                val adminViewModel: AdminViewModel = viewModel()

                val authState by authViewModel.authState.collectAsState()
                val messages by chatViewModel.messages.collectAsState()

                var showLogin by remember { mutableStateOf(true) }

                when (authState) {
                    is AuthState.Success -> {
                        val currentUser = authViewModel.getCurrentUserEmail() ?: "anonymous"
                        ChatScreen(
                            messages = messages,
                            currentUser = currentUser,
                            onSend = { message ->
                                chatViewModel.sendMessage(message, currentUser)
                            }
                        )
                    }

                    is AuthState.AdminSuccess -> {
                        AdminScreen(viewModel = adminViewModel)
                    }

                    else -> {
                        if (showLogin) {
                            LoginScreen(
                                onLoginSuccess = { /* handled by AuthState */ },
                                onAdminLoginSuccess = { /* handled by AuthState */ },
                                onSignupClick = { showLogin = false }
                            )
                        } else {
                            SignupScreen(
                                onSignupSuccess = { showLogin = true },
                                onLoginClick = { showLogin = true }
                            )
                        }
                    }
                }
            }
        }
    }
}
