package week11.st273238.fivesensestracker.util

sealed class UiState {
    object Empty : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Failure(val message: String) : UiState()
}
