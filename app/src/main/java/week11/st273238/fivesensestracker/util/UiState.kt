package week11.st273238.fivesensestracker.util

//# Sealed class for Loading/AuthRequired/Authenticated
sealed class UiState {
    object Loading : UiState()
    object AuthRequired : UiState()
    object Authenticated : UiState()
}