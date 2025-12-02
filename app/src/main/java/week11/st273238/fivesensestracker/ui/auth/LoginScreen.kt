package week11.st273238.fivesensestracker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // Dark outer background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181818))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        // White centered card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title + subtitle
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "5 Senses Tracker",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(24.dp))

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                // Blue Login button
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0094FF) // Figma blue
                    )
                ) {
                    Text("Login")
                }

                Spacer(Modifier.height(16.dp))

                // Links
                Text(
                    text = "Create Account",
                    color = Color(0xFF0094FF),
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF0094FF),
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onNavigateToForgot() }
                )

                // Error text
                state.error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
