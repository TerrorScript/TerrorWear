package com.terrsus.terrorwear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.terrsus.terrorwear.LocalAppContainer
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen
import com.terrsus.terrorwear.ui.screens.ble.BleScreen
import com.terrsus.terrorwear.ui.screens.cameraremote.CameraRemoteScreen
import com.terrsus.terrorwear.modules.games.pong.ui.PongScreen
import com.terrsus.terrorwear.modules.games.tilt.ui.TiltScreen
import com.terrsus.terrorwear.modules.ble.ui.gatt.GattScreen
import com.terrsus.terrorwear.modules.tools.programassist.ui.ProgramAssistScreen
import com.terrsus.terrorwear.modules.games.stratagem.ui.StratagemScreen
import com.terrsus.terrorwear.features.ble.ui.components.BlePermissionBox
import com.terrsus.terrorwear.modules.settings.ui.SettingsScreen
import com.terrsus.terrorwear.modules.settings.viewmodel.SettingsViewModel
import com.terrsus.terrorwear.viewmodel.ble.BleViewModel
import com.terrsus.terrorwear.viewmodel.ble.GattViewModel

@Composable
fun NavGraph(navController: NavHostController) {

    // Log route changes
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            android.util.Log.d("NAV", "Navigated to route: ${entry.destination.route}")
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.Dashboard.path
    ) {
        // Dashboard
        composable(Route.Dashboard.path) {
            val dashboardButtons = listOf(
                Route.Settings,

                Route.ProgramAssist,
                Route.CameraRemote,

                Route.Pong,
                Route.Tilt,
                Route.Stratagem,

                Route.Ble,
                Route.Wifi,
            )

            DashboardScreen(
                routes = dashboardButtons,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // Settings
        composable(Route.Settings.path) {
            val localAppContainer = LocalAppContainer.current
            val viewModel = remember { SettingsViewModel(localAppContainer.settingsInteractor) }

            SettingsScreen(viewModel)
        }

        // Program Assist
        composable(Route.ProgramAssist.path) {
            ProgramAssistScreen()
        }

        // Camera Remote
        composable(Route.CameraRemote.path) {
            CameraRemoteScreen()
        }

        // Stratagem
        composable(Route.Stratagem.path) {
            StratagemScreen()
        }

        // Pong
        composable(Route.Pong.path) {
            PongScreen(navController)
        }

        // Tilt
        composable(Route.Tilt.path) {
            TiltScreen(navController)
        }

        // BLE
        composable(Route.Ble.path) {
            BlePermissionBox {
                BleScreen(navController)
            }
        }

        // GATT
        composable(
            route = Route.Gatt.path,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address")!!
            val viewModel: GattViewModel = viewModel { GattViewModel(address) }
            GattScreen(viewModel)
        }

    }
}