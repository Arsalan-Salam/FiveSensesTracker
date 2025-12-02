package week11.st273238.fivesensestracker.ui.sensor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.util.SensorUiState
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun SensorDetailsScreen(
    sensorType: SensorType,
    state: SensorUiState,
    onBack: () -> Unit,
    onSaveReading: () -> Unit
) {
    val reading = state.currentReading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181818))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // Title
                Text(
                    text = "Sensor Detail",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "5 Senses Tracker",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Sensor: ${sensorType.name}",
                    fontSize = 16.sp
                )

                Spacer(Modifier.height(12.dp))

                // Values box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF2F2F2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        val values = state.latestReadings[sensorType]?.values ?: emptyList()

                        Text("Value 1: ${values.getOrNull(0) ?: "0.00"}")
                        Text("Value 2: ${values.getOrNull(1) ?: "0.00"}")
                        Text("Value 3: ${values.getOrNull(2) ?: "0.00"}")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Graph placeholder
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Live graph placeholder",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Green Save Reading button
                Button(
                    onClick = onSaveReading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27AE60) // Figma green
                    )
                ) {
                    Text("Save Reading")
                }

                Spacer(Modifier.height(20.dp))

                // Back to Main Menu link
                Text(
                    text = "< Back to Main Menu",
                    color = Color(0xFF0094FF),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }
        }
    }
}

@Composable
fun LiveSensorGraph(
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    val graphColor = MaterialTheme.colorScheme.primary

    if (values.isEmpty()) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text("Waiting for sensor data…")
        }
        return
    }

    Canvas(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val maxVal = values.maxOrNull() ?: 0f
        val minVal = values.minOrNull() ?: 0f
        val range = if (maxVal == minVal) 1f else (maxVal - minVal)

        val stepX = if (values.size <= 1) 0f else size.width / (values.size - 1)
        val path = Path()

        values.forEachIndexed { index, v ->
            val normalized = (v - minVal) / range
            val x = stepX * index
            val y = size.height - (normalized * size.height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = graphColor,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
    }
}