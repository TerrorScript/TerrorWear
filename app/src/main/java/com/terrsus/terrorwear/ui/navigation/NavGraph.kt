package com.terrsus.terrorwear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.terrsus.terrorwear.LocalAppContainer
import com.terrsus.terrorwear.domain.features.FeatureLifecycleController
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen
import com.terrsus.terrorwear.ui.screens.ble.BleScreen
import com.terrsus.terrorwear.ui.screens.cameraremote.CameraRemoteScreen
import com.terrsus.terrorwear.modules.games.pong.ui.PongScreen
import com.terrsus.terrorwear.modules.games.tilt.ui.TiltScreen
import com.terrsus.terrorwear.modules.ble.ui.gatt.GattScreen
import com.terrsus.terrorwear.modules.tools.programassist.ui.ProgramAssistScreen
import com.terrsus.terrorwear.modules.games.stratagem.ui.StratagemScreen
import com.terrsus.terrorwear.features.ble.ui.components.BlePermissionBox
import com.terrsus.terrorwear.modules.imu.ui.ImuScreen
import com.terrsus.terrorwear.modules.settings.ui.SettingsScreen
import com.terrsus.terrorwear.modules.settings.viewmodel.SettingsViewModel
import com.terrsus.terrorwear.modules.tools.compass.ui.CompassScreen
import com.terrsus.terrorwear.viewmodel.ble.BleViewModel
import com.terrsus.terrorwear.viewmodel.ble.GattViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    // Access the global app container (DI root)
    val app = LocalAppContainer.current

    // Create the lifecycle controller ONCE per NavGraph composition.
    // remember {} ensures it is not recreated on every recomposition.
    val lifecycle = remember {
        FeatureLifecycleController(
            ble = app.bleManager,
            wifi = app.wifiManager,
            sensors = app.sensorManager
        )
    }

    // Track the previously active route.
    // This persists across recompositions but resets if NavGraph is recreated.
    var previousRoute by remember { mutableStateOf<Route?>(null) }

    // React to changes in the `route` parameter.
    // When the composable for a route enters composition, this fires.
    @Composable
    fun enter(route: Route) {
        LaunchedEffect(route) {
            // Notify lifecycle controller of the transition
            lifecycle.onRouteChanged(previousRoute, route)

            // Update previous route for next transition
            previousRoute = route
        }
    }

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

        // ------------------------------
        // Dashboard
        // ------------------------------
        composable(Route.Dashboard.path) {
            enter(Route.Dashboard)

            val dashboardButtons = listOf(
                Route.Settings,

                Route.ProgramAssist,
                Route.CameraRemote,
                Route.Compass,

                Route.Pong,
                Route.Tilt,
                Route.Stratagem,

                Route.Ble,
                Route.Wifi,
                Route.Imu,
            )

            DashboardScreen(
                routes = dashboardButtons,
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // ------------------------------
        // System
        // ------------------------------
        composable(Route.Settings.path) {
            enter(Route.Settings)

            val localAppContainer = LocalAppContainer.current
            val viewModel = remember { SettingsViewModel(localAppContainer.settingsInteractor) }

            SettingsScreen(viewModel)
        }

        // ------------------------------
        // Tools
        // ------------------------------
        composable(Route.ProgramAssist.path) {
            enter(Route.ProgramAssist)
            ProgramAssistScreen()
        }

        composable(Route.CameraRemote.path) {
            enter(Route.CameraRemote)
            CameraRemoteScreen()
        }

        composable(Route.Compass.path) {
            enter(Route.Compass)
            CompassScreen()
        }

        // ------------------------------
        // Games
        // ------------------------------
        composable(Route.Stratagem.path) {
            enter(Route.Stratagem)
            StratagemScreen()
        }

        composable(Route.Pong.path) {
            enter(Route.Pong)
            PongScreen(navController)
        }

        composable(Route.Tilt.path) {
            enter(Route.Tilt)
            TiltScreen(navController)
        }

        // ------------------------------
        // Debug / Misc
        // ------------------------------
        composable(Route.Ble.path) {
            enter(Route.Ble)
            BlePermissionBox {
                BleScreen(navController)
            }
        }

        composable(
            route = Route.Gatt.path,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            enter(Route.Gatt)

            val address = backStackEntry.arguments?.getString("address")!!
            val viewModel: GattViewModel = viewModel { GattViewModel(address) }
            GattScreen(viewModel)
        }

        composable(Route.Imu.path) {
            enter(Route.Imu)
            ImuScreen()
        }
    }
}