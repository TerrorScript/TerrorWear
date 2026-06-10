package com.terrsus.terrorwear.ui.navigation

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.terrsus.terrorwear.LocalAppContainer
import com.terrsus.terrorwear.domain.features.Feature
import com.terrsus.terrorwear.domain.features.FeatureLifecycleController
import com.terrsus.terrorwear.modules.ble.ui.gatt.GattScreen
import com.terrsus.terrorwear.modules.games.pong.ui.PongScreen
import com.terrsus.terrorwear.modules.games.pong.viewmodel.PongViewModel
import com.terrsus.terrorwear.modules.games.stratagem.ui.StratagemScreen
import com.terrsus.terrorwear.modules.games.stratagem.viewmodel.StratagemViewModel
import com.terrsus.terrorwear.modules.games.tilt.ui.TiltScreen
import com.terrsus.terrorwear.modules.games.tilt.viewmodel.TiltViewModel
import com.terrsus.terrorwear.modules.imu.ui.ImuScreen
import com.terrsus.terrorwear.modules.settings.ui.SettingsScreen
import com.terrsus.terrorwear.modules.settings.viewmodel.SettingsViewModel
import com.terrsus.terrorwear.modules.tools.cameraremote.ui.CameraRemoteScreen
import com.terrsus.terrorwear.modules.tools.cameraremote.viewmodel.CameraRemoteViewModel
import com.terrsus.terrorwear.modules.tools.compass.ui.CompassScreen
import com.terrsus.terrorwear.modules.tools.programassist.ui.ProgramAssistScreen
import com.terrsus.terrorwear.modules.tools.programassist.viewmodel.ProgramAssistViewModel
import com.terrsus.terrorwear.modules.wifi.ui.WifiInfoScreen
import com.terrsus.terrorwear.modules.wifi.ui.WifiLogScreen
import com.terrsus.terrorwear.modules.wifi.ui.WifiToolsScreen
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiConnectionViewModel
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiNetworkInfoViewModel
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiPacketViewModel
import com.terrsus.terrorwear.ui.screens.ble.BleScreen
import com.terrsus.terrorwear.ui.screens.dashboard.DashboardScreen
import com.terrsus.terrorwear.viewmodel.ble.BleViewModel
import com.terrsus.terrorwear.viewmodel.ble.GattViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val LogTag = "TW/NavGraph"

class PermissionContinuationHolder {
    var cont: Continuation<Boolean>? = null
}


@Composable
fun NavGraph(
    context: Context,
    navController: NavController
) {
    // Access the global app container (DI root)
    val app = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    var permissionRequestInProgress by remember { mutableStateOf(false) }

    val permissionContinuationHolder = remember { PermissionContinuationHolder() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }
        permissionContinuationHolder.cont?.resume(granted)
        permissionContinuationHolder.cont = null
    }

    suspend fun requestPermissions(perms: List<Permission>): Boolean =
        suspendCoroutine { cont ->
            permissionContinuationHolder.cont = cont
            launcher.launch(perms.map { it.androidName }.toTypedArray())
        }

    val permissionOrchestrator = PermissionOrchestrator(
        context,
        bleProvider = { app.bleManager },
        wifiProvider = { app.wifiManager },
        sensorsProvider = { app.sensorManager },
        ::requestPermissions
    )

    // Create the lifecycle controller ONCE per NavGraph composition.
    // remember {} ensures it is not recreated on every recomposition.
    val lifecycle = remember {
        FeatureLifecycleController(
            bleProvider = { app.bleManager },
            wifiProvider = { app.wifiManager },
            sensorsProvider = { app.sensorManager }
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

    fun attemptNavigate(route: Route, args: Map<String, String> = emptyMap()) {
        if (permissionRequestInProgress) return

        val oldRoute = previousRoute
        val newlyRequired = route.features - (oldRoute?.features ?: emptySet())

        scope.launch {
            permissionRequestInProgress = true

            permissionOrchestrator.ensurePermissions(

                newlyRequiredFeatures = newlyRequired,
                onGranted = {
                    val finalPath = when (route) {
                        Route.Gatt -> Route.Gatt.path(args["address"]!!)
                        else -> route.path
                    }
                    navController.navigate(finalPath)
                },
                onDenied = {
                    val featureArg: String = newlyRequired.joinToString(",") { it.name }
                    val finalPath = Route.PermissionDenied.path(featureArg)
                    navController.navigate(finalPath)
                }
            )

            permissionRequestInProgress = false
        }
    }

    fun navigateBack(): Unit {
        navController.popBackStack()
    }

    // Log route changes
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            Log.d(LogTag, "Navigated to route: ${entry.destination.route}")
        }
    }

    NavHost(
        navController as NavHostController,
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
                Route.WifiInfo,
                Route.Imu,
            )

            DashboardScreen(
                routes = dashboardButtons,
                onNavigate = { route -> attemptNavigate(route) }
            )
        }

        // ------------------------------
        // Failure
        // ------------------------------
        composable(
            Route.PermissionDenied.path,
            arguments = listOf(navArgument("features") { type = NavType.StringType })
        ) { backStackEntry ->
            enter(Route.Dashboard)

            val raw = backStackEntry.arguments?.getString("features") ?: ""
            val features: List<Feature> = raw
                .split(",")
                .mapNotNull { name ->
                    runCatching { Feature.valueOf(name) }.getOrNull()
                }
            val requiredPermissions =
                permissionOrchestrator.getRequiredPermissions(features.toSet())
            PermissionDeniedScreen(
                features = features,
                requiredPermissions = requiredPermissions,
                onBack = ::navigateBack
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
            val viewModel: ProgramAssistViewModel = viewModel()
            ProgramAssistScreen(viewModel)
        }

        composable(Route.CameraRemote.path) {
            enter(Route.CameraRemote)
            val viewModel: CameraRemoteViewModel = viewModel()
            CameraRemoteScreen(
                viewModel
            )
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
            val viewModel: StratagemViewModel = viewModel()
            StratagemScreen(viewModel)
        }

        composable(Route.Pong.path) {
            enter(Route.Pong)
            val viewModel: PongViewModel = viewModel()
            PongScreen(
                navigateBack = ::navigateBack,
                viewModel
            )
        }

        composable(Route.Tilt.path) {
            enter(Route.Tilt)
            val viewModel: TiltViewModel = viewModel()
            TiltScreen(
                navigateBack = ::navigateBack,
                viewModel
            )
        }

        // ------------------------------
        // Debug / Misc
        // ------------------------------
        composable(Route.Ble.path) {
            enter(Route.Ble)
            val viewModel: BleViewModel = viewModel()
            BleScreen(
                attemptNavigate = ::attemptNavigate,
                viewModel
            )
        }
        composable(Route.WifiInfo.path) {
            enter(Route.WifiInfo)
            val viewModel: WifiNetworkInfoViewModel = viewModel()
            val connectionViewModel: WifiConnectionViewModel = viewModel()
            WifiInfoScreen(
                attemptNavigate = ::attemptNavigate,
                viewModel = viewModel,
                connectionViewModel = connectionViewModel
            )
        }
        composable(Route.WifiTools.path) {
            enter(Route.WifiTools)
            val viewModel: WifiConnectionViewModel = viewModel()
            WifiToolsScreen(
                attemptNavigate = ::attemptNavigate,
                navigateBack = ::navigateBack,
                viewModel
            )
        }
        composable(Route.WifiLogs.path) {
            enter(Route.WifiLogs)
            val viewModel: WifiPacketViewModel = viewModel()
            WifiLogScreen(
                navigateBack = ::navigateBack,
                viewModel
            )
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