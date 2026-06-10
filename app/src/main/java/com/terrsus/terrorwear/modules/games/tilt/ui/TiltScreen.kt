package com.terrsus.terrorwear.modules.games.tilt.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.wear.compose.material.TimeText
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.features.games.tilt.domain.model.TiltPhase
import com.terrsus.terrorwear.features.games.tilt.domain.logic.TiltSensorRepositoryImpl
import com.terrsus.terrorwear.modules.games.tilt.ui.draw.*
import com.terrsus.terrorwear.modules.games.tilt.viewmodel.TiltViewModel
import kotlinx.coroutines.isActive

/**
 * Tilt game screen.
 *
 * @param navController navigation controller for leaving the screen
 * @param viewModel the TiltViewModel instance (pure game logic)
 */
@Composable
fun TiltScreen(
    navigateBack: () -> Unit,
    viewModel: TiltViewModel
) {
    // SensorManager needs context so it lives here
    val sensorManager = AppContainer.sensorManager

    // Repository wraps sensor callbacks
    val sensorRepo = remember { TiltSensorRepositoryImpl(sensorManager) }

    // Start sensors when screen appears
    LaunchedEffect(Unit) {
        sensorRepo.start()
    }

    // Stop sensors when screen disappears
    DisposableEffect(Unit) {
        onDispose { sensorRepo.stop() }
    }

    // Send tilt updates to the viewmodel
    LaunchedEffect(sensorRepo) {
        sensorManager.orientation.collect { orientation ->
            viewModel.onTilt(orientation)
        }
    }

    // Send tap events to the viewmodel
    LaunchedEffect(sensorRepo) {
        sensorManager.onTap = {
            viewModel.onTap()
        }
    }

    // Game loop lives here because only the screen has a frame clock
    LaunchedEffect(Unit) {
        viewModel.startGame()

        var lastFrameTime = 0L

        while (isActive) {
            withFrameNanos { frameTime ->
                if (lastFrameTime != 0L) {
                    val dtSec = (frameTime - lastFrameTime) / 1_000_000_000f
                    val nowMillis = frameTime / 1_000_000L
                    viewModel.step(dtSec, nowMillis)
                }
                lastFrameTime = frameTime
            }
        }
    }

    val state by viewModel.state.collectAsState()
    val phase by viewModel.phase.collectAsState()

    // Back button logic
    BackHandler {
        val consumed = viewModel.onBackPressed()
        if (!consumed) navigateBack()
    }


    TimeText()

    Box(modifier = Modifier.fillMaxSize()) {

        // Draw everything
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBall(state.ball)
            drawObstacles(state.obstacles)
            drawWalls(state.walls)
            drawPowerups(state.powerups)
            drawBorderIndicators(state.obstacles)
            drawFieldIndicators(state.ball, state.obstacles)
            drawCrtOverlay()
            drawScreenWarpGrid()
        }

        // Pause overlay
        if (phase == TiltPhase.Paused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                PauseOverlay(
                    onResume = { viewModel.resume() },
                    onRestart = { viewModel.startGame() }
                )
            }
        }
    }
}