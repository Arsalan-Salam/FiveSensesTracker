package week11.st273238.fivesensestracker.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.data.repository.SensorRepository
import week11.st273238.fivesensestracker.util.SensorUiState

class SensorViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext
    private val repository = SensorRepository(appContext)

    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState: StateFlow<SensorUiState> = _uiState

    init {
        // Start collecting sensor and location updates as soon as ViewModel is created
        startSensorStreams()
    }

    private fun startSensorStreams() {
        // 1) Continuous hardware sensors: pressure, light, accel, proximity
        viewModelScope.launch {
            repository.observeSensors().collect { reading ->
                updateCurrentReading(reading)
            }
        }

        // 2) One-shot last known GPS location (your repo exposes this)
        viewModelScope.launch {
            repository.getLastLocation().collect { reading ->
                updateCurrentReading(reading)
            }
        }
    }
    // -----------------------
    // REFRESH GPS LOCATION
    // -----------------------
    @SuppressLint("MissingPermission")
    fun refreshLocation() {
        try {
            val lm = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (loc != null) {
                _uiState.value = _uiState.value.copy(
                    gpsLatitude = loc.latitude,
                    gpsLongitude = loc.longitude,
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "Unable to retrieve location"
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = e.localizedMessage
            )
        }
    }

    // -----------------------
    // UPDATE CURRENT READING
    // (call this when you get a new sensor reading)
    // -----------------------
    fun updateCurrentReading(reading: SensorReading) {
        val currentMap = _uiState.value.latestReadings.toMutableMap()
        currentMap[reading.sensorType] = reading

        val newHistory = (_uiState.value.history + reading).takeLast(50)

        _uiState.value = _uiState.value.copy(
            currentReading = reading,
            latestReadings = currentMap,
            history = newHistory,
            error = null
        )
    }

    // -----------------------
    // SAVE CURRENT READING
    // -----------------------
    fun saveCurrentReadingToCloud() {
        val reading = _uiState.value.currentReading ?: return

        viewModelScope.launch {
            repository.saveReading(reading)
        }
    }

    // -----------------------
    // LOAD READINGS FROM CLOUD
    // -----------------------
    fun loadHistory() {
        viewModelScope.launch {
            val history = repository.loadReadings()
            _uiState.value = _uiState.value.copy(history = history)
        }
    }

    fun selectSensor(type: SensorType) {
        _uiState.value = _uiState.value.copy(selectedSensor = type)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
