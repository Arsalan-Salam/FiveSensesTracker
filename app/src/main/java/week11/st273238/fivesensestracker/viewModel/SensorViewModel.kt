package week11.st273238.fivesensestracker.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.data.repository.SensorRepository
import week11.st273238.fivesensestracker.util.SensorUiState
import week11.st273238.fivesensestracker.util.UiState

class SensorViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SensorRepository(application.applicationContext)

    // Single state object for all sensors + current detail
    private val _sensorState = MutableStateFlow(SensorUiState())
    val sensorState: StateFlow<SensorUiState> = _sensorState

    private val _saveState = MutableStateFlow<UiState>(UiState.Empty)
    val saveState: StateFlow<UiState> = _saveState

    init {
        viewModelScope.launch {
            repo.observeAllSensors().collectLatest { reading ->
                val current = _sensorState.value
                val latest = current.latestReadings.toMutableMap()
                latest[reading.sensorType] = reading

                _sensorState.value = current.copy(
                    latestReadings = latest,
                    currentReading = if (current.currentReading?.sensorType == reading.sensorType)
                        reading
                    else
                        current.currentReading
                )
            }
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            repo.getLocationReading().collectLatest { reading ->
                val current = _sensorState.value
                val latest = current.latestReadings.toMutableMap()
                latest[SensorType.LOCATION] = reading

                _sensorState.value = current.copy(
                    latestReadings = latest,
                    currentReading = if (current.currentReading?.sensorType == SensorType.LOCATION)
                        reading
                    else
                        current.currentReading
                )
            }
        }
    }

    fun selectSensor(type: SensorType) {
        val latest = _sensorState.value.latestReadings
        _sensorState.value = _sensorState.value.copy(
            currentReading = latest[type]
        )
    }

    fun saveCurrent() {
        val reading = _sensorState.value.currentReading
        if (reading == null) {
            _saveState.value = UiState.Failure("No reading to save")
            return
        }

        _saveState.value = UiState.Loading

        viewModelScope.launch {
            val ok = repo.saveReading(reading)
            _saveState.value =
                if (ok) UiState.Success else UiState.Failure("Save failed")
        }
    }
}
