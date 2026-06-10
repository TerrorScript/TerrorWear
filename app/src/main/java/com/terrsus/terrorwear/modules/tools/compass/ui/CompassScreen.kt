package com.terrsus.terrorwear.modules.tools.compass.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.AppContainer
import kotlin.math.roundToInt

/**
 * A visually enhanced compass screen combining:
 * - A rotating needle
 * - A circular dial with tick marks
 * - A cardinal direction ring
 * - A subtle radial background gradient
 * - Haptic feedback when crossing cardinal directions
 *
 * This design is lightweight and optimized for Wear OS.
 */
@Composable
fun CompassScreen() {
    val sensorManager = AppContainer.sensorManager
    val orientation = sensorManager.orientation.collectAsState()

    val yaw = orientation.value.yaw
    val animatedYaw = rememberCircularYaw(yaw)

    val haptics = LocalHapticFeedback.current
    var lastCardinal by remember { mutableStateOf<Int?>(null) }

    // Detect cardinal crossings (0, 90, 180, 270)
    val cardinal = ((yaw / 90f).roundToInt() * 90).mod(360)
    if (cardinal != lastCardinal) {
        lastCardinal = cardinal
        haptics.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF202020), Color.Black),
                    radius = 300f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CompassDial(animatedYaw)
    }
}

@Composable
fun rememberCircularYaw(yaw: Float): Float {
    var previous by remember { mutableStateOf(yaw) }

    // Compute shortest angular distance
    val delta = ((yaw - previous + 540f) % 360f) - 180f
    val target = previous + delta

    previous = target

    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    return animated
}