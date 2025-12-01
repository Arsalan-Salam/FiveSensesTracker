package week11.st273238.fivesensestracker.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Looper
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st273238.fivesensestracker.data.model.SensorReading
import week11.st273238.fivesensestracker.data.model.SensorType

class SensorRepository(context: Context) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val fusedLocation =
        LocationServices.getFusedLocationProviderClient(context)

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Streams ALL sensors.
     * ViewModel filters based on sensorType.
     */
    fun observeAllSensors(): Flow<SensorReading> = callbackFlow {

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                val type = when (event.sensor.type) {
                    Sensor.TYPE_PRESSURE -> SensorType.PRESSURE
                    Sensor.TYPE_LIGHT -> SensorType.LIGHT
                    Sensor.TYPE_ACCELEROMETER -> SensorType.ACCELERATION
                    Sensor.TYPE_PROXIMITY -> SensorType.PROXIMITY
                    else -> return
                }

                trySend(
                    SensorReading(
                        sensorType = type,
                        values = event.values.toList()
                    )
                )
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        listOf(
            Sensor.TYPE_PRESSURE,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_PROXIMITY
        ).forEach { sType ->
            sensorManager.getDefaultSensor(sType)?.let { sensor ->
                sensorManager.registerListener(
                    listener,
                    sensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }

    /**
     * One-shot GPS reading.
     */
    @SuppressLint("MissingPermission")
    fun getLocationReading(): Flow<SensorReading> = callbackFlow {

        val request = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = 1000
        }

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return

                trySend(
                    SensorReading(
                        sensorType = SensorType.LOCATION,
                        values = listOf(loc.latitude.toFloat(), loc.longitude.toFloat()),
                        latitude = loc.latitude,
                        longitude = loc.longitude
                    )
                )

                fusedLocation.removeLocationUpdates(this)
                close()
            }
        }

        fusedLocation.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        awaitClose { }
    }

    suspend fun saveReading(reading: SensorReading): Boolean {
        val uid = auth.currentUser?.uid ?: return false

        return try {
            firestore.collection("users")
                .document(uid)
                .collection("readings")
                .add(reading)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun loadReadings(): List<SensorReading> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        return try {
            firestore.collection("users")
                .document(uid)
                .collection("readings")
                .get()
                .await()
                .toObjects(SensorReading::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
