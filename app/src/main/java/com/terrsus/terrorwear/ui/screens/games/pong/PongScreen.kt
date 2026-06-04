package com.terrsus.terrorwear.ui.screens.games.pong

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.terrsus.terrorwear.domain.games.pong.model.GameState
import com.terrsus.terrorwear.domain.games.pong.model.PongPhase
import com.terrsus.terrorwear.viewmodel.games.pong.PongViewModel

/**
 * Main Pong game screen. Handles rendering, input, and frame updates.
 */
@Composable
fun PongScreen(
    navController: NavHostController,
    viewModel: PongViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val phase by viewModel.phase.collectAsState()

    // Game loop
    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (true) {
            withFrameNanos { time ->
                if (lastTime != 0L && phase == PongPhase.Playing) {
                    val dt = (time - lastTime) / 1_000_000_000f
                    viewModel.step(dt)
                }
                lastTime = time
            }
        }
    }

    // Screen size → ViewModel initialization
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        val widthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
        val heightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
        viewModel.initialize(widthPx, heightPx)
    }

    // Back button
    BackHandler {
        val handled = viewModel.onBackPressed()
        if (!handled) navController.popBackStack()
    }

    TimeText()

    Box(
        Modifier
            .fillMaxSize()
            .let { if (phase == PongPhase.Paused) it.blur(5.dp) else it }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (phase == PongPhase.Menu) viewModel.startGame()
                    }
                    detectDragGestures { _, dragAmount ->
                        viewModel.onPlayerDrag(dragAmount.y)
                    }
                }
        ) {
            when (phase) {
                PongPhase.Menu -> {
                    drawCenteredText("Tap to Start")
                }

                PongPhase.Playing,
                PongPhase.Paused -> {
                    drawBall(state)
                    drawPaddles(state)
                    drawScore(state)
                    drawTimer(state)
                }
            }
        }
    }

    if (phase == PongPhase.Paused)
        PausedScreen(viewModel)
}

/**
 * Pause overlay with resume and restart buttons.
 */
@Composable
fun PausedScreen(viewModel: PongViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .blur(20.dp)
    )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.resume() }) {
            Text("Resume")
        }
        Button(onClick = { viewModel.restart() }) {
            Text("Restart")
        }
    }
}