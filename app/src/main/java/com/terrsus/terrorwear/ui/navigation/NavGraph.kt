package com.terrsus.terrorwear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen

object Routes {
    const val DASHBOARD = "dashboard"
}

@Composable
fun TerrorWearNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen()
        }
    }
}
