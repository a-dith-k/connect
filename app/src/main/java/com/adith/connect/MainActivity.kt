package com.adith.connect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adith.connect.ui.ChatScreen
import com.adith.connect.ui.LoginScreen
import com.adith.connect.ui.theme.ConnectTheme
import com.adith.connect.viewmodel.ChatViewModel
import com.adith.connect.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectTheme {
                val loginViewModel: LoginViewModel = viewModel()
                val chatViewModel: ChatViewModel = viewModel()

                val messages by chatViewModel.messages.collectAsState()

                if (!loginViewModel.loggedIn) {
                    LoginScreen(
                        username = loginViewModel.username,
                        password = loginViewModel.password,
                        onUsernameChange = { loginViewModel.username = it },
                        onPasswordChange = { loginViewModel.password = it },
                        onLoginClick = { loginViewModel.login() },
                        loginError = loginViewModel.loginError
                    )
                } else {
                    ChatScreen(
                        messages = messages,
                        currentUser = loginViewModel.username,
                        onSend = { messageText ->
                            chatViewModel.sendMessage(
                                text = messageText,
                                sender = loginViewModel.username
                            )
                        }
                    )
                }
            }
        }
    }
}
