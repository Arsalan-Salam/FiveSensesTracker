package week11.st273238.fivesensestracker.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st273238.fivesensestracker.viewModel.AuthUiState

@Composable
fun ForgotPasswordScreen(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onResetClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Send Reset Link") }

        Spacer(Modifier.height(12.dp))

        Text(
            "Back to Login",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onBackToLogin() }
        )

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
