package week11.st273238.fivesensestracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.viewmodel.SensorUiState

@Composable
fun MainMenuScreen(
    sensorState: SensorUiState,
    onSensorClick: (SensorType) -> Unit,
    onSignOut: () -> Unit,
    onRefreshLocation: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text("Main Menu", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("5 Senses Tracker", fontSize = 16.sp, modifier = Modifier.padding(bottom = 24.dp))

            Text("Select a Sensor", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SensorTile(
                        type = SensorType.PRESSURE,
                        primaryText = sensorState.latestReadings[SensorType.PRESSURE]
                            ?.values?.firstOrNull()?.toString() ?: "—",
                        unit = "hPa",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        onClick = onSensorClick
                    )
                    SensorTile(
                        type = SensorType.LIGHT,
                        primaryText = sensorState.latestReadings[SensorType.LIGHT]
                            ?.values?.firstOrNull()?.toString() ?: "—",
                        unit = "lx",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        onClick = onSensorClick
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SensorTile(
                        type = SensorType.LOCATION,
                        primaryText = sensorState.latestReadings[SensorType.LOCATION]
                            ?.values?.let { vals ->
                                if (vals.size >= 2) "Lat: ${vals[0]}, Lng: ${vals[1]}" else "Lat / Lng"
                            } ?: "Lat / Lng",
                        unit = "",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        onClick = {
                            onRefreshLocation()
                            onSensorClick(SensorType.LOCATION)
                        }
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SensorTile(
                        type = SensorType.ACCELERATION,
                        primaryText = sensorState.latestReadings[SensorType.ACCELERATION]
                            ?.values?.firstOrNull()?.toString() ?: "—",
                        unit = "x",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = onSensorClick
                    )
                    SensorTile(
                        type = SensorType.PROXIMITY,
                        primaryText = sensorState.latestReadings[SensorType.PROXIMITY]
                            ?.values?.firstOrNull()?.toString() ?: "—",
                        unit = "cm",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.inversePrimary,
                        onClick = onSensorClick
                    )
                }
            }
        }

        Button(
            onClick = onSignOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text("Sign Out")
        }
    }
}

@Composable
private fun SensorTile(
    type: SensorType,
    primaryText: String,
    unit: String,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color,
    onClick: (SensorType) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 3.dp,
        modifier = modifier
            .height(100.dp)
            .clickable { onClick(type) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(type.displayName, fontWeight = FontWeight.Bold)
                Text("$primaryText $unit")
            }
        }
    }
}
