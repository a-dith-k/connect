package com.adith.connect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adith.connect.viewmodel.AuthViewModel
import com.adith.connect.viewmodel.AuthState

@Composable
fun SignupScreen(
    viewModel: AuthViewModel = viewModel(),
    onSignupSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🌟 Friendly Message Display at the Top
        if (authState is AuthState.Error) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp, start = 24.dp, end = 24.dp)
            ) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFEBEB))
                ) {
                    Text(
                        text = friendlyMessage((authState as AuthState.Error).message),
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 🧱 Main Form Centered
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (authState is AuthState.Error) viewModel.clearError()
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (authState is AuthState.Error) viewModel.clearError()
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.signUp(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? Log in")
            }

            if (authState is AuthState.Loading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (authState is AuthState.Success) {
                LaunchedEffect(Unit) { onSignupSuccess() }
            }
        }
    }
}

// ✅ Converts technical error to user-friendly message
fun friendlyMessage(message: String): String {
    return when {
        "email address is badly" in message.lowercase() -> "Please enter a valid email address"
        "password should be at least" in message.lowercase() -> "Password must be at least 6 characters"
        "email address is already" in message.lowercase() -> "This email is already registered"
        else -> message // fallback to raw if unknown
    }
}
