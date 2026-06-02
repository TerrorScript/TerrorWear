package com.terrsus.terrorwear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen
import com.terrsus.terrorwear.ui.screens.arduino.ArduinoScreen
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardButton
import com.terrsus.terrorwear.ui.screens.programassist.ProgramAssistScreen
import com.terrsus.terrorwear.ui.screens.stratagem.StratagemScreen
import com.terrsus.terrorwear.viewmodel.ArduinoViewModel

object Routes {
    const val DASHBOARD = "dashboard"
    const val STRATAGEM = "stratagem"
    const val ARDUINO = "arduino"
    const val PROGRAM_ASSIST = "program_assist"
}

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(Routes.DASHBOARD) {
            val dashboardButtons = listOf(
                DashboardButton("Stratagem", Routes.STRATAGEM),
                DashboardButton("Arduino", Routes.ARDUINO),
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

        composable(Routes.ARDUINO) {
            val viewModel: ArduinoViewModel = viewModel()
            ArduinoScreen(viewModel = viewModel)
        }

        composable(Routes.PROGRAM_ASSIST) {
            ProgramAssistScreen()
        }
    }
}