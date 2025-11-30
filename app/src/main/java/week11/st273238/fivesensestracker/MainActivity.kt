package week11.st273238.fivesensestracker

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st273238.fivesensestracker.navigation.AppNavGraph
import week11.st273238.fivesensestracker.ui.theme.FiveSensesTrackerTheme
import week11.st273238.fivesensestracker.viewModel.AuthViewModel
import week11.st273238.fivesensestracker.viewModel.SensorViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // ViewModels owned by this Activity
            val authViewModel: AuthViewModel = viewModel()
            val sensorViewModel: SensorViewModel = viewModel()

            // Location Permission
            var askedPermission by remember { mutableStateOf(false) }
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { }

            LaunchedEffect(Unit) {
                if (!askedPermission) {
                    askedPermission = true
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            FiveSensesTrackerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph(
                        authViewModel = authViewModel,
                        sensorViewModel = sensorViewModel
                    )
                }
            }
        }
    }
}
