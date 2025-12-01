package week11.st273238.fivesensestracker.util

import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType

data class SensorUiState(
    val latestReadings: Map<SensorType, SensorReading> = emptyMap(),
    val currentReading: SensorReading? = null
)
