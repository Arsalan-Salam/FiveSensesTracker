package week11.st273238.fivesensestracker.data.model

data class SensorReading(
    val sensorType: SensorType = SensorType.PRESSURE,
    val values: List<Float> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
