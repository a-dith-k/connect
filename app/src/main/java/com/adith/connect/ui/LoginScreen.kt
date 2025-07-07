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
import com.adith.connect.viewmodel.AuthState
import com.adith.connect.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onAdminLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🛑 Top-aligned error banner
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

        // 🎯 Login form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Log In", style = MaterialTheme.typography.headlineMedium)
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

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Checkbox(checked = isAdmin, onCheckedChange = { isAdmin = it })
                Text("I am admin")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.login(email, password, isAdmin)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onSignupClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Don't have an account? Sign up")
            }

            if (authState is AuthState.Loading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            when (authState) {
                is AuthState.Success -> LaunchedEffect(Unit) { onLoginSuccess() }
                is AuthState.AdminSuccess -> LaunchedEffect(Unit) { onAdminLoginSuccess() }
                else -> {}
            }
        }
    }
}

