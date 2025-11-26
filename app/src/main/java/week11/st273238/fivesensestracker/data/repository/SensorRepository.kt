package week11.st273238.fivesensestracker.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType

class SensorRepository(
    context: Context
) {
    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun observeSensors(): Flow<SensorReading> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                val type = when (event.sensor.type) {
                    Sensor.TYPE_PRESSURE -> SensorType.PRESSURE
                    Sensor.TYPE_LIGHT -> SensorType.LIGHT
                    Sensor.TYPE_ACCELEROMETER -> SensorType.ACCELERATION
                    Sensor.TYPE_PROXIMITY -> SensorType.PROXIMITY
                    else -> null
                } ?: return

                trySend(
                    SensorReading(
                        sensorType = type,
                        values = event.values.toList()
                    )
                )
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val sensors = listOf(
            sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        )

        sensors.forEach { sensor ->
            sensor?.let {
                sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
            }
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }

    fun getLastLocation(): Flow<SensorReading> = callbackFlow {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    trySend(
                        SensorReading(
                            sensorType = SensorType.LOCATION,
                            values = listOf(
                                it.latitude.toFloat(),
                                it.longitude.toFloat()
                            )
                        )
                    )
                }
                close()
            }
            .addOnFailureListener { close(it) }

        awaitClose { }
    }

    // Save reading to Firestore under users/{uid}/readings
    suspend fun saveReading(reading: SensorReading) {
        val uid = auth.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(uid)
            .collection("readings")
            .add(reading)
            .await()
    }

    // Load all readings from Firestore for current user
    suspend fun loadReadings(): List<SensorReading> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("readings")
            .get()
            .await()

        return snapshot.toObjects(SensorReading::class.java)
    }
}
