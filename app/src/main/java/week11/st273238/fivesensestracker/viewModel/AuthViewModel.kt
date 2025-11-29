package week11.st273238.fivesensestracker.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.util.UiState
import week11.st273238.fivesensestracker.data.repository.SensorRepository

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // UiState for login/auth
    // Tracks whether the user is authenticated, or requires authentication, or is in a loading state.
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _message = MutableStateFlow<String?>(null)
//    val message: StateFlow<String?> = _message

    // Holds the current list of Sensor readings for the authenticated user.
    private val _sensors = MutableStateFlow<List<SensorReading>>(emptyList())
//    val sensors: StateFlow<List<SensorReading>> = _sensors

    init {
        // Observe FirebaseAuth state
        // Sets up an addAuthStateListener to automatically update the _uiState when the user's authentication status changes (login, logout).
        // When a user logs in, it also triggers the observation of their sensor list (todo).
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                _uiState.value = UiState.AuthRequired
                _sensors.value = emptyList()
            } else {
                _uiState.value = UiState.Authenticated
//                observeSensorList()
            }
        }
    }

//    fun getEmail(): String? {
//        return auth.currentUser?.email
//    }

    // login Firebase function
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _uiState.value = UiState.Authenticated }
            .addOnFailureListener { e ->
                _uiState.value = UiState.AuthRequired
                _message.value = e.localizedMessage ?: "Login failed"
            }
    }

    // signUp Firebase function
    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { _uiState.value = UiState.Authenticated }
            .addOnFailureListener { e ->
                _uiState.value = UiState.AuthRequired
                _message.value = e.localizedMessage ?: "Sign up failed"
            }
    }

    // logOut Firebase function
    fun logout() {
        auth.signOut()
        _uiState.value = UiState.AuthRequired
    }

}