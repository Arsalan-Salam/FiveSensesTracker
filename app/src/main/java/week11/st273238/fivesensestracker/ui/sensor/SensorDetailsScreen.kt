package week11.st273238.fivesensestracker.ui.sensor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text("Sensor Detail", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("5 Senses Tracker", fontSize = 16.sp, modifier = Modifier.padding(bottom = 24.dp))

        Text("Sensor: ${sensorType.name}", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        // SENSOR VALUES
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Value 1: ${reading?.values?.getOrNull(0) ?: "0.00"}")
                Text("Value 2: ${reading?.values?.getOrNull(1) ?: "0.00"}")
                Text("Value 3: ${reading?.values?.getOrNull(2) ?: "0.00"}")
            }
        }

        Spacer(Modifier.height(16.dp))

        // GRAPH PLACEHOLDER
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            LiveSensorGraph(
                values = reading?.values ?: emptyList(),
                modifier = Modifier.fillMaxSize()
            )
                Text("Live graph placeholder")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onSaveReading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Save Reading")
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("← Back to Main Menu")
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