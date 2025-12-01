package week11.st273238.fivesensestracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.data.repository.SensorRepository
import week11.st273238.fivesensestracker.util.UiState

class SensorViewModel(private val repo: SensorRepository) : ViewModel() {

    private val _light = MutableStateFlow<SensorReading?>(null)
    val light: StateFlow<SensorReading?> = _light

    private val _pressure = MutableStateFlow<SensorReading?>(null)
    val pressure: StateFlow<SensorReading?> = _pressure

    private val _accel = MutableStateFlow<SensorReading?>(null)
    val accel: StateFlow<SensorReading?> = _accel

    private val _prox = MutableStateFlow<SensorReading?>(null)
    val prox: StateFlow<SensorReading?> = _prox

    private val _location = MutableStateFlow<SensorReading?>(null)
    val location: StateFlow<SensorReading?> = _location

    private val _saveState = MutableStateFlow<UiState>(UiState.Empty)
    val saveState: StateFlow<UiState> = _saveState

    init {
        viewModelScope.launch {
            repo.observeAllSensors().collectLatest { reading ->
                when (reading.sensorType) {
                    SensorType.LIGHT -> _light.value = reading
                    SensorType.PRESSURE -> _pressure.value = reading
                    SensorType.ACCELERATION -> _accel.value = reading
                    SensorType.PROXIMITY -> _prox.value = reading
                    else -> Unit
                }
            }
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            repo.getLocationReading().collectLatest {
                _location.value = it
            }
        }
    }

    fun save(type: SensorType) {
        val reading = when (type) {
            SensorType.LIGHT -> light.value
            SensorType.PRESSURE -> pressure.value
            SensorType.ACCELERATION -> accel.value
            SensorType.PROXIMITY -> prox.value
            SensorType.LOCATION -> location.value
        }

        if (reading == null) {
            _saveState.value = UiState.Failure("No reading for $type")
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
