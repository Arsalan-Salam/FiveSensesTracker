package week11.st273238.fivesensestracker.ui.sensor

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
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
}
