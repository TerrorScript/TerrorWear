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
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardButton
import com.terrsus.terrorwear.ui.screens.gatt.GattScreen
import com.terrsus.terrorwear.ui.screens.programassist.ProgramAssistScreen
import com.terrsus.terrorwear.ui.screens.stratagem.StratagemScreen
import com.terrsus.terrorwear.ui.util.BlePermissionBox
import com.terrsus.terrorwear.viewmodel.BleViewModel
import com.terrsus.terrorwear.viewmodel.GattViewModel

object Routes {
    const val DASHBOARD = "dashboard"
    const val STRATAGEM = "stratagem"
    const val BLE = "ble"
    const val PROGRAM_ASSIST = "program_assist"
}

@Composable
fun NavGraph(navController: NavHostController) {
// Log route changes
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val route = entry.destination.route
            android.util.Log.d("NAV", "Navigated to route: $route")
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(Routes.DASHBOARD) {
            val dashboardButtons = listOf(
                DashboardButton("Stratagem", Routes.STRATAGEM),
                DashboardButton("Arduino", Routes.BLE),
                DashboardButton("Program Assist", Routes.PROGRAM_ASSIST)
            )

            DashboardScreen(
                buttons = dashboardButtons,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Routes.STRATAGEM) {
            StratagemScreen()
        }

        composable(Routes.BLE) {
            val viewModel: BleViewModel = viewModel()
            BlePermissionBox {
                BleScreen(viewModel, navController)
            }
        }
        composable(
            route = "gatt/{address}",
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address")!!
            val viewModel: GattViewModel = viewModel {
                GattViewModel(address)
            }
            GattScreen(viewModel)
        }


        composable(Routes.PROGRAM_ASSIST) {
            ProgramAssistScreen()
        }
    }
}