package week11.st273238.fivesensestracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.util.SensorUiState

@Composable
fun MainMenuScreen(
    sensorState: SensorUiState,
    onSensorClick: (SensorType) -> Unit,
    onSignOut: () -> Unit,
    onRefreshLocation: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181818))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        // White centered card like the Figma
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {

                // Header
                Text(
                    text = "Main Menu",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "5 Senses Tracker",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "Select a Sensor",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // GRID inside the card (matches Figma tile layout)
                Column {

                    // Row 1 – Pressure / Light
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
                            color = Color(0xFFFFE082),      // warm yellow
                            onClick = onSensorClick
                        )
                        SensorTile(
                            type = SensorType.LIGHT,
                            primaryText = sensorState.latestReadings[SensorType.LIGHT]
                                ?.values?.firstOrNull()?.toString() ?: "—",
                            unit = "lx",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF81D4FA),      // light blue
                            onClick = onSensorClick
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Row 2 – Location (full width)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SensorTile(
                            type = SensorType.LOCATION,
                            primaryText = sensorState.latestReadings[SensorType.LOCATION]
                                ?.values?.let { vals ->
                                    if (vals.size >= 2)
                                        "${"%.4f".format(vals[0])}, ${"%.4f".format(vals[1])}"
                                    else
                                        "Lat / Lng"
                                } ?: "Lat / Lng",
                            unit = "Lat / Lng",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF80CBC4),      // teal
                            tileHeight = 96.dp,
                            onClick = {
                                onRefreshLocation()
                                onSensorClick(SensorType.LOCATION)
                            }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Row 3 – Acceleration / Proximity
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
                            color = Color(0xFFCE93D8),      // purple
                            onClick = onSensorClick
                        )
                        SensorTile(
                            type = SensorType.PROXIMITY,
                            primaryText = sensorState.latestReadings[SensorType.PROXIMITY]
                                ?.values?.firstOrNull()?.toString() ?: "—",
                            unit = "cm",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFFFAB91),      // orange/red
                            onClick = onSensorClick
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Sign Out button bottom-right inside the card (like Figma)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onSignOut,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5252) // red sign-out
                        )
                    ) {
                        Text("Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
private fun SensorTile(
    type: SensorType,
    primaryText: String,
    unit: String,
    modifier: Modifier = Modifier,
    color: Color,
    tileHeight: Dp = 88.dp,
    onClick: (SensorType) -> Unit
) {
    Surface(
        color = Color.Transparent,
        shadowElevation = 0.dp,
        modifier = modifier
            .height(tileHeight)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick(type) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = type.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Column {
                    Text(
                        text = primaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    if (unit.isNotBlank()) {
                        Text(
                            text = unit,
                            fontSize = 12.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }
            }
        }
    }
}


