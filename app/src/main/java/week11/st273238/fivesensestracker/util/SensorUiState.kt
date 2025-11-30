package week11.st273238.fivesensestracker.util

import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType

data class SensorUiState(
    val selectedSensor: SensorType? = null,
    val gpsLatitude: Double = 0.0,
    val gpsLongitude: Double = 0.0,
    val currentReading: SensorReading? = null,
    val history: List<SensorReading> = emptyList(),
    // Latest reading per sensor type, used in MainMenu tiles
    val latestReadings: Map<SensorType, SensorReading> = emptyMap(),
    val error: String? = null
)
