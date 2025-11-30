package week11.st273238.fivesensestracker.data.model

data class SensorReading(
    val sensorType: SensorType = SensorType.PRESSURE,
    val values: List<Float> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
