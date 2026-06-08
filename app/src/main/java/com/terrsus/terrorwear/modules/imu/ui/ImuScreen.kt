package com.terrsus.terrorwear.modules.imu.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedModifier
import androidx.wear.compose.foundation.CurvedScope
import androidx.wear.compose.foundation.curvedComposable
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.size
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.terrsus.terrorwear.AppContainer

/**
 * IMU debug screen.
 *
 * Shows:
 * - Raw acceleration
 * - Orientation (yaw, pitch, roll)
 * - Arc indicators around the bezel
 * - A small rolling graph of yaw changes
 */
@Composable
fun ImuScreen() {
    val sensorManager = AppContainer.sensorManager

    val orientation = sensorManager.orientation.collectAsState()
    val accel = sensorManager.acceleration.collectAsState()

    // Rolling history for yaw graph
    val maxPoints = 60
    var yawHistory by remember { mutableStateOf(List(maxPoints) { 0f }) }

    LaunchedEffect(orientation.value.yaw) {
        yawHistory = (yawHistory + orientation.value.yaw).takeLast(maxPoints)
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 6.dp.toPx()

                // Outermost radius (touching bezel)
                val outerRadius = size.minDimension / 2f - stroke

                // Each arc is inset by one stroke width
                val yawRadius = outerRadius
                val pitchRadius = outerRadius - stroke * 1.5f
                val rollRadius = outerRadius - stroke * 3f
                val testRadius = outerRadius - stroke * 4.5f

                // Helper to draw a centered arc at a given radius
                fun drawArcAtRadius(color: Color, sweep: Float, radius: Float) {
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(
                            x = size.width / 2f - radius,
                            y = size.height / 2f - radius
                        ),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        style = Stroke(width = stroke)
                    )
                }

                // Aviation‑standard IMU colors:
                // Yaw = Yellow
                // Pitch = Green
                // Roll = Magenta

                drawArcAtRadius(Color.Yellow, orientation.value.yaw, yawRadius)
                drawArcAtRadius(Color.Green, orientation.value.pitch, pitchRadius)
                drawArcAtRadius(Color.Magenta, orientation.value.roll, rollRadius)

                // Test arc (inner)
                drawArcAtRadius(Color.Red, 20f, testRadius)
            }

            // --- Main content ---
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                autoCentering = AutoCenteringParams(itemIndex = 0)
            ) {
                item { Text("IMU") }

                item {
                    Text("Accel: ${accel.value.x}, ${accel.value.y}, ${accel.value.z}")
                }

                item {
                    Text(
                        "Orientation: " +
                                "${orientation.value.yaw.toInt()}, " +
                                "${orientation.value.pitch.toInt()}, " +
                                "${orientation.value.roll.toInt()}"
                    )
                }

                item {
                    Spacer(Modifier.height(6.dp))
                    Text("Yaw Graph")
                }

                item {
                    YawGraph(yawHistory)
                }
            }
        }
    }
}

/**
 * Simple line graph showing yaw changes over time.
 */
@Composable
fun YawGraph(values: List<Float>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        if (values.isEmpty()) return@Canvas

        val max = 360f
        val stepX = size.width / (values.size - 1).coerceAtLeast(1)
        var last = Offset(0f, size.height - (values.first() / max) * size.height)

        values.forEachIndexed { i, v ->
            val x = i * stepX
            val y = size.height - (v / max) * size.height
            val point = Offset(x, y)
            drawLine(Color.Cyan, last, point, strokeWidth = 2f)
            last = point
        }
    }
}