package week11.st273238.fivesensestracker.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import week11.st273238.fivesensestracker.data.model.SensorType
import week11.st273238.fivesensestracker.ui.auth.ForgotPasswordScreen
import week11.st273238.fivesensestracker.ui.auth.LoginScreen
import week11.st273238.fivesensestracker.ui.auth.SignUpScreen
import week11.st273238.fivesensestracker.ui.main.MainMenuScreen
import week11.st273238.fivesensestracker.ui.sensor.SensorDetailsScreen
import week11.st273238.fivesensestracker.viewModel.AuthViewModel
import week11.st273238.fivesensestracker.viewModel.SensorViewModel

object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val FORGOT = "forgot"
    const val MAIN_MENU = "main_menu"
    const val SENSOR_DETAIL = "sensor_detail"
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    sensorViewModel: SensorViewModel = viewModel()   // ✅ no factory needed now
) {
    val navController = rememberNavController()

    val authState by authViewModel.uiState.collectAsState()
    val sensorState by sensorViewModel.sensorState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {

        // LOGIN
        composable(Routes.LOGIN) {
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                state = authState,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onLoginClick = { authViewModel.login() },
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) },
                onNavigateToForgot = { navController.navigate(Routes.FORGOT) }
            )
        }

        // SIGN UP
        composable(Routes.SIGN_UP) {
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                }
            }

            SignUpScreen(
                state = authState,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onConfirmPasswordChange = authViewModel::onConfirmPasswordChange,
                onSignUpClick = { authViewModel.signUp() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // FORGOT PASSWORD
        composable(Routes.FORGOT) {
            ForgotPasswordScreen(
                state = authState,
                onEmailChange = authViewModel::onEmailChange,
                onResetClick = { authViewModel.resetPassword() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // MAIN MENU
        composable(Routes.MAIN_MENU) {
            MainMenuScreen(
                sensorState = sensorState,
                onSensorClick = { type ->
                    sensorViewModel.selectSensor(type)
                    navController.navigate("${Routes.SENSOR_DETAIL}/${type.name}")
                },
                onSignOut = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = true }
                    }
                },
                onRefreshLocation = { sensorViewModel.refreshLocation() }
            )
        }

        // SENSOR DETAIL
        composable(
            route = "${Routes.SENSOR_DETAIL}/{sensorType}",
            arguments = listOf(
                navArgument("sensorType") { type = NavType.StringType }
            )
        ) { entry ->
            val sensorTypeName = entry.arguments?.getString("sensorType")!!
            val sensorType = SensorType.valueOf(sensorTypeName)

            SensorDetailsScreen(
                sensorType = sensorType,
                state = sensorState,
                onBack = { navController.popBackStack() },
                onSaveReading = { sensorViewModel.saveCurrent() }
            )
        }
    }
}
