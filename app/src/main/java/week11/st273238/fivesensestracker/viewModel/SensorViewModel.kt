package week11.st273238.fivesensestracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.data.repository.SensorRepository

data class SensorUiState(
    val latestReadings: Map<SensorType, SensorReading> = emptyMap(),
    val selectedSensor: SensorType? = null,
    val savedHistory: Map<SensorType, List<SensorReading>> = emptyMap()
)

class SensorViewModel(
    context: Context
) : ViewModel() {

    private val repository = SensorRepository(context.applicationContext)

    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState: StateFlow<SensorUiState> = _uiState

    init {
        observeSensors()
    }

    private fun observeSensors() {
        viewModelScope.launch {
            repository.observeSensors().collect { reading ->
                _uiState.update { state ->
                    state.copy(
                        latestReadings = state.latestReadings + (reading.sensorType to reading)
                    )
                }
            }
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            repository.getLastLocation().collect { reading ->
                _uiState.update { state ->
                    state.copy(
                        latestReadings = state.latestReadings + (SensorType.LOCATION to reading)
                    )
                }
            }
        }
    }

    fun selectSensor(type: SensorType) {
        _uiState.update { it.copy(selectedSensor = type) }
    }

    fun saveCurrentReadingLocal() {
        val state = _uiState.value
        val type = state.selectedSensor ?: return
        val reading = state.latestReadings[type] ?: return

        _uiState.update {
            val old = it.savedHistory[type].orEmpty()
            it.copy(savedHistory = it.savedHistory + (type to (old + reading)))
        }
    }

    fun saveCurrentReadingToCloud() {
        val state = _uiState.value
        val type = state.selectedSensor ?: return
        val reading = state.latestReadings[type] ?: return

        viewModelScope.launch {
            repository.saveReading(reading)
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val history = repository.loadReadings()
            val grouped = history.groupBy { it.sensorType }

            _uiState.update { it.copy(savedHistory = grouped) }
        }
    }
}
