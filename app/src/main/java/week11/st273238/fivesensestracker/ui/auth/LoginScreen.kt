package week11.st273238.fivesensestracker.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st273238.fivesensestracker.viewModel.AuthUiState

@Composable
fun LoginScreen(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onNavigateToSignUp) {
            Text("Create an Account")
        }

        TextButton(onClick = onNavigateToForgot) {
            Text("Forgot Password?")
        }

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
