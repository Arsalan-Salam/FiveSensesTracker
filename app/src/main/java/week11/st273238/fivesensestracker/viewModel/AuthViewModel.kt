package week11.st273238.fivesensestracker.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// NOTE: AuthUiState is defined in AuthUiState.kt in the same package.
// Do NOT redeclare it here.

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    // --------------------
    // FIELD UPDATES
    // --------------------
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, error = null)
    }

    // --------------------
    // LOGIN
    // --------------------
    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Email and password are required")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        auth.signInWithEmailAndPassword(state.email, state.password)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    error = null
                )
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Login failed"
                )
            }
    }

    // --------------------
    // SIGN UP
    // --------------------
    fun signUp() {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            _uiState.value = state.copy(error = "All fields are required")
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(error = "Passwords do not match")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        auth.createUserWithEmailAndPassword(state.email, state.password)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    error = null
                )
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Sign up failed"
                )
            }
    }

    // --------------------
    // RESET PASSWORD (used by ForgotPasswordScreen)
    // --------------------
    fun resetPassword() {
        val state = _uiState.value
        if (state.email.isBlank()) {
            _uiState.value = state.copy(error = "Please enter your email")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        auth.sendPasswordResetEmail(state.email)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Password reset email sent"
                )
            }
            .addOnFailureListener { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Failed to send reset email"
                )
            }
    }

    // --------------------
    // LOG OUT
    // --------------------
    fun logout() {
        auth.signOut()
        _uiState.value = AuthUiState() // back to default state
    }
}
