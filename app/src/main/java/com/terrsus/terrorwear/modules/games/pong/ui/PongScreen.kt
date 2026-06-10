package com.terrsus.terrorwear.modules.games.pong.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.features.games.pong.domain.model.Collision
import com.terrsus.terrorwear.features.games.pong.domain.model.PongPhase
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawBall
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawCenteredText
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawField
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawPaddles
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawScore
import com.terrsus.terrorwear.modules.games.pong.ui.draw.drawTimer
import com.terrsus.terrorwear.modules.games.pong.viewmodel.PongViewModel

/**
 * Pong game screen. Draws the game, handles input, and runs the frame loop.
 *
 * @param navController Navigation controller for back navigation.
 * @param viewModel ViewModel containing game state and logic.
 */
@Composable
fun PongScreen(
    navigateBack: () -> Unit,
    viewModel: PongViewModel
) {
    val state by viewModel.state.collectAsState()
    val phase by viewModel.phase.collectAsState()
    val haptics = LocalHapticFeedback.current

    // Game loop
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L && phase == PongPhase.Playing) {
                    val dt = (now - last) / 1_000_000_000f
                    viewModel.step(dt)
                }
                last = now
            }
        }
    }

    // Trigger haptics whenever relevant events are emitted
    LaunchedEffect(Unit) {
        viewModel.event.collect { collision ->
            when (collision) {
                Collision.Wall -> haptics.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                Collision.Paddle -> haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                Collision.Score.Player -> haptics.performHapticFeedback(HapticFeedbackType.Reject)
                Collision.Score.Enemy -> haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                else -> Unit
            }
        }
    }

    // Initialize with screen size
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        val w = with(density) { config.screenWidthDp.dp.toPx() }
        val h = with(density) { config.screenHeightDp.dp.toPx() }
        viewModel.initialize(w, h)
    }

    // Back button
    BackHandler {
        val handled = viewModel.onBackPressed()
        if (!handled) navigateBack()
    }

    // Wear OS vignette
    Vignette(vignettePosition = VignettePosition.Top)

    TimeText()

    // Game canvas
    Box(
        Modifier
            .fillMaxSize()
            .let { if (phase == PongPhase.Paused) it.blur(3.dp) else it }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (phase == PongPhase.Menu) {
                            haptics.performHapticFeedback(
                                HapticFeedbackType.LongPress
                            )
                            viewModel.startGame()
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { _, drag ->
                        viewModel.onPlayerDrag(drag.y)
                    }
                }
        ) {
            when (phase) {
                PongPhase.Menu -> {
                    drawCenteredText("Pong", y = size.height * 0.3f)
                    drawCenteredText("Tap to Start")
                }

                PongPhase.Playing,
                PongPhase.Paused -> {
                    drawField(state, viewModel.rules)
                    drawBall(state)
                    drawPaddles(state)

                    drawScore(state)
                    drawTimer(state)
                }
            }
        }
    }

    // Pause overlay
    AnimatedVisibility(
        visible = phase == PongPhase.Paused,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(200))
    ) {
        PausedOverlay(viewModel, haptics)
    }
}