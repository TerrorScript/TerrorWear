package com.terrsus.terrorwear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen
import com.terrsus.terrorwear.ui.screens.ble.BleScreen
import com.terrsus.terrorwear.ui.screens.dashboard.model.DashboardButton
import com.terrsus.terrorwear.ui.screens.games.pong.PongScreen
import com.terrsus.terrorwear.ui.screens.games.tilt.TiltScreen
import com.terrsus.terrorwear.ui.screens.gatt.GattScreen
import com.terrsus.terrorwear.ui.screens.programassist.ProgramAssistScreen
import com.terrsus.terrorwear.ui.screens.stratagem.StratagemScreen
import com.terrsus.terrorwear.ui.util.BlePermissionBox
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
        startDestination = Route.Dashboard.route
    ) {

        // Dashboard
        composable(Route.Dashboard.route) {
            val dashboardButtons = listOf(
                DashboardButton("Stratagem", Route.Stratagem.route),
                DashboardButton("Bluetooth LE", Route.Ble.route),
                DashboardButton("WiFi (UDP & TCP)", Route.Wifi.route),
                DashboardButton("Program Assist", Route.ProgramAssist.route),
                DashboardButton("Pong", Route.Pong.route),
                DashboardButton("Tilt", Route.Tilt.route)
            )

            DashboardScreen(
                buttons = dashboardButtons,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // Stratagem
        composable(Route.Stratagem.route) {
            StratagemScreen()
        }

        // BLE
        composable(Route.Ble.route) {
            val viewModel: BleViewModel = viewModel()
            BlePermissionBox {
                BleScreen(viewModel, navController)
            }
        }

        // GATT
        composable(
            route = Route.Gatt.route,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address")!!
            val viewModel: GattViewModel = viewModel { GattViewModel(address) }
            GattScreen(viewModel)
        }

        // Program Assist
        composable(Route.ProgramAssist.route) {
            ProgramAssistScreen()
        }

        // Pong
        composable(Route.Pong.route) {
            PongScreen(navController)
        }

        // Tilt
        composable(Route.Tilt.route) {
            TiltScreen(navController)
        }
    }
}